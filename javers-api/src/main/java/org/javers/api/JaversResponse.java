package org.javers.api;

import java.util.Collections;
import java.util.List;

/**
 * @author pawel szymczyk
 */
abstract class JaversResponse<T> {

    private List<T> entries;

    public JaversResponse() {
        this(Collections.emptyList());
    }

    public JaversResponse(List<T> entries) {
        this.entries = entries;
    }

    public List<T> getEntries() {
        return entries;
    }
}
