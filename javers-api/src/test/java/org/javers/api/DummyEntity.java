package org.javers.api;

import org.javers.core.metamodel.annotation.Id;

/**
 * @author pawelszymczyk
 */
public class DummyEntity {

    @Id
    private int id;
    private String name;
    private DummyValueObject dummyValueObject;

    private DummyEntity() {

    }

    public DummyEntity(int id, String name, DummyValueObject dummyValueObject) {
        this.id = id;
        this.name = name;
        this.dummyValueObject = dummyValueObject;
    }

    @Id
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DummyValueObject getDummyValueObject() {
        return dummyValueObject;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDummyValueObject(DummyValueObject dummyValueObject) {
        this.dummyValueObject = dummyValueObject;
    }
}
