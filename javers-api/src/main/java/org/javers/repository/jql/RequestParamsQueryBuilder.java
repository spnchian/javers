package org.javers.repository.jql;

import org.javers.common.reflection.ReflectionUtil;
import org.javers.core.commit.CommitId;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    public static final String PROPERTY_NAME = "propertyName";
    public static final String LIMIT = "limit";
    public static final String SKIP = "skip";
    public static final String AUTHOR = "author";
    public static final String COMMIT_PROPERTY = "commitProperty";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String COMMIT_ID = "commitId";
    public static final String VERSION = "version";
    public static final String CHILD_VALUE_OBJECTS = "childValueObjects";
    public static final String NEW_OBJECT_CHANGES = "newObjectChanges";

    @Override
    public JqlQuery apply(Map<String, String[]> parameters) {
        final QueryBuilder queryBuilder = appendObjectIdentifier(parameters);

        if (parameters.containsKey(PROPERTY_NAME)) {
            queryBuilder.andProperty(parameters.get(PROPERTY_NAME)[0]);
        }

        if (parameters.containsKey(LIMIT)) {
            queryBuilder.limit(Integer.valueOf(parameters.get(LIMIT)[0]));
        }

        if (parameters.containsKey(SKIP)) {
            queryBuilder.skip(Integer.valueOf(parameters.get(SKIP)[0]));
        }

        if (parameters.containsKey(AUTHOR)) {
            queryBuilder.byAuthor(parameters.get(AUTHOR)[0]);
        }

        if (parameters.containsKey(COMMIT_PROPERTY)) {
            Arrays.stream(parameters.get(COMMIT_PROPERTY))
                    .forEach(x -> {
                        //TODO check array
                        String[] split = x.split(";");
                        queryBuilder.withCommitProperty(split[0], split[1]);
                    });
        }

        if (parameters.containsKey(FROM)) {
            //TODO LocalDate
            LocalDateTime fromDateTime = Instant.ofEpochMilli(Long.parseLong(parameters.get(FROM)[0]))
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            queryBuilder.from(fromDateTime);
        }

        if (parameters.containsKey(TO)) {
            //TODO LocalDate
            LocalDateTime fromDateTime = Instant.ofEpochMilli(Long.parseLong(parameters.get(TO)[0]))
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            queryBuilder.to(fromDateTime);
        }

        if (parameters.containsKey(COMMIT_ID)) {
            Arrays.stream(parameters.get(COMMIT_ID))
                    .forEach(x -> queryBuilder.withCommitId(CommitId.valueOf(x)));
        }

        if (parameters.containsKey(VERSION)) {
            queryBuilder.withVersion(Long.valueOf(parameters.get(VERSION)[0]));
        }

        if (parameters.containsKey(CHILD_VALUE_OBJECTS)) {
            queryBuilder.withChildValueObjects();
        }

        if (parameters.containsKey(NEW_OBJECT_CHANGES)) {
            queryBuilder.withNewObjectChanges();
        }

        return queryBuilder.build();
    }

    private QueryBuilder appendObjectIdentifier(Map<String, String[]> parameters) {
        QueryBuilder queryBuilder;
        if (isQueryForEntity(parameters)) {
            queryBuilder = queryForEntity(parameters);
        } else if (isValueObjectIdQuery(parameters)) {
            queryBuilder = queryForValueObjectId(parameters);
        } else if (isValueObjectQuery(parameters)) {
            queryBuilder = queryForValueObject(parameters);
        } else if (isClassQuery(parameters)) {
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
