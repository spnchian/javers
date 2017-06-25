package org.javers.api;

import org.javers.core.metamodel.object.CdoSnapshot;

import java.util.List;

/**
 * @author pawel szymczyk
 */
public class SnapshotsResponse {

    private List<CdoSnapshot> entries;

    public SnapshotsResponse(List<CdoSnapshot> entries) {
        this.entries = entries;
    }

    public List<CdoSnapshot> getEntries() {
        return entries;
    }
}
