document.getElementById('fileInput').addEventListener('change', function(event) {
    const file = event.target.files[0];
    if (!file) {
      console.log('No file selected.');
      return;
    }
  
    const reader = new FileReader();
    
    reader.onload = function(event) {
      const arrayBuffer = event.target.result;
      const byteArray = new Uint8Array(arrayBuffer);
      
      console.log('Byte array:', byteArray);
      // Now you can use the byteArray for further processing
    };
  
    reader.onerror = function(event) {
      console.error('File reading error:', event.target.error);
    };
  
    reader.readAsArrayBuffer(file);
  });
  