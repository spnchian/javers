package org.javers.repository.jql

import org.javers.api.*
import org.javers.core.Javers
import org.javers.core.diff.changetype.ValueChange
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Specification

@SpringBootTest(classes = [TestApplication], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class JaversApiStarterIntegrationTest extends Specification {

    @Autowired
    private DummyEntityRepository dummyEntityRepository

    @Autowired
    private TestRestTemplate restTemplate

    @Autowired
    private Javers javers

    void setup() {
        restTemplate.getRestTemplate().getMessageConverters().add(0, new SnapshotsTypeMessageConverter(javers))
    }

    def "should fetch snapshot for given entity"() {
        def id = 1
        given:
        DummyEntity dummyEntity = new DummyEntity(id, "name", null)

        dummyEntityRepository.save(dummyEntity)

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/javers/snapshots")
                .queryParam("instanceId", id)
                .queryParam("className", DummyEntity.class.getName())

        HttpHeaders headers = new HttpHeaders()
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE)

        when:
        ResponseEntity<SnapshotsResponse> response = restTemplate.exchange(builder.build().encode().toUri(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                SnapshotsResponse.class)

        then:
        response.getStatusCodeValue() == 200

        SnapshotsResponse snapshotsResponse = response.getBody()
        snapshotsResponse.entries.size() == 1
        snapshotsResponse.entries[0].getGlobalId().value() == "org.javers.api.DummyEntity/1"
        snapshotsResponse.entries[0].getPropertyValue("name")
    }

    def "should fetch changes for given entity"() {
        def id = 2
        given:
        def saved = dummyEntityRepository.save(new DummyEntity(id, "name_v1", null))
        saved.name = "name_v2"
        dummyEntityRepository.save(saved)

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/javers/changes")
                .queryParam("instanceId", id)
                .queryParam("className", DummyEntity.class.getName())

        HttpHeaders headers = new HttpHeaders()
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE)

        when:
        ResponseEntity<ChangesResponse> response = restTemplate.exchange(builder.build().encode().toUri(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                ChangesResponse.class)

        then:
        response.getStatusCodeValue() == 200

        ChangesResponse changesResponse = response.getBody()
        changesResponse.entries.size() == 1

        ValueChange valueChange = changesResponse.entries.first()
        valueChange.left == "name_v1"
        valueChange.right == "name_v2"
        valueChange.propertyName == "name"
    }

    def "should fetch shadows for given entity"() {
        def id = 3
        given:
        dummyEntityRepository.save(new DummyEntity(id, "name_v1", null))
        dummyEntityRepository.save(new DummyEntity(id, "name_v2", null))

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/javers/shadows")
                .queryParam("instanceId", id)
                .queryParam("className", DummyEntity.class.getName())

        HttpHeaders headers = new HttpHeaders()
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE)

        when:
        ResponseEntity<ShadowsResponse<DummyEntity>> response = restTemplate.exchange(builder.build().encode().toUri(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<ShadowsResponse>() {})

        then:
        response.getStatusCodeValue() == 200

        ShadowsResponse<ShadowsResponse<DummyEntity>> shadows = response.getBody()
        shadows.entries.size() == 2
        shadows.entries[0].get().name == "name_v2"
        shadows.entries[1].get().name == "name_v1"
    }
}