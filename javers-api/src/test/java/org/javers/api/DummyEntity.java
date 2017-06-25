package org.javers.api;

import org.javers.core.metamodel.annotation.Id;

/**
 * @author pawelszymczyk
 */
public class DummyEntity {

    @Id
    private final int id;
    private final DummyValueObject dummyValueObject;

    public DummyEntity(int id) {
        this.id = id;
        this.dummyValueObject = null;
    }

    public DummyEntity(int id, DummyValueObject dummyValueObject) {
        this.id = id;
        this.dummyValueObject = dummyValueObject;
    }

    @Id
    public int getId() {
        return id;
    }

    public DummyValueObject getDummyValueObject() {
        return dummyValueObject;
    }
}
