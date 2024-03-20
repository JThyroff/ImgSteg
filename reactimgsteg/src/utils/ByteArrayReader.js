import { BitBuffer } from "./BitBuffer";
export class ByteArrayReader {

  /**
   * Reads a byte stream from the provided image data. It simply takes the lowest bits of RGB color channels.
   * Assumes the image is already loaded and rendered onto a canvas.
   * @param {ImageData} imageData The ImageData object from which the byte stream will be extracted.
   * @returns {Uint8Array} The extracted byte array.
   */
  static readByteArrayFromImage(imageData) {
    console.log("read byte stream from image");

    const width = imageData.width;
    let pixelIndex = 0;

    const bitBuffer = new BitBuffer();

    // Determine buffer length - read first 33 bits: length of buffer array
    // Assuming that the image is large enough to hold at least 33 bits
    for (let i = 0; i < 11; i++) {
      this.addPixelDataToBitBuffer(imageData, width, pixelIndex, bitBuffer);
      pixelIndex++;
    }

    const bufferSize = bitBuffer.removeInt(); // Positions to be read - i.e., size of the following buffer

    if (bitBuffer.size() !== 1) {
      throw new Error('Buffer size mismatch.');
    }

    // Now read the rest of the bytes
    while (bitBuffer.size() < bufferSize * 8) {
      this.addPixelDataToBitBuffer(imageData, width, pixelIndex, bitBuffer);
      pixelIndex++;
    }

    console.log("Byte stream read.");

    // Convert bitBuffer to byte array
    return bitBuffer.removeByteArray(bufferSize);
  }

  /**
   * Adds pixel data to the BitBuffer object.
   * @param {ImageData} imageData The image data.
   * @param {number} width The width of the image.
   * @param {number} pixelIndex The current pixel index.
   * @param {BitBuffer} bitBuffer The BitBuffer instance.
   */
  static addPixelDataToBitBuffer(imageData, width, pixelIndex, bitBuffer) {
    const x = pixelIndex % width;
    const y = Math.floor(pixelIndex / width);
    const i = (y * width + x) * 4; // pixel index in the imageData array

    const r = imageData.data[i];
    const g = imageData.data[i + 1];
    const b = imageData.data[i + 2];

    bitBuffer.add(r & 1); // Add the least significant bit of the red channel
    bitBuffer.add(g & 1); // Add the least significant bit of the green channel
    bitBuffer.add(b & 1); // Add the least significant bit of the blue channel
  }
}
