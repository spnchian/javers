package org.javers.api;


/**
 * @author pawel szymczyk
 */
public class DummyValueObject {

    private final int value;

    public DummyValueObject(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
