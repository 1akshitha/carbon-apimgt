package org.wso2.carbon.apimgt.rest.api.store.impl;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.apimgt.core.api.APIStore;
import org.wso2.carbon.apimgt.core.exception.APIManagementException;
import org.wso2.carbon.apimgt.core.impl.APIManagerFactory;
import org.wso2.carbon.apimgt.core.models.Application;
import org.wso2.carbon.apimgt.core.models.Tier;
import org.wso2.carbon.apimgt.core.util.APIUtils;
import org.wso2.carbon.apimgt.rest.api.common.APIConstants;
import org.wso2.carbon.apimgt.rest.api.common.ApplicationConstants;
import org.wso2.carbon.apimgt.rest.api.common.RestApiConstants;
import org.wso2.carbon.apimgt.rest.api.common.util.RestApiUtil;
import org.wso2.carbon.apimgt.rest.api.store.ApplicationsApiService;
import org.wso2.carbon.apimgt.rest.api.store.NotFoundException;
import org.wso2.carbon.apimgt.rest.api.store.dto.ApplicationDTO;
import org.wso2.carbon.apimgt.rest.api.store.dto.ApplicationKeyDTO;
import org.wso2.carbon.apimgt.rest.api.store.dto.ApplicationKeyGenerateRequestDTO;
import org.wso2.carbon.apimgt.rest.api.store.dto.ApplicationListDTO;
import org.wso2.carbon.apimgt.rest.api.store.mappings.ApplicationKeyMappingUtil;
import org.wso2.carbon.apimgt.rest.api.store.mappings.ApplicationMappingUtil;
import org.wso2.carbon.apimgt.rest.api.store.util.RestAPIStoreUtils;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@javax.annotation.Generated(value = "class org.wso2.maven.plugins.JavaMSF4JServerCodegen", date = "2016-11-01T13:48:55.078+05:30")
public class ApplicationsApiServiceImpl extends ApplicationsApiService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationsApiServiceImpl.class);

    @Override
    public Response applicationsApplicationIdDelete(java.lang.String applicationId, java.lang.String ifMatch, java
            .lang.String ifUnmodifiedSince)
            throws NotFoundException {
        java.lang.String username = RestApiUtil.getLoggedInUsername();
        try {
            APIStore apiConsumer = APIManagerFactory.getInstance().getAPIConsumer(username);
            Application application = apiConsumer.getApplicationByUUID(applicationId);
            if (application != null) {
                if (RestAPIStoreUtils.isUserAccessAllowedForApplication(application)) {
                    apiConsumer.removeApplication(application);
                    return Response.ok().build();
                } else {
                    RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
                }
            } else {
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
            }
        } catch (APIManagementException e) {
            RestApiUtil.handleInternalServerError("Error while deleting application " + applicationId, e, log);
        }
        return null;
    }

    @Override
    public Response applicationsApplicationIdGet(java.lang.String applicationId, java.lang.String accept, java.lang.String ifNoneMatch,

                                                 java.lang.String ifModifiedSince) throws NotFoundException {
        java.lang.String username = RestApiUtil.getLoggedInUsername();
        try {
            APIStore apiConsumer = APIManagerFactory.getInstance().getAPIConsumer(username);
            Application application = apiConsumer.getApplicationByUUID(applicationId);
            if (application != null) {
                if (RestAPIStoreUtils.isUserAccessAllowedForApplication(application)) {
                    ApplicationDTO applicationDTO = ApplicationMappingUtil.fromApplicationtoDTO(application);
                    return Response.ok().entity(applicationDTO).build();
                } else {
                    RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
                }
            } else {
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
            }
        } catch (APIManagementException e) {
            RestApiUtil.handleInternalServerError("Error while retrieving application " + applicationId, e, log);
        }
        return null;
    }

    @Override public Response applicationsApplicationIdPut(java.lang.String applicationId, ApplicationDTO body,
                                                           java.lang.String contentType, java.lang.String ifMatch, java.lang.String ifUnmodifiedSince) throws NotFoundException {
        java.lang.String username = RestApiUtil.getLoggedInUsername();
        try {
            APIStore apiConsumer = APIManagerFactory.getInstance().getAPIConsumer(username);
            Application oldApplication = apiConsumer.getApplicationByUUID(applicationId);
            if (oldApplication != null) {
                if (RestAPIStoreUtils.isUserAccessAllowedForApplication(oldApplication)) {
                    //we do not honor the subscriber coming from the request body as we can't change the subscriber of the application
                    Application application = ApplicationMappingUtil.fromDTOtoApplication(body, username);
                    //groupId of the request body is not honored for now.
                    // Later we can improve by checking admin privileges of the user.
                    application.setGroupId(oldApplication.getGroupId());
                    //we do not honor the application id which is sent via the request body
                    application.setUUID(oldApplication.getUUID());

                    apiConsumer.updateApplication(application);

                    //retrieves the updated application and send as the response
                    Application updatedApplication = apiConsumer.getApplicationByUUID(applicationId);
                    ApplicationDTO updatedApplicationDTO = ApplicationMappingUtil
                            .fromApplicationtoDTO(updatedApplication);
                    return Response.ok().entity(updatedApplicationDTO).build();
                } else {
                    RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
                }
            } else {
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
            }
        } catch (APIManagementException e) {
            RestApiUtil.handleInternalServerError("Error while updating application " + applicationId, e, log);
        }
        return null;
    }

    @Override
    public Response applicationsGenerateKeysPost(java.lang.String applicationId, ApplicationKeyGenerateRequestDTO body,
                                                 java.lang.String contentType, java.lang.String ifMatch, java.lang.String ifUnmodifiedSince) throws NotFoundException {
        java.lang.String username = RestApiUtil.getLoggedInUsername();
        try {
            APIStore apiConsumer = APIManagerFactory.getInstance().getAPIConsumer(username);
            Application application = apiConsumer.getApplicationByUUID(applicationId);
            if (application != null) {
                if (RestAPIStoreUtils.isUserAccessAllowedForApplication(application)) {
                    java.lang.String[] accessAllowDomainsArray = body.getAccessAllowDomains().toArray(new java.lang.String[1]);
                    JSONObject jsonParamObj = new JSONObject();
                    jsonParamObj.put(ApplicationConstants.OAUTH_CLIENT_USERNAME, username);
                    java.lang.String jsonParams = jsonParamObj.toString();
                    java.lang.String tokenScopes = StringUtils.join(body.getScopes(), " ");

                    Map<java.lang.String, Object> keyDetails = apiConsumer
                            .requestApprovalForApplicationRegistration(username, application.getName(),
                                    body.getKeyType().toString(), body.getCallbackUrl(), accessAllowDomainsArray,
                                    body.getValidityTime(), tokenScopes, application.getGroupId(), jsonParams);
                    ApplicationKeyDTO applicationKeyDTO = ApplicationKeyMappingUtil
                            .fromApplicationKeyToDTO(keyDetails, body.getKeyType().toString());

                    return Response.ok().entity(applicationKeyDTO).build();
                } else {
                    RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
                }
            } else {
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
            }
        } catch (APIManagementException e) {
            if (RestApiUtil.rootCauseMessageMatches(e, "primary key violation")) {
                RestApiUtil
                        .handleResourceAlreadyExistsError("Keys already generated for the application " + applicationId,
                                e, log);
            } else {
                RestApiUtil.handleInternalServerError("Error while generating keys for application " + applicationId, e,
                        log);
            }
        }
        return null;
    }

    @Override public Response applicationsGet(java.lang.String query, Integer limit, Integer offset, java.lang.String accept,
                                              java.lang.String ifNoneMatch) throws NotFoundException {
        java.lang.String username = RestApiUtil.getLoggedInUsername();

        // currently groupId is taken from the user so that groupId coming as a query parameter is not honored.
        // As a improvement, we can check admin privileges of the user and honor groupId.
        java.lang.String groupId = RestApiUtil.getLoggedInUserGroupId();

        limit = limit != null ? limit : RestApiConstants.PAGINATION_LIMIT_DEFAULT;
        offset = offset != null ? offset : RestApiConstants.PAGINATION_OFFSET_DEFAULT;

        ApplicationListDTO applicationListDTO;
        try {
            APIStore apiConsumer = APIManagerFactory.getInstance().getAPIConsumer(username);
            Application[] allMatchedApps = new Application[0];
            if (StringUtils.isBlank(query)) {
                allMatchedApps = apiConsumer.getApplications(username, groupId);
            } else {
                Application application = apiConsumer.getApplicationsByName(username, query, groupId);
                if (application != null) {
                    allMatchedApps = new Application[1];
                    allMatchedApps[0] = application;
                }
            }

            //allMatchedApps are already sorted to application name
            applicationListDTO = ApplicationMappingUtil.fromApplicationsToDTO(allMatchedApps, limit, offset);
            ApplicationMappingUtil.setPaginationParams(applicationListDTO, groupId, limit, offset,
                    allMatchedApps.length);

            return Response.ok().entity(applicationListDTO).build();
        } catch (APIManagementException e) {
            RestApiUtil
                    .handleInternalServerError("Error while retrieving applications of the user " + username, e, log);
        }
        return null;
    }

    @Override public Response applicationsPost(ApplicationDTO body, java.lang.String contentType) throws NotFoundException {
        java.lang.String username = RestApiUtil.getLoggedInUsername();
        try {
            APIStore apiConsumer = APIManagerFactory.getInstance().getAPIConsumer(username);
            //validate the tier specified for the application
            java.lang.String tierName = body.getThrottlingTier();
            if (tierName != null) {
                Map<java.lang.String, Tier> appTierMap = APIUtils.getTiers(APIConstants.TIER_APPLICATION_TYPE);
                if (appTierMap == null || RestApiUtil.findTier(appTierMap.values(), tierName) == null) {
                    RestApiUtil.handleBadRequest("Specified tier " + tierName + " is invalid", log);
                }
            } else {
                RestApiUtil.handleBadRequest("Throttling tier cannot be null", log);
            }

            //subscriber field of the body is not honored. It is taken from the context
            Application application = ApplicationMappingUtil.fromDTOtoApplication(body, username);

            //setting the proper groupId. This is not honored for now.
            // Later we can honor it by checking admin privileges of the user.
            java.lang.String groupId = RestApiUtil.getLoggedInUserGroupId();
            application.setGroupId(groupId);
            java.lang.String applicationUUID = apiConsumer.addApplication(application, username);

            //retrieves the created application and send as the response
            Application createdApplication = apiConsumer.getApplicationByUUID(applicationUUID);
            ApplicationDTO createdApplicationDTO = ApplicationMappingUtil.fromApplicationtoDTO(createdApplication);

            //to be set as the Location header
            URI location = new URI(RestApiConstants.RESOURCE_PATH_APPLICATIONS + "/" +
                    createdApplicationDTO.getApplicationId());
            return Response.created(location).entity(createdApplicationDTO).build();
        } catch (APIManagementException | URISyntaxException e) {
            if (RestApiUtil.isDueToResourceAlreadyExists(e)) {
                RestApiUtil.handleResourceAlreadyExistsError(
                        "An application already exists with name " + body.getName(), e,
                        log);
            } else {
                RestApiUtil.handleInternalServerError("Error while adding a new application for the user " + username,
                        e, log);
            }
        }
        return null;
    }
}
