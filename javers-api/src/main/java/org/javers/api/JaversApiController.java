package org.javers.api;

import org.javers.core.diff.Change;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.RequestParamsQueryBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author pawel szymczyk
 */
@RestController
@RequestMapping("/javers")
class JaversApiController {

    private static final RequestParamsQueryBuilder requestParamsQueryBuilder =
            new RequestParamsQueryBuilder();

    private final JaversQueryService javersQueryService;

    public JaversApiController(JaversQueryService javersQueryService) {
        this.javersQueryService = javersQueryService;
    }

    @GetMapping(path = "/snapshots", produces = {MediaType.APPLICATION_JSON_VALUE, JaversMediaType.JAVERS_API_V1})
    @ResponseBody
    public SnapshotsResponse snapshots(HttpServletRequest request) {
        Map<String, String[]> queryParameters = request.getParameterMap();
        JqlQuery jqlQuery = requestParamsQueryBuilder.apply(queryParameters);

        return new SnapshotsResponse(javersQueryService.findSnapshots(jqlQuery));
    }

    @GetMapping(path = "/changes", produces = {MediaType.APPLICATION_JSON_VALUE, JaversMediaType.JAVERS_API_V1})
    @ResponseBody
    public JaversResponse<Change> changes(HttpServletRequest request) {
        Map<String, String[]> queryParameters = request.getParameterMap();
        JqlQuery jqlQuery = requestParamsQueryBuilder.apply(queryParameters);

        return new ChangesResponse(javersQueryService.findChanges(jqlQuery));
    }
}