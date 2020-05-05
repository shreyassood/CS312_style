package edu.utexas.cs.cs312;


import com.google.common.collect.ImmutableMap;

public class ResultInfo {

    // Stores additional information urls for custom modules in Checkstyle
    // Key -> Source Name
    // Value -> String url
    public final static ImmutableMap<String, String> INFO_URLS =
            ImmutableMap.<String, String>builder()
                    .put(
                            "com.puppycrawl.tools.checkstyle.checks.coding.MagicNumberCheck",
                            //TODO Add actual link
                            "https://wikis.utexas.edu/"
                    )
                    .build();

}
