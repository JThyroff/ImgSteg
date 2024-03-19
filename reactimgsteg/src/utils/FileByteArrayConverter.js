export const convertFileToUint8Array = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(reader.result);
    reader.onerror = reject;
    new Uint8Array(reader.readAsArrayBuffer(file));
  });
};

export const convertUint8ArrayToFile = (uint8Array, filename, mimeType = 'application/octet-stream') => {
  const blob = new Blob([uint8Array], { type: mimeType });
  const file = new File([blob], filename, { type: mimeType, lastModified: new Date() });
  return file;
};

