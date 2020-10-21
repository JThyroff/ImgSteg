package de.thyroff.imgsteg.utils;

import java.util.Objects;

public class MyPosition {
    private short x, y;
    private Channel channel;
    private short offset;

    public MyPosition(short x, short y, Channel channel, short offset) {
        this.x = x;
        this.y = y;
        this.channel = channel;
        this.offset = offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyPosition)) return false;
        MyPosition that = (MyPosition) o;
        return x == that.x &&
                y == that.y &&
                offset == that.offset &&
                channel == that.channel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, channel, offset);
    }

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public short getOffset() {
        return offset;
    }

    public void setOffset(short offset) {
        this.offset = offset;
    }
}