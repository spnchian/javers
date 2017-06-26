package org.javers.repository.jql;

import org.javers.common.exception.JaversException;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;

import java.util.Map;

//TODO
// - parameters.get(INSTANCE_ID)[0]
public class RequestParamsQueryBuilder {

    public static final String INSTANCE_ID = "instanceId";
    public static final String CLASS_NAME = "className";
    public static final String OWNER_LOCAL_ID = "ownerLocalId";
    public static final String OWNER_ENTITY_CLASS = "ownerEntityClass";
    public static final String PATH = "path";

    public static JqlQuery parametersToJqlQuery(Map<String, String[]> parameters) {
        QueryBuilder queryBuilder = null;
        if (isQueryForEntity(parameters)) {
            queryBuilder = queryForEntity(parameters);
        }

        if (isValueObjectIdQuery(parameters)) {
            queryBuilder = queryForValueObjectId(parameters);
        }

        return queryBuilder.build();
    }

    private static boolean isQueryForEntity(Map<String, String[]> parameters) {
        return parameters.containsKey(INSTANCE_ID) && parameters.containsKey(CLASS_NAME);
    }

    private static QueryBuilder queryForEntity(Map<String, String[]> parameters) {
        return QueryBuilder.byInstanceId(parameters.get(INSTANCE_ID)[0], quietlyClassForName(parameters.get(CLASS_NAME)[0]));
    }

    private static boolean isValueObjectIdQuery(Map<String, String[]> parameters) {
        return parameters.containsKey(OWNER_LOCAL_ID) && parameters.containsKey(OWNER_ENTITY_CLASS) && parameters.containsKey(PATH);
    }

    private static QueryBuilder queryForValueObjectId(Map<String, String[]> parameters) {
        return QueryBuilder.byValueObjectId(parameters.get(OWNER_LOCAL_ID)[0],
                quietlyClassForName(parameters.get(OWNER_ENTITY_CLASS)[0]),
                parameters.get(PATH)[0]);
    }

    private static Class<?> quietlyClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JaversException(e);
        }
    }
}
