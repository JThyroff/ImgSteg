export async function imageToSeed(file) {
  // Convert the file directly to an ArrayBuffer
  const arrayBuffer = await fileToArrayBuffer(file);

  // Calculate the SHA-256 hash of the raw image data
  const hash = await hashArrayBuffer(arrayBuffer);

  return hash;
}

function fileToArrayBuffer(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = (event) => {
      resolve(event.target.result);
    };
    reader.onerror = (event) => {
      reject(event.target.error);
    };
    reader.readAsArrayBuffer(file);
  });
}

async function hashArrayBuffer(arrayBuffer) {
  const hashBuffer = await crypto.subtle.digest('SHA-256', arrayBuffer);
  return new Uint8Array(hashBuffer);
}
