package org.javers.repository.jql

import org.javers.api.*
import org.javers.core.Javers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Specification

@SpringBootTest(classes = [TestApplication], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class JaversMongoStarterIntegrationTest extends Specification {

    @Autowired
    private DummyEntityRepository dummyEntityRepository

    @Autowired
    private TestRestTemplate restTemplate

    @Autowired
    private Javers javers

    void setup() {
        restTemplate.getRestTemplate().getMessageConverters().add(0, new SnapshotResponseMessageConverter(javers))
    }

    def "should fetch snapshot for given entity"() {
        given:
        DummyEntity dummyEntity = new DummyEntity(1)

        dummyEntityRepository.save(dummyEntity)

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/javers/snapshots")
                .queryParam("instanceId", "1")
                .queryParam("className", DummyEntity.class.getName())

        HttpHeaders headers = new HttpHeaders()
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE)

        when:
        ResponseEntity<SnapshotsResponse> response = restTemplate.<SnapshotsResponse> exchange(builder.build().encode().toUri(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                SnapshotsResponse.class)

        then:
        response.getStatusCodeValue() == 200

        SnapshotsResponse snapshotsResponse = response.getBody()
        snapshotsResponse.entries.size() == 1

        with(snapshotsResponse.entries.first().commitMetadata) { it ->
            it.properties.containsKey("key")
            it.properties.containsValue("ok")
            it.author == "unauthenticated"
        }
    }
}