package org.javers.repository.jql

import org.javers.api.DummyEntity
import org.javers.api.DummyValueObject
import org.javers.core.Javers
import org.javers.core.JaversBuilder
import org.javers.core.diff.Change
import org.javers.core.metamodel.object.InstanceId
import org.javers.core.metamodel.object.ValueObjectId
import spock.lang.Shared
import spock.lang.Specification

class RequestParamsQueryBuilderTest extends Specification {

    @Shared
    def queryBuilder = new RequestParamsQueryBuilder()

    def "should create entity query"() {
        given:
        Javers javers = JaversBuilder.javers().build()
        def givenInstanceId = "someInstance"
        def className = DummyEntity.class.name
        Map<String, String[]> queryParams = asQueryParameters(["instanceId": givenInstanceId,
                                             "className": className])
        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(javers, jqlQuery)

        then:
        jqlQuery.isIdQuery()

        InstanceId instanceId = jqlQuery.idFilter
        instanceId.cdoId == givenInstanceId
        instanceId.typeName == className
    }

    def "should create value object id query"() {
        given:
        Javers javers = JaversBuilder.javers().build()
        String givenInstanceId = "someInstance"
        String className = DummyEntity.class.name
        String path = "dummyValueObject"
        Map<String, String[]> queryParams = asQueryParameters(["ownerLocalId": givenInstanceId,
                                                 "ownerEntityClass": className,
                                                 "path": path])
        when:
        JqlQuery jqlQuery = queryBuilder.apply(queryParams)
        compile(javers, jqlQuery)

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
            Javers javers = JaversBuilder.javers().build()
            String className = DummyEntity.class.name
            String path = "dummyValueObject"
            Map<String, String[]> queryParams = asQueryParameters(["ownerEntityClass": className,
                                                                   "path": path])
        when:
            JqlQuery jqlQuery = queryBuilder.apply(queryParams)
            compile(javers, jqlQuery)

        then:
            jqlQuery.isVoOwnerQuery()

            VoOwnerFilter voOwnerFilter = jqlQuery.voOwnerFilter
            voOwnerFilter.ownerEntity.name == className
            voOwnerFilter.path == path
    }

    def "should create query for class"() {
        given:
            Javers javers = JaversBuilder.javers().build()
            String entityClassName = DummyEntity.class.name
            String valueObjectClassName = DummyValueObject.class.name
            Map<String, String[]> queryParams = ["requiredClasses": asStringArray(entityClassName, valueObjectClassName)]
        when:
            JqlQuery jqlQuery = queryBuilder.apply(queryParams)
            compile(javers, jqlQuery)

        then:
            jqlQuery.isClassQuery()

            ClassFilter classFilter = jqlQuery.classFilter
            with(classFilter.managedTypes.collect { it -> it.name}) {
                it.size() == 2
                it.containsAll(entityClassName, valueObjectClassName)
            }
    }

    def "should create query for any domain object"() {
        given:
            Javers javers = JaversBuilder.javers().build()
            Map<String, String[]> queryParams = [:]
        when:
            JqlQuery jqlQuery = queryBuilder.apply(queryParams)
            compile(javers, jqlQuery)

        then:
            jqlQuery.isAnyDomainObjectQuery()
    }

    private List<Change> compile(Javers javers, JqlQuery jqlQuery) {
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
