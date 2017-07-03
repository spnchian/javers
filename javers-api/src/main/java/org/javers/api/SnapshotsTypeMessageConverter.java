package org.javers.api;

import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;

/**
 * @author pawel szymczyk
 */
public class SnapshotsTypeMessageConverter extends AbstractJaversTypesMessageConverter<SnapshotsResponse> {

    SnapshotsTypeMessageConverter(Javers javers) {
        super(javers);
    }
}
