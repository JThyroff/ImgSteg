import React, { useState } from 'react';
import MyDropzone from './components/MyDropzone';
import MyToggle from './components/MyToggle';
import logo from './logo.svg';
import rightarrow from './rightarrow.svg';
import './App.css';
import { imageToSeed } from './utils/ImageToSeed';


function App() {
  // State to store the input and seed files
  const [inputFile, setInputFile] = useState(null);
  const [seedFile, setSeedFile] = useState(null);
  const [toggleState, setToggleState] = useState(false);

  const handleDropInput = (acceptedFiles) => {
    console.log('Input Files:', acceptedFiles);
    setInputFile(acceptedFiles[0]);
  };

  const handleDropSeed = (acceptedFiles) => {
    console.log('Seed Files:', acceptedFiles);
    setSeedFile(acceptedFiles[0]);
  };

  const handleDownload = () => {
    console.log('Download');
  };

  const process = async () => {
    console.log('Process');

    if (!inputFile || !seedFile) {
      console.log('Missing files');
      return;
    }

    //TODO do checks if the files are matching the theoretical requirements and display error msgs


    if (toggleState) {
      console.log('Reveal');
    } else {
      console.log('Hide');
      const hash = await imageToSeed(seedFile);
      console.log("Hash value of the seed image: " + hash); // This is the SHA-256 hash of the image
    }

    /*// Example processing logic: creating a new file to download
    const processedData = new Blob([`Processed content from ${inputFile.name} and ${seedFile.name}`], { type: 'text/plain' });
    const url = window.URL.createObjectURL(processedData);

    // Creating a temporary anchor element to trigger download
    const a = document.createElement('a');
    a.href = url;
    a.download = 'processed.txt'; // Name of the new file to be downloaded
    document.body.appendChild(a);
    a.click();

    // Cleanup
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);*/
  };

  const handleToggle = (state) => {
    setToggleState(state);
    console.log('Toggle State:', state);
  };

  return (
    <div className="App">
      <header className="App-header">
        {/* Top-left grid cell */}
        <div className="grid-item">
          <MyDropzone onDrop={handleDropInput} accept="image/*" text="Drop input image here. Must be of type *.png." />
        </div>

        {/* Top-right grid cell */}
        <div className="grid-item">
          <MyDropzone onDrop={handleDropSeed} accept="image/*" text="Drop seed image here. Must be of type *.png." />
        </div>

        {/* Bottom-left grid cell */}
        <div className="grid-item" style={{ border: '2px dashed black' }}>
          <MyDropzone onDrop={handleDropInput} accept="image/*" text="Drop input file here. Can be an arbitrary type. Only necessary for 'Hide'." />
        </div>

        {/* Bottom-right grid cell */}
        <div className="grid-item">
          <MyToggle onToggle={handleToggle} />
          {/* Arrow container */}
          <img src={rightarrow} className="rightarrow" alt="rightarrow" />

          {/* Process button */}
          <button className="process-button" onClick={process}>Process</button>
        </div>

        {/* You might want to place the logo outside of the header or in its own grid cell */}
        <img src={logo} className="App-logo" alt="logo" />
      </header >
    </div >
  );
}

export default App;
