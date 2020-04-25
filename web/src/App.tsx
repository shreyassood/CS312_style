import React from 'react';
import FileDrop from './components/FileDrop'
import StyleResult from "./components/StyleResult";

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
        this.acceptFileCallback = this.acceptFileCallback.bind(this)
    }

    acceptFileCallback<T extends File>(files: T[]){
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
                <StyleResult fileResults={this.state.fileResults}/>
                }

            </div>
        );
    }

}
