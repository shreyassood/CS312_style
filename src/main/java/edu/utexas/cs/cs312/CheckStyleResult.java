package edu.utexas.cs.cs312;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class CheckStyleResult {

    public List<CheckStyleError> errors;
    public int resultCode;

    public CheckStyleResult(int resultCode, List<CheckStyleError> errors) {
        this.resultCode = resultCode;
        this.errors = ImmutableList.copyOf(errors);
    }

    public static class CheckStyleError {
        public int lineNumber;
        public int columnNumber;
        public String message;

        public CheckStyleError(int lineNumber, int columnNumber, String message) {
            this.lineNumber = lineNumber;
            this.columnNumber = columnNumber;
            this.message = message;
        }

    }
}
