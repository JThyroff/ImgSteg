async function imageToSeed(file) {
    // Read and resize the image using a canvas
    const image = await readFileAsImage(file);
    const resizedImage = resizeAndGrayscaleImage(image, 100, 100);
  
    // Convert the canvas to a Blob and then to an ArrayBuffer
    const imageData = await canvasToBlob(resizedImage);
    const arrayBuffer = await blobToArrayBuffer(imageData);
  
    // Calculate the SHA-256 hash of the image data
    const hash = await hashArrayBuffer(arrayBuffer);
    
    return hash;
  }
  
  function readFileAsImage(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = (event) => {
        const img = new Image();
        img.onload = () => resolve(img);
        img.onerror = reject;
        img.src = event.target.result;
      };
      reader.onerror = reject;
      reader.readAsDataURL(file);
    });
  }
  
  function resizeAndGrayscaleImage(image, width, height) {
    const canvas = document.createElement('canvas');
    canvas.width = width;
    canvas.height = height;
    const ctx = canvas.getContext('2d');
    ctx.drawImage(image, 0, 0, width, height);
    return canvas;
  }
  
  function canvasToBlob(canvas) {
    return new Promise((resolve) => {
      canvas.toBlob(resolve, 'image/png');
    });
  }
  
  function blobToArrayBuffer(blob) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = (event) => {
        resolve(event.target.result);
      };
      reader.onerror = reject;
      reader.readAsArrayBuffer(blob);
    });
  }
  
  async function hashArrayBuffer(arrayBuffer) {
    const hashBuffer = await crypto.subtle.digest('SHA-256', arrayBuffer);
    return Array.from(new Uint8Array(hashBuffer)).map(b => b.toString(16).padStart(2, '0')).join('');
  }
  
  // Usage example: Handle an input file change event
  const handleFileChange = async (event) => {
    const file = event.target.files[0];
    if (file) {
      const hash = await imageToSeed(file);
      console.log(hash); // This is the SHA-256 hash of the image
    }
  };
  