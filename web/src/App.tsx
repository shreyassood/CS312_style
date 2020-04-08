import React from 'react';
import HelloWorld from './components/HelloWorld'

export default function App() {
  return (
    <div className="App">
      <header className="App-header">
        <HelloWorld />
      </header>

        <form method="POST" action="/upload" encType="multipart/form-data">
            <input type="file" name="file"/><br/>
            <input type="submit" value="Submit"/>
        </form>
    </div>);
}
