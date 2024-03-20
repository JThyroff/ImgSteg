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
import { ByteArrayReader } from './utils/ByteArrayReader';
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

  async function hide(secretInputFile, imgInputFile, seed) {
    try {
      // Convert the file to a Uint8Array and wait for the operation to complete
      const byteArray = await convertFileToUint8Array(secretInputFile);

      // Encrypt the data and wait for the encryption to complete
      const encryptedData = await DataEncryptor.encryptData(byteArray, seed);

      // Get the image data from the image file and wait for the operation to complete
      const imageData = await getImageDataFromImageFile(imgInputFile);

      // Write the encrypted data into the image data
      const imgDataNew = ByteArrayWriter.writeByteArrayIntoImage(encryptedData, imageData);

      // Create a new image file from the modified image data and wait for the file to be created
      const newImage = await createImageFileFromImageData(imgDataNew, "img_containing_secret_data.png");

      // Trigger the download of the new image
      triggerDownload(newImage);
    } catch (error) {
      // If an error occurs in any of the above steps, log it
      console.error('Error:', error);
    }
  }

  async function reveal(imgInputFile, seed) {
    try {
      // Get the image data from the image file and wait for the operation to complete
      const imageData = await getImageDataFromImageFile(imgInputFile);

      // Extract the encrypted data from the image data
      const encryptedData = ByteArrayReader.readByteArrayFromImage(imageData);

      // Decrypt the data and wait for the decryption to complete
      const decryptedData = await DataEncryptor.decryptData(encryptedData, seed);

      // Convert the decrypted data to a file and wait for the operation to complete
      const decryptedFile = await convertUint8ArrayToFile(decryptedData, "decryptedFile_change_file_ending.aaa")

      // Trigger the download of the decrypted file
      triggerDownload(decryptedFile);
    } catch (error) {
      // If an error occurs in any of the above steps, log it
      console.error('Error:', error);
    }
  }


  const process = async () => {
    console.log('Process');

    if (!imgInputFile || !seedInputFile) {
      console.log('Missing files');
      return;
    }

    //TODO do checks if the files are matching the theoretical requirements and display error msgs

    const seed = await imageToSeed(seedInputFile);
    console.log("Hash value of the seed image: " + ByteArrayToHex.bytesToHex(seed)); // This is the SHA-256 hash of the image

    if (toggleState) {
      console.log('Reveal');
      reveal(imgInputFile, seed);
    } else {
      console.log('Hide');
      hide(secretInputFile, imgInputFile, seed);
    }
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
          <MyDropzone onDrop={handleDropImage} accept="image/png" text="Drop input image here. Must be of type *.png." />
        </div>

        {/* Top-right grid cell */}
        <div className="grid-item">
          <MyDropzone onDrop={handleDropSeed} accept="image/png" text="Drop seed image here. Must be of type *.png." />
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
