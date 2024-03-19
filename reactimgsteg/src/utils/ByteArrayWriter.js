class ByteArrayWriter {
    /**
     * Injects a byte stream into an image's pixel data.
     * @param {Uint8Array} buffer - The byte stream to be injected.
     * @param {ImageData} imageData - The ImageData object of the image.
     */
    static writeByteArrayIntoImage(buffer, imageData) {
        console.log("write byte stream into image.");

        const width = imageData.width;
        const height = imageData.height;
        const pixelCount = width * height;

        // Check if the buffer fits into the image
        if (buffer.length * 8 > pixelCount * 3) {
            throw new Error("Buffer doesn't fit the image.");
        }

        // Write byte array into bit buffer
        const bitBuffer = new BitBuffer();
        // Write buffer length at the beginning (32 bits)
        bitBuffer.addInt(buffer.length);
        bitBuffer.addBytes(buffer);

        // Do some bit stuffing to fill to a multiple of 3 for simple write to pixels
        while (bitBuffer.size() % 3 !== 0) {
            bitBuffer.add(false);
        }

        // Injection
        let argb, x, y, index = 0;

        while (!bitBuffer.isEmpty()) {
            const bit1 = bitBuffer.removeFirst();
            const bit2 = bitBuffer.removeFirst();
            const bit3 = bitBuffer.removeFirst();

            x = index % width;
            y = Math.floor(index / width);

            argb = this.getARGBFromImageData(imageData, x, y);
            argb = ARGB.inject3(argb, bit1, bit2, bit3);
            this.setARGBIntoImageData(imageData, x, y, argb);

            index++;
        }

        console.log("Byte stream written into image.");
    }

    static getARGBFromImageData(imageData, x, y) {
        const index = (y * imageData.width + x) * 4;
        return (255 << 24) | (imageData.data[index] << 16) | (imageData.data[index + 1] << 8) | imageData.data[index + 2];
    }

    static setARGBIntoImageData(imageData, x, y, argb) {
        const index = (y * imageData.width + x) * 4;
        imageData.data[index] = ARGB.getRed(argb);
        imageData.data[index + 1] = ARGB.getGreen(argb);
        imageData.data[index + 2] = ARGB.getBlue(argb);
        // Alpha channel is not modified, assuming it's always 255 (opaque)
    }
}
