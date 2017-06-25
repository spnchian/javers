package org.javers.repository.jql;

import org.javers.common.exception.JaversException;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;

import java.util.Map;

public class QueryBBBB {

    public static final String INSTANCE_ID = "instanceId";
    public static final String CLASS_NAME = "className";

    public static JqlQuery parametersToJqlQuery(Map<String, String[]> parameters) {
        QueryBuilder queryBuilder = null;
        if (isQueryForEntity(parameters)) {
            queryBuilder = queryForEntity(parameters);
        }

        return queryBuilder.build();
    }

    private static QueryBuilder queryForEntity(Map<String, String[]> parameters) {
        return QueryBuilder.byInstanceId(parameters.get(INSTANCE_ID)[0], quietlyClassForName(parameters.get(CLASS_NAME)[0]));
    }

    private static boolean isQueryForEntity(Map<String, String[]> parameters) {
        return parameters.containsKey(INSTANCE_ID) && parameters.containsKey(CLASS_NAME);
    }

    private static Class<?> quietlyClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JaversException(e);
        }
    }
}
