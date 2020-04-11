import React from 'react';

type Props = {}

type State = {
    isFetching: boolean
    text: string | null
}

export default class HelloWorld extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
        this.state = {
            isFetching: true,
            text: null
        }
    }

    componentDidMount() {
        this.setState({isFetching: true})
        fetch("/hello_world")
            .then(response => response.text())
            .then(text => this.setState({text: text, isFetching: false}))
    }

    render() {
        return (this.state.isFetching && <h1>Fetching text ...</h1>) || <h1>{this.state.text}</h1>
    }
}