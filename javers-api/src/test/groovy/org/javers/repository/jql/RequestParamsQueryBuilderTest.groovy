package org.javers.repository.jql

import org.javers.api.DummyEntity
import org.javers.api.DummyValueObject
import org.javers.core.Javers
import org.javers.core.JaversBuilder
import org.javers.core.commit.CommitId
import org.javers.core.diff.Change
import org.javers.core.metamodel.object.InstanceId
import org.javers.core.metamodel.object.ValueObjectId
import spock.lang.Shared
import spock.lang.Specification

import java.security.Timestamp

import static org.javers.repository.jql.RequestParamsQueryBuilder.*

class RequestParamsQueryBuilderTest extends Specification {

    @Shared
    def queryBuilder = new RequestParamsQueryBuilder()

    @Shared
    Javers javers = JaversBuilder.javers().build()

    def "should create entity query"() {
        given:
        def givenInstanceId = "someInstance"
        def className = DummyEntity.class.name
        Map<String, String[]> queryParams = asQueryParameters([(INSTANCE_ID): givenInstanceId,
                                                               (CLASS_NAME) : className])
        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.isIdQuery()

        InstanceId instanceId = jqlQuery.idFilter
        instanceId.cdoId == givenInstanceId
        instanceId.typeName == className
    }

    def "should create value object id query"() {
        given:
        String givenInstanceId = "someInstance"
        String className = DummyEntity.class.name
        String path = "dummyValueObject"
        Map<String, String[]> queryParams = asQueryParameters([(OWNER_LOCAL_ID)    : givenInstanceId,
                                                               (OWNER_ENTITY_CLASS): className,
                                                               (PATH)              : path])
        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.isIdQuery()

        ValueObjectId valueObjectId = jqlQuery.idFilter
        valueObjectId.fragment == path
        InstanceId instanceId = valueObjectId.ownerId
        instanceId.cdoId == givenInstanceId
        instanceId.typeName == className
    }

    def "should create value object query"() {
        given:
        String className = DummyEntity.class.name
        String path = "dummyValueObject"
        Map<String, String[]> queryParams = asQueryParameters([(OWNER_ENTITY_CLASS): className,
                                                               (PATH)              : path])
        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.isVoOwnerQuery()

        VoOwnerFilter voOwnerFilter = jqlQuery.voOwnerFilter
        voOwnerFilter.ownerEntity.name == className
        voOwnerFilter.path == path
    }

    def "should create query for class"() {
        given:
        String entityClassName = DummyEntity.class.name
        String valueObjectClassName = DummyValueObject.class.name
        Map<String, String[]> queryParams = [(REQUIRED_CLASS): asStringArray(entityClassName, valueObjectClassName)]
        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.isClassQuery()

        ClassFilter classFilter = jqlQuery.classFilter
        with(classFilter.managedTypes.collect { it -> it.name }) {
            it.size() == 2
            it.containsAll(entityClassName, valueObjectClassName)
        }
    }

    def "should create query for any domain object"() {
        given:
        Map<String, String[]> queryParams = [:]

        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.isAnyDomainObjectQuery()
    }

    def "should create propertyName filter"() {
        given:
        def propertyName = "name"
        Map<String, String[]> queryParams = asQueryParameters([(PROPERTY_NAME): propertyName])

        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.getChangedProperty() == propertyName
    }

    def "should create limit filter"() {
        given:
        def limit = 90
        Map<String, String[]> queryParams = asQueryParameters([(LIMIT): limit as String])

        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.getQueryParams().limit() == limit
    }

    def "should create skip filter"() {
        given:
        def skip = 19
        Map<String, String[]> queryParams = asQueryParameters([(SKIP): skip as String])

        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.getQueryParams().skip() == skip
    }

    def "should create author filter"() {
        given:
        def author = "kazik"
        Map<String, String[]> queryParams = asQueryParameters([(AUTHOR): author])

        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.getQueryParams().author().get() == author
    }

    def "should create commitProperty filter"() {
        given:
        def commitProperty = "customCommitProperty"
        def commitPropertyValue = "value123"
        def anotherCommitProperty = "anotherCustomCommitProperty"
        def anotherCommitPropertyValue = "value456"
        Map<String, String[]> queryParams = [(COMMIT_PROPERTY): asStringArray("$commitProperty;$commitPropertyValue"
                , "$anotherCommitProperty;$anotherCommitPropertyValue")]

        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.getQueryParams().commitProperties() == [(commitProperty): commitPropertyValue,
                                                         (anotherCommitProperty): anotherCommitPropertyValue]
    }

    def "should create from and to LocalDateTime filter"() {
        given:
        def fromTimestamp = 1499716946
        def toTimestamp = 1499716947
        Map<String, String[]> queryParams = asQueryParameters([(FROM): fromTimestamp as String,
                                                               (TO): toTimestamp as String])

        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        java.sql.Timestamp.valueOf(jqlQuery.getQueryParams().from().get()).getTime() == fromTimestamp
        java.sql.Timestamp.valueOf(jqlQuery.getQueryParams().to().get()).getTime() == toTimestamp
    }

    def "should create commitId filter"() {
        given:
        def oneCommitId = "1.0"
        def anotherCommitId = "1.1"
        Map<String, String[]> queryParams = [(COMMIT_ID): asStringArray(oneCommitId, anotherCommitId)]

        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.getQueryParams().commitIds() == [CommitId.valueOf(oneCommitId), CommitId.valueOf(anotherCommitId)] as Set
    }

    def "should create version filter"() {
        given:
        def version = 1L
        Map<String, String[]> queryParams = asQueryParameters([(VERSION): version as String])

        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.getQueryParams().version().get() == version
    }

    def "should create childValueObjects filter"() {
        given:
        Map<String, String[]> queryParams = asQueryParameters([(CHILD_VALUE_OBJECTS): true as String])

        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.getQueryParams().isAggregate() == true
    }

    def "should create newObjectChanges filter"() {
        given:
        Map<String, String[]> queryParams = asQueryParameters([(NEW_OBJECT_CHANGES): true as String])

        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(jqlQuery)

        then:
        jqlQuery.getQueryParams().newObjectChanges() == true
    }

    private List<Change> compile(JqlQuery jqlQuery) {
        javers.findChanges(jqlQuery)
    }

    private Map<String, String[]> asQueryParameters(Map<String, String> params) {
        Map<String, String[]> queryParams = params.collectEntries {
            key, value -> [(key): asStringArray(value)]
        }

        return queryParams
    }

    private String[] asStringArray(String... s) {
        String[] strings = s
        strings
    }
}
