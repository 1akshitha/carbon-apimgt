package org.wso2.carbon.apimgt.rest.api.store.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.apimgt.core.api.APIStore;
import org.wso2.carbon.apimgt.core.exception.APIManagementException;
import org.wso2.carbon.apimgt.core.exception.APIMgtResourceNotFoundException;
import org.wso2.carbon.apimgt.core.exception.ErrorHandler;
import org.wso2.carbon.apimgt.core.exception.ExceptionCodes;
import org.wso2.carbon.apimgt.core.models.API;
import org.wso2.carbon.apimgt.core.models.Application;
import org.wso2.carbon.apimgt.core.models.Subscription;
import org.wso2.carbon.apimgt.core.util.APIMgtConstants;
import org.wso2.carbon.apimgt.rest.api.common.RestApiConstants;
import org.wso2.carbon.apimgt.rest.api.common.dto.ErrorDTO;
import org.wso2.carbon.apimgt.rest.api.common.util.RestApiUtil;
import org.wso2.carbon.apimgt.rest.api.store.ApiResponseMessage;
import org.wso2.carbon.apimgt.rest.api.store.NotFoundException;
import org.wso2.carbon.apimgt.rest.api.store.SubscriptionsApiService;
import org.wso2.carbon.apimgt.rest.api.store.dto.SubscriptionDTO;
import org.wso2.carbon.apimgt.rest.api.store.dto.SubscriptionListDTO;
import org.wso2.carbon.apimgt.rest.api.store.mappings.SubscriptionMappingUtil;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

