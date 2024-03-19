class BitBuffer {
    constructor() {
      this.buffer = [];
    }
  
    add(b) {
      this.buffer.push(!!b);
    }
  
    addNumber(toAdd, bits) {
      for (let d = 0; d < bits; d++) {
        // Put true for a 1 and false for 0
        this.buffer.push((toAdd & 1) === 1);
        toAdd >>= 1;
      }
    }
  
    addInt(toAdd) {
      this.addNumber(toAdd, 32);
    }
  
    addShort(toAdd) {
      this.addNumber(toAdd, 16);
    }
  
    addBytes(bytes) {
      bytes.forEach((b) => {
        for (let i = 0; i < 8; i++) {
          // Write every single bit into the buffer
          this.buffer.push(((b >> i) & 1) === 1);
        }
      });
    }
  
    // This function would need to be adjusted since JavaScript doesn't 
    // have direct access to image pixels without using canvas or similar API
    addFromImage(image, width, pixelIndex) {
      throw new Error('Not implemented: addFromImage requires specific implementation for JavaScript.');
    }
  
    addBooleanArray(booleanArray) {
      booleanArray.forEach(b => this.add(b));
    }
  
    isEmpty() {
      return this.buffer.length === 0;
    }
  
    size() {
      return this.buffer.length;
    }
  
    removeFirst() {
      return this.buffer.shift() || false;
    }
  
    removeInt() {
      if (this.size() < 32) {
        throw new Error("Buffer is not large enough to remove int");
      }
      let toReturn = 0;
      for (let i = 0; i < 32; i++) {
        if (this.removeFirst()) {
          toReturn |= (1 << i);
        }
      }
      return toReturn;
    }
  
    removeShort() {
      if (this.size() < 16) {
        throw new Error("Buffer is not large enough to remove short");
      }
      let toReturn = 0;
      for (let i = 0; i < 16; i++) {
        if (this.removeFirst()) {
          toReturn |= (1 << i);
        }
      }
      return toReturn;
    }
  
    removeByte() {
      if (this.size() < 8) {
        throw new Error("Buffer is not large enough to remove byte");
      }
      let toReturn = 0;
      for (let i = 0; i < 8; i++) {
        if (this.removeFirst()) {
          toReturn |= (1 << i);
        }
      }
      return toReturn;
    }
  
    removeBooleanArray(length) {
      if (this.size() < length) {
        throw new Error("Buffer is not large enough to remove " + length + " booleans");
      }
      let b = new Array(length);
      for (let i = 0; i < length; i++) {
        b[i] = this.removeFirst();
      }
      return b;
    }
  
    removeByteArray(length) {
      if (this.size() < length * 8) {
        throw new Error("Buffer is not large enough to remove " + length + " bytes");
      }
      let b = new Uint8Array(length);
      for (let i = 0; i < length; i++) {
        b[i] = this.removeByte();
      }
      return b;
    }
  
    static toBooleanArray(i) {
      let b = new Array(32);
      for (let d = 0; d < 32; d++) {
        b[d] = (i & 1) !== 0;
        i >>= 1;
      }
      return b;
    }
  }
  