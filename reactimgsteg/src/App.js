import React from 'react';
import MyDropzone from './components/MyDropzone';
import MyToggle from './components/MyToggle';
import logo from './logo.svg';
import './App.css';

function App() {
  const handleDropInput = (acceptedFiles) => {
    console.log('Input Files:', acceptedFiles);
  };

  const handleDropSeed = (acceptedFiles) => {
    console.log('Seed Files:', acceptedFiles);
  };

  const handleDownload = () => {
    console.log('Download');
  };

  return (
    <div className="App">
      <header className="App-header">
        {/* Top-left grid cell */}
        <div className="grid-item">
          <MyDropzone onDrop={handleDropInput} accept="image/*" text="Drop input here. Must be of type *.png for 'Reveal' and can be arbitrary file for 'Hide'" />
        </div>

        {/* Top-right grid cell */}
        <div className="grid-item">
          <MyDropzone onDrop={handleDropSeed} accept="image/*" text="Drop seed image here. Type *.png" />
        </div>

        {/* Bottom-left grid cell */}
        <div className="grid-item">
          <MyToggle />
        </div>

        {/* Bottom-right grid cell */}
        <div className="grid-item">
          <button onClick={handleDownload}>Download</button>
        </div>

        {/* You might want to place the logo outside of the header or in its own grid cell */}
        <img src={logo} className="App-logo" alt="logo" />
      </header>
    </div>
  );
}

export default App;
