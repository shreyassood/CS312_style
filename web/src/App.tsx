import React from 'react';
import FileDrop from './components/FileDrop'
import StyleResult, {APIResult} from "./components/StyleResult";
import {Affix, Button, PageHeader, Row, Col} from "antd";

import 'antd/dist/antd.css';
import './App.css';

type Props = {}

type State = {
    uploadedDocument: boolean
    uploadingDocument: boolean
    fileResults: APIResult | null
}


export default class App extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            uploadedDocument: false,
            uploadingDocument: false,
            fileResults: null
        };

        // bind to access setState in callback
        this.acceptFileCallback = this.acceptFileCallback.bind(this);
        this.uploadNewFile = this.uploadNewFile.bind(this);
    }

    acceptFileCallback<T extends File>(files: T[]) {

        this.setState({
            uploadingDocument: true
        });

        const formData = new FormData();
        formData.append('file', files[0]);

        fetch('/upload', {
            method: 'POST',
            // headers: new Headers({
            //     'Content-Type': 'multipart/form-data',
            // }),
            body: formData
        })
            .then(response => response.json())
            .then(
                (data) => {
                    this.setState({
                        fileResults: data,
                        uploadedDocument: true,
                        uploadingDocument: false,
                    });
                },
                (error) => {
                    this.setState({
                        fileResults: null,
                        uploadedDocument: true,
                        uploadingDocument: false,
                    });
                }
            )
    }

    uploadNewFile() {
        this.setState({
            uploadedDocument: false,
            uploadingDocument: false,
        });
    }


    render() {

        let fileUpload;
        if(!this.state.uploadedDocument) {
            fileUpload =
                <FileDrop
                    acceptFileCallback={this.acceptFileCallback}
                    uploadingDocument={this.state.uploadingDocument}
                />
        } else {
            fileUpload =
                <Button size="large" onClick={this.uploadNewFile}>
                    Upload another file
                </Button>
        }

        return (
            <div className="App">
                <Affix>
                <PageHeader
                    ghost={false}
                    title="CS312 Style Checker"
                />
                </Affix>

                <div className="upload-container">

                    <Row>
                        {fileUpload}
                    </Row>

                </div>

                {this.state.uploadedDocument &&
                <StyleResult fileResults={this.state.fileResults}/>
                }

            </div>
        );
    }

}
