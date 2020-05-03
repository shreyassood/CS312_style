import React, {useMemo} from 'react';
import {DropEvent, useDropzone} from 'react-dropzone';

import './index.css';
import { Spin } from "antd";

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
        // acceptedFiles,
        getRootProps,
        getInputProps,
        isDragActive,
        isDragReject,
    } = useDropzone({
        onDropAccepted: props.acceptFileCallback
    });

    const style = useMemo(() => ({
        ...(isDragActive ? activeStyle : {}),
        ...(isDragReject ? rejectStyle : {})
    }), [
        isDragActive,
        isDragReject
    ]) as React.CSSProperties;


    // const files = acceptedFiles.map(file => (
    //     <span>{file.name}</span>
    // ));

    if (!props.uploadingDocument) {
        return (
            <div className="dropzone" {...getRootProps({style})}>
                <input {...getInputProps()} type="file" name="file"/>
                <span>Drag 'n' drop some files here, or click to select files</span>
            </div>
        )
    }

    return (
        <Spin/>
    )

}