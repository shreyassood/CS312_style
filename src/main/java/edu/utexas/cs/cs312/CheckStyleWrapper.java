package edu.utexas.cs.cs312;

import com.puppycrawl.tools.checkstyle.*;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import org.springframework.web.util.HtmlUtils;
import picocli.CommandLine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// From CheckStyle Source (Main)
public class CheckStyleWrapper {

    public static ArrayList<String> runCheckStyle(File inputFile) throws IOException, CheckstyleException {
        File outputFile = File.createTempFile("output-temp", ".xml");

        int resultCode = runCheckStyle(
                outputFile.toPath(),
                Collections.singletonList(inputFile)
        );

        // TODO Parse XML Result
        ArrayList<String> resultStrings = new ArrayList<>();
        resultStrings.add("Result code: " + resultCode + "<br/>");
        Scanner scanner = new Scanner(outputFile);
        while (scanner.hasNextLine()) {
            resultStrings.add(HtmlUtils.htmlEscape(scanner.nextLine()));
            resultStrings.add("<br/>");
        }

        return resultStrings;
    }

    private static int runCheckStyle(Path outputPath, List<File> filesToProcess) throws IOException, CheckstyleException {
        CliOptions options = new CliOptions();
        options.configurationFile = "/google_checks.xml";
        options.format = OutputFormat.XML;
        options.outputPath = outputPath;
        return runCheckstyle(options, filesToProcess);
    }

    /**
     * Executes required Checkstyle actions based on passed parameters.
     *
     * @param options        user-specified options
     * @param filesToProcess the list of files whose style to check
     * @return number of violations of ERROR level
     * @throws IOException         when output file could not be found
     * @throws CheckstyleException when properties file could not be loaded
     */
    private static int runCheckstyle(CliOptions options, List<File> filesToProcess)
            throws CheckstyleException, IOException {
        // setup the properties
        final Properties props;

//        if (options.propertiesFile == null) {
        props = System.getProperties();
//        }
//        else {
//            props = loadProperties(options.propertiesFile);
//        }

        // create a configuration
        final ThreadModeSettings multiThreadModeSettings =
                new ThreadModeSettings(options.checkerThreadsNumber,
                        options.treeWalkerThreadsNumber);

        final ConfigurationLoader.IgnoredModulesOptions ignoredModulesOptions;
        if (options.executeIgnoredModules) {
            ignoredModulesOptions = ConfigurationLoader.IgnoredModulesOptions.EXECUTE;
        } else {
            ignoredModulesOptions = ConfigurationLoader.IgnoredModulesOptions.OMIT;
        }

        final Configuration config = ConfigurationLoader.loadConfiguration(
                options.configurationFile, new PropertiesExpander(props),
                ignoredModulesOptions, multiThreadModeSettings);

        // create RootModule object and run it
        final int errorCounter;
        final ClassLoader moduleClassLoader = Checker.class.getClassLoader();
        final RootModule rootModule = getRootModule(config.getName(), moduleClassLoader);

        try {
            final AuditListener listener;
//            if (options.generateXpathSuppressionsFile) {
//                // create filter to print generated xpath suppressions file
//                final Configuration treeWalkerConfig = getTreeWalkerConfig(config);
//                if (treeWalkerConfig != null) {
//                    final DefaultConfiguration moduleConfig =
//                            new DefaultConfiguration(
//                                    XpathFileGeneratorAstFilter.class.getName());
//                    moduleConfig.addAttribute(Main.CliOptions.ATTRIB_TAB_WIDTH_NAME,
//                            String.valueOf(options.tabWidth));
//                    ((DefaultConfiguration) treeWalkerConfig).addChild(moduleConfig);
//                }
//
//                listener = new XpathFileGeneratorAuditListener(getOutputStream(options.outputPath),
//                        getOutputStreamOptions(options.outputPath));
//            }
//            else {
            listener = createListener(options.format, options.outputPath);
//            }

            rootModule.setModuleClassLoader(moduleClassLoader);
            rootModule.configure(config);
            rootModule.addListener(listener);

            // run RootModule
            errorCounter = rootModule.process(filesToProcess);
        } finally {
            rootModule.destroy();
        }

        return errorCounter;
    }

