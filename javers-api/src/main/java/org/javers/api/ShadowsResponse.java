package org.javers.api;

import org.javers.shadow.Shadow;

import java.util.List;

/**
 * @author pawel szymczyk
 */
public class ShadowsResponse<T> extends JaversResponse<Shadow<T>> {
    public ShadowsResponse(List<Shadow<T>> entries) {
        super(entries);
    }
}