@javax.annotation.Generated(value = "class org.wso2.maven.plugins.JavaMSF4JServerCodegen", date = "2016-11-01T13:48:55.078+05:30")
public class SubscriptionsApiServiceImpl extends SubscriptionsApiService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionsApiServiceImpl.class);


    @Override public Response subscriptionsGet(String apiId, String applicationId, Integer offset, Integer limit,
            String accept, String ifNoneMatch) throws NotFoundException {

        List<Subscription> subscribedApiList = null;
        SubscriptionListDTO subscriptionListDTO = null;
        String username = RestApiUtil.getLoggedInUsername();
        limit = limit != null ? limit : RestApiConstants.PAGINATION_LIMIT_DEFAULT;
        offset = offset != null ? offset : RestApiConstants.PAGINATION_OFFSET_DEFAULT;

        try {
            APIStore apiStore = RestApiUtil.getConsumer(username);
            if(!StringUtils.isEmpty(apiId)) {
                subscribedApiList = apiStore.getSubscriptionsByAPI(apiId);
                subscriptionListDTO = SubscriptionMappingUtil.fromSubscriptionListToDTO(subscribedApiList, limit,
                        offset);
            } else if(!StringUtils.isEmpty(applicationId)) {
                Application application = apiStore.getApplicationByUuid(applicationId);
                if (application != null) {
                    subscribedApiList = apiStore.getAPISubscriptionsByApplication(application);
                    subscriptionListDTO = SubscriptionMappingUtil.fromSubscriptionListToDTO(subscribedApiList, limit,
                            offset);
                } else {
                    String errorMessage = "Application not found: " + applicationId;
                    APIMgtResourceNotFoundException e = new APIMgtResourceNotFoundException(
                            errorMessage, ExceptionCodes.APPLICATION_NOT_FOUND);
                    HashMap<String, String> paramList = new HashMap<String, String>();
                    paramList.put(APIMgtConstants.ExceptionsConstants.APPLICATION_ID, applicationId);
                    ErrorDTO errorDTO = RestApiUtil.getErrorDTO(e.getErrorHandler(), paramList);
                    log.error(errorMessage,e);
                    return Response.status(e.getErrorHandler().getHttpStatusCode()).entity(errorDTO).build();
                }
            } else {
                //mandatory parameters not provided
                String errorMessage = "Either applicationId or apiId should be provided";
                ErrorHandler errorHandler = ExceptionCodes.PARAMETER_NOT_PROVIDED;
                ErrorDTO errorDTO = RestApiUtil.getErrorDTO(errorHandler);
                log.error(errorMessage);
                return Response.status(errorHandler.getHttpStatusCode()).entity(errorDTO).build();
            }
        } catch (APIManagementException e) {
            String errorMessage = "Error while retrieving subscriptions";
            HashMap<String, String> paramList = new HashMap<String, String>();
            paramList.put(APIMgtConstants.ExceptionsConstants.API_ID, applicationId);
            paramList.put(APIMgtConstants.ExceptionsConstants.APPLICATION_ID, applicationId);
            ErrorDTO errorDTO = RestApiUtil.getErrorDTO(e.getErrorHandler(), paramList);
            log.error(errorMessage, e);
            return Response.status(e.getErrorHandler().getHttpStatusCode()).entity(errorDTO).build();
        }

        return Response.ok().entity(subscriptionListDTO).build();
    }

    @Override public Response subscriptionsPost(SubscriptionDTO body, String contentType) throws NotFoundException {
        String username = RestApiUtil.getLoggedInUsername();
        try {
            APIStore apiStore = RestApiUtil.getConsumer(username);
            String applicationId = body.getApplicationId();
            String apiId = body.getApiIdentifier();
            String tier = body.getTier();

            if (!StringUtils.isEmpty(applicationId) && !StringUtils.isEmpty(apiId) && !StringUtils.isEmpty(tier)) {
                Application application = apiStore.getApplicationByUuid(applicationId);
                API api = apiStore.getAPIbyUUID(apiId);
                if (application != null && api != null) {
                    apiStore.addApiSubscription(apiId, applicationId, tier);
                } else {
                    String errorMessage = null;
                    ExceptionCodes exceptionCode = null;
                    if (application == null) {
                        exceptionCode = ExceptionCodes.APPLICATION_NOT_FOUND;
                        errorMessage = "Application not found";
                    } else if (api == null) {
                        exceptionCode = ExceptionCodes.API_NOT_FOUND;
                        errorMessage = "Api not found";
                    }
                    APIMgtResourceNotFoundException e = new APIMgtResourceNotFoundException(errorMessage,
                            exceptionCode);
                    HashMap<String, String> paramList = new HashMap<String, String>();
                    ErrorDTO errorDTO = RestApiUtil.getErrorDTO(e.getErrorHandler(), paramList);
                    log.error(errorMessage, e);
                    return Response.status(e.getErrorHandler().getHttpStatusCode()).entity(errorDTO).build();
                }
            } else {
                //mandatory parameters not provided
                String errorMessage = "Some mandatory parameters were not provided";
                ErrorHandler errorHandler = ExceptionCodes.PARAMETER_NOT_PROVIDED;
                ErrorDTO errorDTO = RestApiUtil.getErrorDTO(errorHandler);
                log.error(errorMessage);
                return Response.status(errorHandler.getHttpStatusCode()).entity(errorDTO).build();
            }

        } catch (APIManagementException e) {
            String errorMessage = "Error while adding subscriptions";
            HashMap<String, String> paramList = new HashMap<String, String>();
            paramList.put(APIMgtConstants.ExceptionsConstants.API_ID, body.getApiIdentifier());
            paramList.put(APIMgtConstants.ExceptionsConstants.APPLICATION_ID, body.getApplicationId());
            paramList.put(APIMgtConstants.ExceptionsConstants.TIER, body.getTier());
            ErrorDTO errorDTO = RestApiUtil.getErrorDTO(e.getErrorHandler(), paramList);
            log.error(errorMessage, e);
            return Response.status(e.getErrorHandler().getHttpStatusCode()).entity(errorDTO).build();
        }

        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    @Override public Response subscriptionsSubscriptionIdDelete(String subscriptionId, String ifMatch,
            String ifUnmodifiedSince) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
    @Override
    public Response subscriptionsSubscriptionIdGet(String subscriptionId
, String accept
, String ifNoneMatch
, String ifModifiedSince
 ) throws NotFoundException {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