    /**
     * Enumeration over the possible output formats.
     *
     * @noinspection PackageVisibleInnerClass
     */
    // Package-visible for tests.
    enum OutputFormat {
        /**
         * XML output format.
         */
        XML,
        /**
         * Plain output format.
         */
        PLAIN;

        /**
         * Returns a new AuditListener for this OutputFormat.
         *
         * @param out     the output stream
         * @param options the output stream options
         * @return a new AuditListener for this OutputFormat
         */
        public AuditListener createListener(OutputStream out,
                                            AutomaticBean.OutputStreamOptions options) {
            final AuditListener result;
            if (this == XML) {
                result = new XMLLogger(out, options);
            } else {
                result = new DefaultLogger(out, options);
            }
            return result;
        }

        /**
         * Returns the name in lowercase.
         *
         * @return the enum name in lowercase
         */
        @Override
        public String toString() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    /**
     * This method creates in AuditListener an open stream for validation data, it must be
     * closed by {@link RootModule} (default implementation is {@link Checker}) by calling
     * {@link AuditListener#auditFinished(AuditEvent)}.
     *
     * @param format         format of the audit listener
     * @param outputLocation the location of output
     * @return a fresh new {@code AuditListener}
     * @throws IOException when provided output location is not found
     */
    private static AuditListener createListener(OutputFormat format, Path outputLocation)
            throws IOException {
        final OutputStream out = getOutputStream(outputLocation);
        final AutomaticBean.OutputStreamOptions closeOutputStreamOption =
                getOutputStreamOptions(outputLocation);
        return format.createListener(out, closeOutputStreamOption);
    }

    /**
     * Create output stream or return System.out
     *
     * @param outputPath output location
     * @return output stream
     * @throws IOException might happen
     * @noinspection UseOfSystemOutOrSystemErr
     */
    @SuppressWarnings("resource")
    private static OutputStream getOutputStream(Path outputPath) throws IOException {
        final OutputStream result;
        if (outputPath == null) {
            result = System.out;
        } else {
            result = Files.newOutputStream(outputPath);
        }
        return result;
    }

    /**
     * Create {@link AutomaticBean.OutputStreamOptions} for the given location.
     *
     * @param outputPath output location
     * @return output stream options
     */
    private static AutomaticBean.OutputStreamOptions getOutputStreamOptions(Path outputPath) {
        final AutomaticBean.OutputStreamOptions result;
        if (outputPath == null) {
            result = AutomaticBean.OutputStreamOptions.NONE;
        } else {
            result = AutomaticBean.OutputStreamOptions.CLOSE;
        }
        return result;
    }

    /**
     * Creates a new instance of the root module that will control and run
     * Checkstyle.
     *
     * @param name              The name of the module. This will either be a short name that
     *                          will have to be found or the complete package name.
     * @param moduleClassLoader Class loader used to load the root module.
     * @return The new instance of the root module.
     * @throws CheckstyleException if no module can be instantiated from name
     */
    private static RootModule getRootModule(String name, ClassLoader moduleClassLoader)
            throws CheckstyleException {
        final ModuleFactory factory = new PackageObjectFactory(
                Checker.class.getPackage().getName(), moduleClassLoader);

        return (RootModule) factory.createModule(name);
    }

    /**
     * Command line options.
     *
     * @noinspection unused, FieldMayBeFinal, CanBeFinal,
     * MismatchedQueryAndUpdateOfCollection, LocalCanBeFinal
     */
    @CommandLine.Command(name = "checkstyle", description = "Checkstyle verifies that the specified "
            + "source code files adhere to the specified rules. By default violations are "
            + "reported to standard out in plain format. Checkstyle requires a configuration "
            + "XML file that configures the checks to apply.",
            mixinStandardHelpOptions = true)
    private static class CliOptions {

        /**
         * Width of CLI help option.
         */
        private static final int HELP_WIDTH = 100;

        /**
         * The default number of threads to use for checker and the tree walker.
         */
        private static final int DEFAULT_THREAD_COUNT = 1;

        /**
         * Name for the moduleConfig attribute 'tabWidth'.
         */
        private static final String ATTRIB_TAB_WIDTH_NAME = "tabWidth";

        /**
         * Default output format.
         */
        private static final OutputFormat DEFAULT_OUTPUT_FORMAT = OutputFormat.PLAIN;

        /**
         * Option name for output format.
         */
        private static final String OUTPUT_FORMAT_OPTION = "-f";

