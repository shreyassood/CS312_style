import React from 'react';
import HelloWorld from './components/HelloWorld'
import FileDrop from './components/FileDrop'

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
                    <HelloWorld/>
                </header>

                {!this.state.uploadedDocument &&
                <FileDrop acceptFileCallback={this.acceptFileCallback}/>
                }

                {this.state.uploadedDocument &&
                <p>
                    {this.state.fileResults}
                </p>
                }

            </div>
        );
    }

}
