package de.thyroff.imgsteg;

import java.util.Objects;

public class MyClass {
    int x, y;
    Channel c;
    int offset;

    public MyClass(int x, int y, Channel c, int offset) {
        this.x = x;
        this.y = y;
        this.c = c;
        this.offset = offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyClass)) return false;
        MyClass myClass = (MyClass) o;
        return x == myClass.x &&
                y == myClass.y &&
                offset == myClass.offset &&
                c == myClass.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, c, offset);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Channel getC() {
        return c;
    }

    public void setC(Channel c) {
        this.c = c;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
