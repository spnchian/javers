package domain;

import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.custom.CustomPropertyComparator;
import org.javers.core.metamodel.object.GlobalId;
import org.javers.core.metamodel.property.Property;

/**
 * Created by schiang on 7/14/17.
 */
public class PosessionComparator implements CustomPropertyComparator<Posession, ValueChange> {

    /**
     * @param left       left (or old) property value
     * @param right      right (or current) property value
     * @param affectedId Id of domain object being compared
     * @param property   property being compared
     * @return should return null if compared objects have no differences
     */
    @Override
    public ValueChange compare(Posession left, Posession right, GlobalId affectedId, Property property) {
        return null;
    }
}
