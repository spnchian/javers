package org.javers.api;

import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.RequestParamsQueryBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ChangesResponse changes(HttpServletRequest request) {
        Map<String, String[]> queryParameters = request.getParameterMap();
        JqlQuery jqlQuery = requestParamsQueryBuilder.apply(queryParameters);

        return new ChangesResponse(javersQueryService.findChanges(jqlQuery));
    }

    @GetMapping(path = "/shadows", produces = {MediaType.APPLICATION_JSON_VALUE, JaversMediaType.JAVERS_API_V1})
    @ResponseBody
    public ShadowsResponse<Object> shadows(HttpServletRequest request) {
        Map<String, String[]> queryParameters = request.getParameterMap();
        JqlQuery jqlQuery = requestParamsQueryBuilder.apply(queryParameters);

        return new ShadowsResponse(javersQueryService.findShadows(jqlQuery));
    }
}