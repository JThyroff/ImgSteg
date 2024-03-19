export const ARGB = {
    getRed: function (argb) {
        return (argb >> 16) & 0xff;
    },

    getBlue: function (argb) {
        return argb & 0xff;
    },

    getGreen: function (argb) {
        return (argb >> 8) & 0xff;
    },

    getAlpha: function (argb) {
        return (argb >> 24) & 0xff;
    },

    toARGB: function (alpha, red, green, blue) {
        // Assuming the case when alpha is not provided
        if (blue === undefined) {
            blue = green;
            green = red;
            red = alpha;
            alpha = 255;
        }
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    },

    /*inject3: function (argb, bits) {
        if (bits.length !== 3) {
            throw new Error("Length has to be 3. Actual Length: " + bits.length);
        }
        return this.inject3Bits(argb, bits[0], bits[1], bits[2]);
    },*/

    inject3Bits: function (argb, bit1, bit2, bit3) {
        let mask;

        // Red
        mask = 1 << 16;
        argb = bit1 ? (argb | mask) : (argb & ~mask);

        // Green
        mask = 1 << 8;
        argb = bit2 ? (argb | mask) : (argb & ~mask);

        // Blue
        mask = 1;
        argb = bit3 ? (argb | mask) : (argb & ~mask);

        return argb;
    }
};
