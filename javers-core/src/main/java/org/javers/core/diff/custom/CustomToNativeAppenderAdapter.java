package org.javers.core.diff.custom;

import domain.EverythingComparator;
import org.javers.core.diff.NodePair;
import org.javers.core.diff.appenders.PropertyChangeAppender;
import org.javers.core.diff.changetype.PropertyChange;
import org.javers.core.metamodel.type.JaversProperty;
import org.javers.core.metamodel.type.JaversType;

/**
 * @author bartosz walacik
 */
public class CustomToNativeAppenderAdapter<T, C extends PropertyChange> implements PropertyChangeAppender<C> {
    private final CustomPropertyComparator<T, C> delegate;
    private final Class<T> propertyJavaClass;

    public CustomToNativeAppenderAdapter(CustomPropertyComparator<T, C> delegate, Class<T> propertyJavaClass) {
        this.delegate = delegate;
        this.propertyJavaClass = propertyJavaClass;
    }

    @Override
    public boolean supports(JaversType propertyType) {
        return propertyType.getBaseJavaClass().equals(propertyJavaClass);
    }

    public boolean supports(JaversType propertyType,NodePair pair){
        if (this.delegate instanceof EverythingComparator){
            ((EverythingComparator) this.delegate).hasPath(pair.getGlobalId().toString());
        }
        return false;
    }

    @Override
    public C calculateChanges(NodePair pair, JaversProperty property) {
        T leftValue = (T)pair.getLeftPropertyValue(property);
        T rightValue = (T)pair.getRightPropertyValue(property);

        return delegate.compare(leftValue, rightValue, pair.getGlobalId(), property);
    }


    @Override
    public int priority() {
        return HIGH_PRIORITY;
    }
}
