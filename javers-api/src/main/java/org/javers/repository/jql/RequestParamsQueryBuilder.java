package org.javers.repository.jql;

import org.javers.common.reflection.ReflectionUtil;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

//TODO
// - parameters.get(INSTANCE_ID)[0]
public class RequestParamsQueryBuilder implements Function<Map<String, String[]>, JqlQuery> {

    public static final String INSTANCE_ID = "instanceId";
    public static final String CLASS_NAME = "className";
    public static final String OWNER_LOCAL_ID = "ownerLocalId";
    public static final String OWNER_ENTITY_CLASS = "ownerEntityClass";
    public static final String PATH = "path";
    public static final String REQUIRED_CLASS = "requiredClasses";

    @Override
    public JqlQuery apply(Map<String, String[]> parameters) {
        QueryBuilder queryBuilder = null;
        queryBuilder = appendObjectIdentifier(parameters, queryBuilder);

        //TODO any domain object

        return queryBuilder.build();
    }

    private QueryBuilder appendObjectIdentifier(Map<String, String[]> parameters, QueryBuilder queryBuilder) {
        if (isQueryForEntity(parameters)) {
            queryBuilder = queryForEntity(parameters);
        } else if (isValueObjectIdQuery(parameters)) {
            queryBuilder = queryForValueObjectId(parameters);
        } else if (isValueObjectQuery(parameters)) {
            queryBuilder = queryForValueObject(parameters);
        }else if (isClassQuery(parameters)) {
            queryBuilder = queryForClass(parameters);
        } else {
            queryBuilder = QueryBuilder.anyDomainObject();
        }

        return queryBuilder;
    }

    private QueryBuilder queryForClass(Map<String, String[]> parameters) {
        Set<Class> p = Arrays.stream(parameters.get(REQUIRED_CLASS))
                .map(className -> ReflectionUtil.classForName(className))
                .collect(Collectors.toSet());

        return QueryBuilder.byClass(p);
    }

    private boolean isClassQuery(Map<String, String[]> parameters) {
        return parameters.containsKey(REQUIRED_CLASS);
    }

    private boolean isQueryForEntity(Map<String, String[]> parameters) {
        return parameters.containsKey(INSTANCE_ID) && parameters.containsKey(CLASS_NAME);
    }

    private QueryBuilder queryForEntity(Map<String, String[]> parameters) {
        return QueryBuilder.byInstanceId(parameters.get(INSTANCE_ID)[0], ReflectionUtil.classForName(parameters.get(CLASS_NAME)[0]));
    }

    private boolean isValueObjectIdQuery(Map<String, String[]> parameters) {
        return parameters.containsKey(OWNER_LOCAL_ID) && parameters.containsKey(OWNER_ENTITY_CLASS) && parameters.containsKey(PATH);
    }

    private QueryBuilder queryForValueObjectId(Map<String, String[]> parameters) {
        return QueryBuilder.byValueObjectId(parameters.get(OWNER_LOCAL_ID)[0],
                ReflectionUtil.classForName(parameters.get(OWNER_ENTITY_CLASS)[0]),
                parameters.get(PATH)[0]);
    }

    private boolean isValueObjectQuery(Map<String, String[]> parameters) {
        return parameters.containsKey(OWNER_ENTITY_CLASS) && parameters.containsKey(PATH);
    }

    private static QueryBuilder queryForValueObject(Map<String, String[]> parameters) {
        return QueryBuilder.byValueObject(ReflectionUtil.classForName(parameters.get(OWNER_ENTITY_CLASS)[0]), parameters.get(PATH)[0]);
    }
}
