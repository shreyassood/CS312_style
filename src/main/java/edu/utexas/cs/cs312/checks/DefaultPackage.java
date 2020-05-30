package edu.utexas.cs.cs312.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Check which makes sure class is part
 * of default package
 * (No package statement)
 */
public class DefaultPackage extends AbstractCheck {

    public static final String MSG_KEY = "default.package";

    @Override
    public int[] getDefaultTokens() {
        return getRequiredTokens();
    }

    @Override
    public int[] getAcceptableTokens() {
        return getRequiredTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return new int[] {TokenTypes.PACKAGE_DEF};
    }

    @Override
    public void visitToken(DetailAST ast) {
        final DetailAST nameAST = ast.getLastChild().getPreviousSibling();
        log(nameAST, MSG_KEY);
    }
}
