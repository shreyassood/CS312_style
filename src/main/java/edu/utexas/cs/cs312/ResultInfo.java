package edu.utexas.cs.cs312;


import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.checks.coding.MagicNumberCheck;
import com.puppycrawl.tools.checkstyle.checks.sizes.LineLengthCheck;

public class ResultInfo {

    // Stores additional information urls for custom modules in Checkstyle
    // Key -> Source Name
    // Value -> String url
    public final static ImmutableMap<String, String> INFO_URLS =
            ImmutableMap.<String, String>builder()
                    .put(
                            MagicNumberCheck.class.getName(),
                            "https://wikis.utexas.edu/display/cs312style/Magic+Literals"
                    )
                    .put(
                            LineLengthCheck.class.getName(),
                            "https://wikis.utexas.edu/display/cs312style/Code+Cleanliness"
                    )
                    .build();

}
