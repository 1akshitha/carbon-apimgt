/*
 *
 *   Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.apimgt.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.apimgt.core.api.APIStore;
import org.wso2.carbon.apimgt.core.dao.APISubscriptionDAO;
import org.wso2.carbon.apimgt.core.dao.ApiDAO;
import org.wso2.carbon.apimgt.core.dao.ApplicationDAO;
import org.wso2.carbon.apimgt.core.exception.APIManagementException;
import org.wso2.carbon.apimgt.core.models.APIResults;
import org.wso2.carbon.apimgt.core.models.Application;
import org.wso2.carbon.apimgt.core.util.APIUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of API Store operations.
 *
 */
public class APIStoreImpl extends AbstractAPIManager implements APIStore {

    private static final Logger log = LoggerFactory.getLogger(APIStoreImpl.class);

    public APIStoreImpl(java.lang.String username, ApiDAO apiDAO, ApplicationDAO applicationDAO, APISubscriptionDAO
            apiSubscriptionDAO) {
        super(username, apiDAO, applicationDAO, apiSubscriptionDAO,new APILifeCycleManagerImpl());
    }

    @Override
    public Map<java.lang.String, Object> getAllAPIsByStatus(int offset, int limit, java.lang.String[] status, boolean returnAPITags)
            throws APIManagementException {

        return null;
    }

    @Override
    public Application getApplicationsByName(java.lang.String userId, java.lang.String applicationName, java.lang
            .String groupId)
            throws APIManagementException {
        Application application = null;
        try {
            application = getApplicationDAO().getApplicationByName(userId, applicationName, groupId);
        } catch (SQLException e) {
            APIUtils.logAndThrowException(
                    "Error occurred while fetching application for the given applicationName - " + applicationName, e,
                    log);
        }
        return application;
    }

    @Override public Application[] getApplications(String subscriber, java.lang.String groupingId)
            throws APIManagementException {
        return new Application[0];
    }

    @Override public void updateApplication(Application application) throws APIManagementException {

    }

    @Override public Map<java.lang.String, Object> requestApprovalForApplicationRegistration(java.lang.String userId,
                                                                                             java.lang.String applicationName, java.lang.String tokenType, java.lang.String callbackUrl, java.lang.String[] allowedDomains, java.lang.String validityTime,
                                                                                             java.lang.String tokenScope, java.lang.String groupingId, java.lang.String jsonString) throws APIManagementException {
        return null;
    }

    public APIResults searchAPIs(java.lang.String query, int offset, int limit)
            throws APIManagementException {

        APIResults apiResults = null;
        try {
            List<java.lang.String> roles = new ArrayList<>(); // TODO -- roles list
            apiResults = getApiDAO().searchAPIsForRoles(query, offset, limit,
                    roles);
        } catch (SQLException e) {
            APIUtils.logAndThrowException("Error occurred while updating searching APIs - " + query, e, log);
        }

        return apiResults;
    }


    @Override public void removeApplication(Application application) throws APIManagementException {

    }

    @Override public java.lang.String addApplication(Application application, java.lang.String userId) throws APIManagementException {
        return null;
    }

  }
