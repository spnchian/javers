package org.javers.core;

import org.javers.common.validation.Validate;
import org.javers.core.diff.ListCompareAlgorithm;
import org.javers.core.diff.custom.CustomPropertyComparator;

import java.util.HashMap;

/**
 * @author bartosz walacik
 */
public class JaversCoreConfiguration {

    private MappingStyle mappingStyle = MappingStyle.FIELD;

    private ListCompareAlgorithm listCompareAlgorithm = ListCompareAlgorithm.SIMPLE;

    private boolean newObjectsSnapshot = false;

    private CommitIdGenerator commitIdGenerator = CommitIdGenerator.SYNCHRONIZED_SEQUENCE;

    private HashMap<String,CustomPropertyComparator> pathTocomparator;

    JaversCoreConfiguration withMappingStyle(MappingStyle mappingStyle) {
        Validate.argumentIsNotNull(mappingStyle);
        this.mappingStyle = mappingStyle;
        return this;
    }

    JaversCoreConfiguration withCommitIdGenerator(CommitIdGenerator commitIdGenerator) {
        Validate.argumentIsNotNull(commitIdGenerator);
        this.commitIdGenerator = commitIdGenerator;
        return this;
    }

    JaversCoreConfiguration withNewObjectsSnapshot(boolean newObjectsSnapshot) {
        this.newObjectsSnapshot = newObjectsSnapshot;
        return this;
    }

    JaversCoreConfiguration withListCompareAlgorithm(ListCompareAlgorithm algorithm) {
        this.listCompareAlgorithm = algorithm;
        return this;
    }

    public MappingStyle getMappingStyle() {
        return mappingStyle;
    }

    public ListCompareAlgorithm getListCompareAlgorithm() {
        return listCompareAlgorithm;
    }

    public boolean isNewObjectsSnapshot() {
        return newObjectsSnapshot;
    }

    public CommitIdGenerator getCommitIdGenerator() {
        return commitIdGenerator;
    }

    public HashMap<String,CustomPropertyComparator> getPathTocomparator() {
        if(pathTocomparator == null){
            pathTocomparator = new HashMap<>();
        }
        return pathTocomparator;
    }

    public void addPathToComparator(String path, CustomPropertyComparator comparator) {
        if(pathTocomparator == null){
            pathTocomparator = new HashMap<>();
        }
        pathTocomparator.put(path, comparator);
    }
}
