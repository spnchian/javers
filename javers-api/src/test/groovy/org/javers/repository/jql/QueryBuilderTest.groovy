package org.javers.repository.jql

import org.javers.api.DummyEntity
import org.javers.core.Javers
import org.javers.core.JaversBuilder
import spock.lang.Specification

class QueryBuilderTest extends Specification {

    def "should create entity query"() {
        given:
        Javers javers = JaversBuilder.javers().build()
        String[] instanceId = ["someInstance"]
        String[] className = [DummyEntity.class.name]
        Map<String, String[]> queryParams = ["instanceId": instanceId,
                                             "className": className]
        when:
        JqlQuery jqlQuery = QueryBBBB.parametersToJqlQuery(queryParams)
        javers.fin

        then:
        jqlQuery.isIdQuery()
    }
}
