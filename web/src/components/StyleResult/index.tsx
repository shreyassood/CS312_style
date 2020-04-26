import React from 'react';
import Alert from "react-bootstrap/Alert";

type Props = {
    fileResults: APIResult | null,
}

export type APIResult = {
    result: CheckStyleResult | null
    error: string | null,
}

export type CheckStyleResult = {
    errors: CheckStyleError[],
    resultCode: bigint,
}

type CheckStyleError = {
    lineNumber: bigint,
    columnNumber: bigint,
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

    return (
        <div>
            {
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
            }

        </div>
    )
}