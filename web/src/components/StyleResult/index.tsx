import React from 'react';
import {Alert, Col, Row} from 'antd';
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
            <Alert
                type="error"
                message="Oh snap! You got an error!"
                description="Problem communicating with server, please try again later."
            />
        )
    }

    // Exception from CheckStyle
    if (props.fileResults.error != null) {
        return (
            <Alert
                type="error"
                message="Oh snap! You got an error!"
                description={props.fileResults.error}
            />
        )
    }

    if (props.fileResults.result == null) {
        return (
            <Alert
                type="error"
                message="Oh snap! You got an error!"
                description="Unknown problem occurred."
            />
        )
    }

    if (props.fileResults.result.errors.length === 0) {
        return (
            <div>
                No errors found!
            </div>
        )
    }

    let errorMap: { [key: number]: CheckStyleError[]; } = {};
    for (const error of props.fileResults.result.errors) {
        // each line can have multiple errors
        if (errorMap.hasOwnProperty(error.lineNumber)) {
            errorMap[error.lineNumber].push(error);
        } else {
            errorMap[error.lineNumber] = [error];
        }
    }

    return (
        <div>

            <Row>
                <Alert
                    type="info"
                    message="Hover your cursor over the red lines with errors to see more information."
                />
            </Row>

            <Row>
                <Col span={16} offset={4}>
                <SyntaxHighlighter
                    className="col-md"
                    language="java"
                    style={tomorrow}
                    wrapLines
                    renderer={null}
                    astGenerator={null}
                    lineProps={
                        (lineNumber: number) => {
                            if (errorMap.hasOwnProperty(lineNumber)) {
                                return {
                                    errors: errorMap[lineNumber],
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
                </Col>
            </Row>

        </div>
    )
}