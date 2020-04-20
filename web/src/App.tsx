import React from 'react';
import HelloWorld from './components/HelloWorld'
import FileDrop from './components/FileDrop'

type Props = {}

type State = {
    uploadedDocument: boolean
}

export default class App extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            uploadedDocument: false,
        }
    }


    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <HelloWorld/>
                </header>

                <FileDrop/>

            </div>
        );
    }

}
