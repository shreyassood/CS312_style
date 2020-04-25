import React from 'react';

type Props = {
    fileResults: string | null,
}

export default function StyleResult(props: Props) {
    return (
        <p>
            {props.fileResults}
        </p>
    )
}