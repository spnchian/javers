package org.javers.repository.jql

import org.javers.api.DummyEntity
import org.javers.core.Javers
import org.javers.core.JaversBuilder
import org.javers.core.diff.Change
import org.javers.core.metamodel.object.InstanceId
import org.javers.core.metamodel.object.ValueObjectId
import spock.lang.Specification

class RequestParamsQueryBuilderTest extends Specification {

    def "should create entity query"() {
        given:
        Javers javers = JaversBuilder.javers().build()
        def givenInstanceId = "someInstance"
        def className = DummyEntity.class.name
        Map<String, String[]> queryParams = ["instanceId": asStringArray(givenInstanceId),
                                             "className": asStringArray(className)]
        when:
        JqlQuery jqlQuery = RequestParamsQueryBuilder.parametersToJqlQuery(queryParams)
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
        Map<String, String[]> queryParams = ["ownerLocalId": asStringArray(givenInstanceId),
                                                 "ownerEntityClass": asStringArray(className),
                                                 "path": asStringArray(path)]
        when:
        JqlQuery jqlQuery = RequestParamsQueryBuilder.parametersToJqlQuery(queryParams)
        compile(javers, jqlQuery)

        then:
        jqlQuery.isIdQuery()

        ValueObjectId valueObjectId = jqlQuery.idFilter
        valueObjectId.fragment == path
        InstanceId instanceId = valueObjectId.ownerId
        instanceId.cdoId == givenInstanceId
        instanceId.typeName == className
    }

    private List<Change> compile(Javers javers, JqlQuery jqlQuery) {
        javers.findChanges(jqlQuery)
    }

    private Map<String, String[]> asQueryParameters(Map<String, String> params) {
        Map<String, String[]> queryParams = 
    }

    private String[] asStringArray(String... s) {
        String[] strings = s
        strings
    }
}
