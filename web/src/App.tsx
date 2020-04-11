import React from 'react';
import HelloWorld from './components/HelloWorld'
import FileDrop from './components/FileDrop'

export default function App() {
  return (
    <div className="App">
      <header className="App-header">
        <HelloWorld />
      </header>

        <form method="POST" action="/upload" encType="multipart/form-data">
            <FileDrop />
            <input type="submit" value="Submit"/>
        </form>


    </div>);
}
