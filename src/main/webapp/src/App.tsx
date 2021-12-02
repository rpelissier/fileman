import React from 'react';
import logo from './logo.svg';
import './App.css';
import DirectoryForm from "./DirectoryForm";

function App() {
    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
            </header>
            <DirectoryForm></DirectoryForm>
        </div>
    );
}

export default App;
