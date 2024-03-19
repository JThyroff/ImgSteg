export function getImageDataFromImageFile(file) {
    return new Promise((resolve, reject) => {
        // Create a canvas element
        var canvas = document.createElement('canvas');
        var ctx = canvas.getContext('2d');

        // Create an image element
        var image = new Image();
        image.onload = () => {
            // Set canvas dimensions to match image dimensions
            canvas.width = image.width;
            canvas.height = image.height;

            // Draw the image onto the canvas
            ctx.drawImage(image, 0, 0);

            // Get ImageData from the canvas
            var imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
            resolve(imageData);
        };

        image.onerror = reject;

        // Read the file as a data URL and set it as the image source
        var reader = new FileReader();
        reader.onload = (e) => {
            image.src = e.target.result;
        };
        reader.onerror = reject;
        reader.readAsDataURL(file);
    });
}


function canvasToFile(canvas, fileName, mimeType = 'image/png', qualityArgument = 1.0) {
    return new Promise((resolve, reject) => {
        // Convert canvas content to a Blob
        canvas.toBlob((blob) => {
            if (!blob) {
                // No blob could be created, possibly due to an invalid state of the canvas
                reject(new Error('Canvas-to-Blob conversion failed'));
                return;
            }

            // Create a file from the blob
            const file = new File([blob], fileName, { type: mimeType, lastModified: Date.now() });
            resolve(file);
        }, mimeType, qualityArgument);
    });
}


export function createImageFileFromImageData(imageData, fileName) {
    // Create a new canvas element
    var canvas = document.createElement('canvas');
    var ctx = canvas.getContext('2d');

    // Set canvas dimensions to match ImageData dimensions
    canvas.width = imageData.width;
    canvas.height = imageData.height;

    // Put the ImageData onto the canvas
    ctx.putImageData(imageData, 0, 0);

    return canvasToFile(canvas, fileName);
}
