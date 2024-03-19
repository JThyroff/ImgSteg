export const convertFileToUint8Array = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();

    reader.onload = () => {
      // The file's binary data is stored in reader.result after readAsArrayBuffer completes
      const arrayBuffer = reader.result;
      const uint8Array = new Uint8Array(arrayBuffer);
      resolve(uint8Array);
    };

    reader.onerror = () => reject(reader.error);

    reader.readAsArrayBuffer(file);
  });
};


export const convertUint8ArrayToFile = (uint8Array, filename, mimeType = 'application/octet-stream') => {
  const blob = new Blob([uint8Array], { type: mimeType });
  const file = new File([blob], filename, { type: mimeType, lastModified: new Date() });
  return file;
};

