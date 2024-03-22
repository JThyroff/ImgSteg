import { ByteArrayToHex } from "./ByteArrayToHex";
export class DataEncryptor {
    static async encryptData(data, seed) {
        // Import the key for encryption
        const key = await window.crypto.subtle.importKey(
            "raw",
            seed,
            { name: "AES-CBC", length: 256 },
            false,
            ["encrypt"]
        );

        // Generate an initialization vector
        const iv = window.crypto.getRandomValues(new Uint8Array(16));

        // Encrypt the data
        const encryptedData = await window.crypto.subtle.encrypt(
            { name: "AES-CBC", iv },
            key,
            data
        );

        // Combine the IV and the encrypted data
        const combinedData = new Uint8Array(iv.length + encryptedData.byteLength);
        combinedData.set(iv, 0);
        combinedData.set(new Uint8Array(encryptedData), iv.length);

        return combinedData;
    }

    static async decryptData(combinedData, seed) {
        const hashBuffer = await crypto.subtle.digest('SHA-256', combinedData)
        console.log("Combined Data Hash: " + ByteArrayToHex.bytesToHex(new Uint8Array(hashBuffer)));
        // Extract the IV and the encrypted data
        const iv = combinedData.slice(0, 16);
        const encryptedData = combinedData.slice(16);

        // Import the key for decryption
        const key = await window.crypto.subtle.importKey(
            "raw",
            seed,
            { name: "AES-CBC", length: 256 },
            false,
            ["decrypt"]
        );

        // Decrypt the data
        const decryptedData = await window.crypto.subtle.decrypt(
            { name: "AES-CBC", iv },
            key,
            encryptedData
        );

        return new Uint8Array(decryptedData);
    }
}


/*// Example usage
(async () => {
  const data = new TextEncoder().encode('Hello, world!');
  const seed = window.crypto.getRandomValues(new Uint8Array(32)); // AES-256 needs a 256-bit key
 
  const encryptedData = await DataEncryptor.encryptData(data, seed);
  console.log('Encrypted:', encryptedData);
 
  const decryptedData = await DataEncryptor.decryptData(encryptedData, seed);
  console.log('Decrypted:', new TextDecoder().decode(decryptedData));
})();*/
