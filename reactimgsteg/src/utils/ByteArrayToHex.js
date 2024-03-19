class ByteArrayToHex {
    static bytesToHex(bytes) {
        return Array.from(bytes)
            .map((byte) => byte.toString(16).padStart(2, '0').toUpperCase())
            .join('');
    }
}
