package org.javers.core.diff.appenders;

import org.javers.core.JaversCoreConfiguration;
import org.javers.core.diff.Change;
import org.javers.core.diff.NodePair;
import org.javers.core.diff.changetype.PropertyChange;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.javers.core.diff.custom.CustomPropertyComparator;
import org.javers.core.metamodel.object.GlobalId;
import org.javers.core.metamodel.type.JaversProperty;
import org.javers.core.metamodel.type.JaversType;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by schiang on 7/17/17.
 */
public class PathBasedAppender extends CorePropertyChangeAppender<PropertyChange> {
    /**
     * Checks if given property type is supported
     *
     * @param propertyType
     */
    @Override
    public boolean supports(JaversType propertyType) {
        return false;
    }

    public boolean supports(JaversProperty property, NodePair pair, JaversCoreConfiguration core){
        HashMap<String,CustomPropertyComparator> pathToComparator = core.getPathTocomparator();

        GlobalId temp = pair.getGlobalId();
        String path = temp.toString() + property.getName();
        Set<String> keys = pathToComparator.keySet().stream().filter(pattern -> Pattern.compile(pattern).matcher(path).find()).collect(Collectors.toSet());

        return !keys.isEmpty();
//        return pathToComparator.containsKey(pair.getLeftGlobalId(propertyType).toString());
//        return false;
    }

    @Override
    public PropertyChange calculateChanges(NodePair pair, JaversProperty supportedProperty) {

        return null;
    }

    public PropertyChange calculateChanges(NodePair pair, JaversProperty supportedProperty, JaversCoreConfiguration core){
        HashMap<String,CustomPropertyComparator> pathToComparator = core.getPathTocomparator();
        GlobalId temp = pair.getGlobalId();
        String path = temp.toString() + supportedProperty.getName();
        Set<String> keys = pathToComparator.keySet().stream().filter(pattern -> Pattern.compile(pattern).matcher(path).find()).collect(Collectors.toSet());

        String correctPath = "";
        for(String paths : keys){
            if(paths.length() > correctPath.length()){
                correctPath = paths;
            }
        }
        CustomPropertyComparator correctComparator = pathToComparator.get(correctPath);
        return correctComparator.compare(pair.getLeftPropertyValue(supportedProperty),pair.getRightPropertyValue(supportedProperty),pair.getGlobalId(),supportedProperty);
    }

    @Override
    public int priority(){
        return CorePropertyChangeAppender.HIGH_PRIORITY;
    }
}
