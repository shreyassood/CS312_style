import React from 'react';
import FileDrop from './components/FileDrop'
import StyleResult from "./components/StyleResult";
import Button from "react-bootstrap/Button";

import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

type Props = {}

type State = {
    uploadedDocument: boolean
    fileResults: string | null
}

export default class App extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            uploadedDocument: false,
            fileResults: null
        };

        // bind to access setState in callback
        this.acceptFileCallback = this.acceptFileCallback.bind(this);
        this.uploadNewFile = this.uploadNewFile.bind(this);
    }

    acceptFileCallback<T extends File>(files: T[]) {
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
            .then(data => {
                this.setState({
                    fileResults: JSON.stringify(data),
                    uploadedDocument: true
                });
            })
    }

    uploadNewFile() {
        this.setState({
            uploadedDocument: false,
        });
    }


    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <h1>CS312 Style Checker</h1>
                </header>

                {!this.state.uploadedDocument &&
                <FileDrop acceptFileCallback={this.acceptFileCallback}/>
                }

                {this.state.uploadedDocument &&
                <Button size="lg" onClick={this.uploadNewFile}>
                    Upload another file
                </Button>
                }

                {this.state.uploadedDocument &&
                <StyleResult fileResults={this.state.fileResults}/>
                }

            </div>
        );
    }

}
