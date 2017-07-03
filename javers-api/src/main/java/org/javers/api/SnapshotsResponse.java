package org.javers.api;

import org.javers.core.metamodel.object.CdoSnapshot;

import java.util.List;

/**
 * @author pawel szymczyk
 */
public class SnapshotsResponse extends JaversResponse<CdoSnapshot> {

    public SnapshotsResponse(List<CdoSnapshot> entries) {
        super(entries);
    }
}
