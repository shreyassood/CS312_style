import React, {useMemo} from 'react';
import {DropEvent, useDropzone} from 'react-dropzone';

import './index.css';

const baseStyle = {
    flex: 1,
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    padding: '20px',
    borderWidth: 2,
    borderRadius: 2,
    borderColor: '#eeeeee',
    borderStyle: 'dashed',
    backgroundColor: '#fafafa',
    color: '#bdbdbd',
    outline: 'none',
    transition: 'border .24s ease-in-out'
};

const activeStyle = {
    borderColor: '#2196f3'
};

const rejectStyle = {
    borderColor: '#ff1744'
};

type Props = {
    acceptFileCallback?<T extends File>(files: T[], event: DropEvent): void;
    uploadingDocument: boolean;
}

export default function FileDrop(props: Props) {
    const {
        acceptedFiles,
        getRootProps,
        getInputProps,
        isDragActive,
        isDragReject,
    } = useDropzone({
        onDropAccepted: props.acceptFileCallback
    });

    const style = useMemo(() => ({
        ...baseStyle,
        ...(isDragActive ? activeStyle : {}),
        ...(isDragReject ? rejectStyle : {})
    }), [
        isDragActive,
        isDragReject
    ]) as React.CSSProperties;


    const files = acceptedFiles.map(file => (
        <span>{file.name}</span>
    ));


    return (
            <section className="container">
                {!props.uploadingDocument && <div className="dropzone" {...getRootProps({style})}>
                    <input {...getInputProps()} type="file" name="file"/>
                    <p>Drag 'n' drop some files here, or click to select files</p>
                </div>}
                {props.uploadingDocument &&
                <p>Loading result for {files} ... </p>
                }
            </section>
    );
}