        /**
         * List of file to validate.
         */
        @CommandLine.Parameters(arity = "1..*", description = "One or more source files to verify")
        private List<File> files;

        /**
         * Config file location.
         */
        @CommandLine.Option(names = "-c", description = "Sets the check configuration file to use.")
        private String configurationFile;

        /**
         * Output file location.
         */
        @CommandLine.Option(names = "-o", description = "Sets the output file. Defaults to stdout")
        private Path outputPath;

        /**
         * Properties file location.
         */
        @CommandLine.Option(names = "-p", description = "Loads the properties file")
        private File propertiesFile;

        /**
         * LineNo and columnNo for the suppression.
         */
        @CommandLine.Option(names = "-s",
                description = "Print xpath suppressions at the file's line and column position. "
                        + "Argument is the line and column number (separated by a : ) in the file "
                        + "that the suppression should be generated for")
        private String suppressionLineColumnNumber;

        /**
         * Tab character length.
         * Suppression: CanBeFinal - we use picocli and it use  reflection to manage such fields
         *
         * @noinspection CanBeFinal
         */
        @CommandLine.Option(names = {"-w", "--tabWidth"}, description = "Sets the length of the tab character. "
                + "Used only with \"-s\" option. Default value is ${DEFAULT-VALUE}")
        private int tabWidth = CommonUtil.DEFAULT_TAB_WIDTH;

        /**
         * Switch whether to generate suppressions file or not.
         */
        @CommandLine.Option(names = {"-g", "--generate-xpath-suppression"},
                description = "Generates to output a suppression xml to use to suppress all"
                        + " violations from user's config")
        private boolean generateXpathSuppressionsFile;

        /**
         * Output format.
         * Suppression: CanBeFinal - we use picocli and it use  reflection to manage such fields
         *
         * @noinspection CanBeFinal
         */
        @CommandLine.Option(names = "-f", description = "Sets the output format. Valid values: "
                + "${COMPLETION-CANDIDATES}. Defaults to ${DEFAULT-VALUE}")
        private OutputFormat format = DEFAULT_OUTPUT_FORMAT;

        /**
         * Option that controls whether to print the AST of the file.
         */
        @CommandLine.Option(names = {"-t", "--tree"},
                description = "Print Abstract Syntax Tree(AST) of the file")
        private boolean printAst;

        /**
         * Option that controls whether to print the AST of the file including comments.
         */
        @CommandLine.Option(names = {"-T", "--treeWithComments"},
                description = "Print Abstract Syntax Tree(AST) of the file including comments")
        private boolean printAstWithComments;

        /**
         * Option that controls whether to print the parse tree of the javadoc comment.
         */
        @CommandLine.Option(names = {"-j", "--javadocTree"},
                description = "Print Parse tree of the Javadoc comment")
        private boolean printJavadocTree;

        /**
         * Option that controls whether to print the full AST of the file.
         */
        @CommandLine.Option(names = {"-J", "--treeWithJavadoc"},
                description = "Print full Abstract Syntax Tree of the file")
        private boolean printTreeWithJavadoc;

        /**
         * Option that controls whether to print debug info.
         */
        @CommandLine.Option(names = {"-d", "--debug"},
                description = "Print all debug logging of CheckStyle utility")
        private boolean debug;

        /**
         * Option that allows users to specify a list of paths to exclude.
         * Suppression: CanBeFinal - we use picocli and it use  reflection to manage such fields
         *
         * @noinspection CanBeFinal
         */
        @CommandLine.Option(names = {"-e", "--exclude"},
                description = "Directory/File path to exclude from CheckStyle")
        private List<File> exclude = new ArrayList<>();

        /**
         * Option that allows users to specify a regex of paths to exclude.
         * Suppression: CanBeFinal - we use picocli and it use  reflection to manage such fields
         *
         * @noinspection CanBeFinal
         */
        @CommandLine.Option(names = {"-x", "--exclude-regexp"},
                description = "Regular expression of directory/file to exclude from CheckStyle")
        private List<Pattern> excludeRegex = new ArrayList<>();

        /**
         * Switch whether to execute ignored modules or not.
         */
        @CommandLine.Option(names = {"-E", "--executeIgnoredModules"},
                description = "Allows ignored modules to be run.")
        private boolean executeIgnoredModules;

