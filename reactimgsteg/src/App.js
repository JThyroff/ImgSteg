import React, { useState } from 'react';
import MyDropzone from './components/MyDropzone';
import MyToggle from './components/MyToggle';
import logo from './logo.svg';
import rightarrow from './rightarrow.svg';
import './App.css';
import { imageToSeed } from './utils/ImageToSeed';
import { convertFileToUint8Array, convertUint8ArrayToFile } from './utils/FileByteArrayConverter';
import { DataEncryptor } from './utils/DataEncryptor';
import { ByteArrayToHex } from './utils/ByteArrayToHex';
import { triggerDownload } from './utils/TriggerDownload';
import { ByteArrayWriter } from './utils/ByteArrayWriter';
import { getImageDataFromImageFile, createImageFileFromImageData } from './utils/ImageToImageDataConverter';

function App() {
  // State to store the input and seed files
  const [imgInputFile, setImgInputFile] = useState(null);
  const [seedInputFile, setSeedInputFile] = useState(null);
  const [secretInputFile, setSecretInputFile] = useState(null);
  const [toggleState, setToggleState] = useState(false);

  const handleDropImage = (acceptedFiles) => {
    console.log('Input Files:', acceptedFiles);
    setImgInputFile(acceptedFiles[0]);
  };

  const handleDropSeed = (acceptedFiles) => {
    console.log('Seed Files:', acceptedFiles);
    setSeedInputFile(acceptedFiles[0]);
  };

  const handleDropSecret = (acceptedFiles) => {
    console.log('Secret Files: ', acceptedFiles);
    setSecretInputFile(acceptedFiles[0]);
  }



  const process = async () => {
    console.log('Process');

    if (!imgInputFile || !seedInputFile) {
      console.log('Missing files');
      return;
    }

    //TODO do checks if the files are matching the theoretical requirements and display error msgs


    if (toggleState) {
      console.log('Reveal');
      /*
      const decryptedData = await DataEncryptor.decryptData(encryptedData, seed);
      const decryptedFile = await convertUint8ArrayToFile(decryptedData, "decryptedFile_change_file_ending.aaa")

      triggerDownload(decryptedFile);*/
    } else {
      console.log('Hide');
      const seed = await imageToSeed(seedInputFile);
      console.log("Hash value of the seed image: " + ByteArrayToHex.bytesToHex(seed)); // This is the SHA-256 hash of the image

      //encrypt the secret file with the seed
      const byteArray = await convertFileToUint8Array(secretInputFile);
      const encryptedData = await DataEncryptor.encryptData(byteArray, seed);

      //write the resulting encrypted data into the input image
      getImageDataFromImageFile(imgInputFile).then(imageData => {
        // Use imageData here
        const imgDataNew = ByteArrayWriter.writeByteArrayIntoImage(encryptedData, imageData)
        createImageFileFromImageData(imgDataNew, "NewImage.png").then(newImage => {
          triggerDownload(newImage)
        }).catch(error => {
          console.error('Error processing image file:', error);
        });
      }).catch(error => {
        console.error('Error processing image file:', error);
      });

      //const newImgData = ByteArrayWriter.writeByteArrayIntoImage(encryptedData, imgData)

      //const newImage = createImageFileFromImageData(imgData, "NewImage.png")
      //triggerDownload(newImage)
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
          <MyDropzone onDrop={handleDropImage} accept="image/*" text="Drop input image here. Must be of type *.png." />
        </div>

        {/* Top-right grid cell */}
        <div className="grid-item">
          <MyDropzone onDrop={handleDropSeed} accept="image/*" text="Drop seed image here. Must be of type *.png." />
        </div>

        {/* Bottom-left grid cell */}
        <div className="grid-item">
          <MyDropzone onDrop={handleDropSecret} accept="image/*" text="Drop input file here. Can be an arbitrary type. Only necessary for 'Hide'." />
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
