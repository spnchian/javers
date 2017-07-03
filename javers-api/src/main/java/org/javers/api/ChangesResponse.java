package org.javers.api;

import org.javers.core.diff.Change;

import java.util.List;

/**
 * @author pawel szymczyk
 */
public class ChangesResponse extends JaversResponse<Change> {
    public ChangesResponse(List<Change> entries) {
        super(entries);
    }
}
