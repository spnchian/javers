package org.javers.api;

import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBBBB;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author pawel szymczyk
 */
@RestController
@RequestMapping("/javers")
public class JaversApiController {

    private final JaversQueryService javersQueryService;

    public JaversApiController(JaversQueryService javersQueryService) {
        this.javersQueryService = javersQueryService;
    }

    @GetMapping(path = "/v1/snapshots", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public SnapshotsResponse snapshots(HttpServletRequest request) {
        Map<String, String[]> queryParameters = request.getParameterMap();
        JqlQuery jqlQuery = QueryBBBB.parametersToJqlQuery(queryParameters);

        return new SnapshotsResponse(javersQueryService.findSnapshots(jqlQuery));
    }
}