package edu.utexas.cs.cs312;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class CheckStyleResult {

    public List<CheckStyleError> errors;
    public String sourceCode;
    public int resultCode;

    public CheckStyleResult(int resultCode, String sourceCode, List<CheckStyleError> errors) {
        this.resultCode = resultCode;
        this.sourceCode = sourceCode;
        this.errors = ImmutableList.copyOf(errors);
    }

    public static class CheckStyleError {
        public int lineNumber;
        public int columnNumber;
        public String message;
        public String infoUrl;

        public CheckStyleError(int lineNumber, int columnNumber, String message) {
            this.lineNumber = lineNumber;
            this.columnNumber = columnNumber;
            this.message = message;
        }

        public CheckStyleError(int lineNumber, int columnNumber, String message, String infoUrl) {
            this(lineNumber, columnNumber, message);
            this.infoUrl = infoUrl;
        }

    }
}
