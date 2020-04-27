import React from 'react';
import Alert from "react-bootstrap/Alert";
import {PrismLight as SyntaxHighlighter} from '../SyntaxHighlighter';
import java from '../SyntaxHighlighter/languages/prism/java';
import tomorrow from '../SyntaxHighlighter/styles/prism/tomorrow';

import './index.css';

// @ts-ignore
SyntaxHighlighter.registerLanguage('java', java);

export const LINE_ERROR_CLASS_NAME = "line-error";
export const LINE_NO_ERROR_CLASS_NAME = "line-no-error";

type Props = {
    fileResults: APIResult | null,
}

export type APIResult = {
    result: CheckStyleResult | null
    error: string | null,
}

export type CheckStyleResult = {
    errors: CheckStyleError[],
    sourceCode: string,
    resultCode: bigint,
}

type CheckStyleError = {
    lineNumber: number,
    columnNumber: number,
    message: string
}

export default function StyleResult(props: Props) {

    if (props.fileResults == null) {
        return (
            <div>
                Unknown error occurred
            </div>
        )
    }

    // Exception from CheckStyle
    if (props.fileResults.error != null) {
        return (
            <Alert variant="danger">
                <Alert.Heading>Oh snap! You got an error!</Alert.Heading>
                <p>
                    {props.fileResults.error}
                </p>
            </Alert>
        )
    }

    if (props.fileResults.result == null) {
        return (
            <div>
                Unknown error occurred
            </div>
        )
    }

    if (props.fileResults.result.errors.length === 0) {
        return (
            <div>
                No errors found!
            </div>
        )
    }

    let errorLineNumbers: number[] = [];
    for (const error of props.fileResults.result.errors) {
        errorLineNumbers.push(error.lineNumber);
    }

    return (
        <div className="container">
            {
                <div className="row">
                    <ul>
                        {props.fileResults.result.errors.map(
                            error => (
                                <li>
                                    {error.lineNumber}:
                                    {error.columnNumber} --
                                    {error.message}
                                </li>
                            )
                        )}
                    </ul>
                </div>
            }

            <div className="row">
                <SyntaxHighlighter
                    className="col-md"
                    language="java"
                    style={tomorrow}
                    wrapLines
                    renderer={null}
                    astGenerator={null}
                    lineProps={
                        (lineNumber: number) => {
                            if (errorLineNumbers.includes(lineNumber)) {
                                return {
                                    className: LINE_ERROR_CLASS_NAME,
                                }
                            } else {
                                return {
                                    className: LINE_NO_ERROR_CLASS_NAME,
                                }
                            }

                        }
                    }
                    showLineNumbers
                >
                    {props.fileResults.result.sourceCode}
                </SyntaxHighlighter>
            </div>

        </div>
    )
}