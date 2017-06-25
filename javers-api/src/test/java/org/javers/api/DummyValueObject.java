package org.javers.api;


public class DummyValueObject {

    private final int value;

    public DummyValueObject(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
