const Channel = {
    ALPHA: { value: 0 },
    RED: { value: 1 },
    GREEN: { value: 2 },
    BLUE: { value: 3 },

    toChannel: function (c) {
        switch (c) {
            case 0:
                return this.ALPHA;
            case 1:
                return this.RED;
            case 2:
                return this.GREEN;
            case 3:
                return this.BLUE;
            default:
                return null;
        }
    },

    toChannelFromBoolean: function (b) {
        if (b.length !== 2) {
            throw new Error("Array has to have length 2");
        }

        if (!b[0] && !b[1]) {
            return this.ALPHA;
        } else if (!b[0]) {
            return this.RED;
        } else if (!b[1]) {
            return this.GREEN;
        } else {
            return this.BLUE;
        }
    },

    toInt: function () {
        return this.value;
    },

    toBoolean: function () {
        const b = [false, false];
        switch (this.value) {
            case 0: // ALPHA
                break;
            case 1: // RED
                b[1] = true;
                break;
            case 2: // GREEN
                b[0] = true;
                break;
            case 3: // BLUE
                b[0] = true;
                b[1] = true;
                break;
        }
        return b;
    }
};
