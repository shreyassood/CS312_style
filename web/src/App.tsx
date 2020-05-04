import React from 'react';
import FileDrop from './components/FileDrop'
import StyleResult, {APIResult} from "./components/StyleResult";
import Button from "react-bootstrap/Button";

import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Navbar from "react-bootstrap/Navbar";

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
                <Button size="lg" onClick={this.uploadNewFile}>
                    Upload another file
                </Button>
        }

        return (
            <div className="App">
                <Navbar bg="dark" variant="dark" className="header">
                    <Navbar.Brand>
                        <img
                            alt=""
                            src="/logo192.png"
                            width="30"
                            height="30"
                            className="d-inline-block align-top"
                        />{' '}
                        CS312 Style Checker
                    </Navbar.Brand>
                </Navbar>

                <div className="container upload-container">

                    <div className="row justify-content-center">
                        {fileUpload}
                    </div>

                </div>

                {this.state.uploadedDocument &&
                <StyleResult fileResults={this.state.fileResults}/>
                }

            </div>
        );
    }

}
