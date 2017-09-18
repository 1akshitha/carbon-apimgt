package org.wso2.carbon.apimgt.rest.api.analytics.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.apimgt.core.api.Analyzer;
import org.wso2.carbon.apimgt.core.exception.APIManagementException;
import org.wso2.carbon.apimgt.core.models.analytics.ApplicationCount;
import org.wso2.carbon.apimgt.rest.api.analytics.ApplicationApiService;
import org.wso2.carbon.apimgt.rest.api.analytics.NotFoundException;
import org.wso2.carbon.apimgt.rest.api.analytics.dto.ApplicationCountListDTO;
import org.wso2.carbon.apimgt.rest.api.analytics.mappings.AnalyticsMappingUtil;
import org.wso2.carbon.apimgt.rest.api.common.dto.ErrorDTO;
import org.wso2.carbon.apimgt.rest.api.common.util.RestApiUtil;
import org.wso2.msf4j.Request;

import javax.ws.rs.core.Response;
import java.util.List;

public class ApplicationApiServiceImpl extends ApplicationApiService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationApiServiceImpl.class);

    @Override
    public Response applicationCountOverTimeGet(String startTime, String endTime, Request request) throws
            NotFoundException {

        String username = RestApiUtil.getLoggedInUsername(request);
        startTime = RestApiUtil.fromISO8601ToUTC(startTime);
        endTime = RestApiUtil.fromISO8601ToUTC(endTime);
        try {
            if (log.isDebugEnabled()) {
                log.debug("Retrieving applications created over time. " +
                        "From: " + startTime + " to: " + endTime);
            }
            Analyzer analyzer = RestApiUtil.getAnalyzer(username);
            List<ApplicationCount> applicationCountList = analyzer
                    .getApplicationCount(startTime, endTime);
            ApplicationCountListDTO applicationCountListDTO = AnalyticsMappingUtil
                    .fromApplicationCountToListDTO(applicationCountList);
            return Response.ok().entity(applicationCountListDTO).build();

        } catch (APIManagementException e) {
            String errorMessage = "Error while retrieving application created over time info";
            ErrorDTO errorDTO = RestApiUtil.getErrorDTO(e.getErrorHandler());
            log.error(errorMessage, e);
            return Response.status(e.getErrorHandler().getHttpStatusCode()).entity(errorDTO).build();
        }
    }
}