        /**
         * The checker threads number.
         * Suppression: CanBeFinal - we use picocli and it use  reflection to manage such fields
         *
         * @noinspection CanBeFinal
         */
        @CommandLine.Option(names = {"-C", "--checker-threads-number"}, description = "(experimental) The "
                + "number of Checker threads (must be greater than zero)")
        private int checkerThreadsNumber = DEFAULT_THREAD_COUNT;

        /**
         * The tree walker threads number.
         * Suppression: CanBeFinal - we use picocli and it use  reflection to manage such fields
         *
         * @noinspection CanBeFinal
         */
        @CommandLine.Option(names = {"-W", "--tree-walker-threads-number"}, description = "(experimental) The "
                + "number of TreeWalker threads (must be greater than zero)")
        private int treeWalkerThreadsNumber = DEFAULT_THREAD_COUNT;

        /**
         * Show AST branches that match xpath.
         */
        @CommandLine.Option(names = {"-b", "--branch-matching-xpath"},
                description = "Show Abstract Syntax Tree(AST) branches that match XPath")
        private String xpath;

        /**
         * Gets the list of exclusions provided through the command line arguments.
         *
         * @return List of exclusion patterns.
         */
        private List<Pattern> getExclusions() {
            final List<Pattern> result = exclude.stream()
                    .map(File::getAbsolutePath)
                    .map(Pattern::quote)
                    .map(pattern -> Pattern.compile("^" + pattern + "$"))
                    .collect(Collectors.toCollection(ArrayList::new));
            result.addAll(excludeRegex);
            return result;
        }


        /**
         * Validates the user-specified command line options.
         *
         * @param parseResult    used to verify if the format option was specified on the command line
         * @param filesToProcess the list of files whose style to check
         * @return list of violations
         */
        // -@cs[CyclomaticComplexity] Breaking apart will damage encapsulation
        private List<String> validateCli(CommandLine.ParseResult parseResult, List<File> filesToProcess) {
            final List<String> result = new ArrayList<>();
            final boolean hasConfigurationFile = configurationFile != null;
            final boolean hasSuppressionLineColumnNumber = suppressionLineColumnNumber != null;

            if (filesToProcess.isEmpty()) {
                result.add("Files to process must be specified, found 0.");
            }
            // ensure there is no conflicting options
            else if (printAst || printAstWithComments || printJavadocTree || printTreeWithJavadoc
                    || xpath != null) {
                if (suppressionLineColumnNumber != null || configurationFile != null
                        || propertiesFile != null || outputPath != null
                        || parseResult.hasMatchedOption(OUTPUT_FORMAT_OPTION)) {
                    result.add("Option '-t' cannot be used with other options.");
                } else if (filesToProcess.size() > 1) {
                    result.add("Printing AST is allowed for only one file.");
                }
            } else if (hasSuppressionLineColumnNumber) {
                if (configurationFile != null || propertiesFile != null
                        || outputPath != null
                        || parseResult.hasMatchedOption(OUTPUT_FORMAT_OPTION)) {
                    result.add("Option '-s' cannot be used with other options.");
                } else if (filesToProcess.size() > 1) {
                    result.add("Printing xpath suppressions is allowed for only one file.");
                }
            } else if (hasConfigurationFile) {
                try {
                    // test location only
                    CommonUtil.getUriByFilename(configurationFile);
                } catch (CheckstyleException ignored) {
                    final String msg = "Could not find config XML file '%s'.";
                    result.add(String.format(Locale.ROOT, msg, configurationFile));
                }
                result.addAll(validateOptionalCliParametersIfConfigDefined());
            } else {
                result.add("Must specify a config XML file.");
            }

            return result;
        }

        /**
         * Validates optional command line parameters that might be used with config file.
         *
         * @return list of violations
         */
        private List<String> validateOptionalCliParametersIfConfigDefined() {
            final List<String> result = new ArrayList<>();
            if (propertiesFile != null && !propertiesFile.exists()) {
                result.add(String.format(Locale.ROOT,
                        "Could not find file '%s'.", propertiesFile));
            }
            if (checkerThreadsNumber < 1) {
                result.add("Checker threads number must be greater than zero");
            }
            if (treeWalkerThreadsNumber < 1) {
                result.add("TreeWalker threads number must be greater than zero");
            }
            return result;
        }
    }
}
