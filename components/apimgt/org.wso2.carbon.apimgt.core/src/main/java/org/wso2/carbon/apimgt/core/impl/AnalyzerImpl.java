/*
* Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.wso2.carbon.apimgt.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.apimgt.core.api.Analyzer;
import org.wso2.carbon.apimgt.core.dao.AnalyticsDAO;
import org.wso2.carbon.apimgt.core.exception.APIManagementException;
import org.wso2.carbon.apimgt.core.exception.APIMgtDAOException;
import org.wso2.carbon.apimgt.core.exception.AnalyticsException;
import org.wso2.carbon.apimgt.core.exception.ExceptionCodes;
import org.wso2.carbon.apimgt.core.models.analytics.APICount;
import org.wso2.carbon.apimgt.core.models.analytics.APIInfo;
import org.wso2.carbon.apimgt.core.models.analytics.APISubscriptionCount;
import org.wso2.carbon.apimgt.core.models.analytics.ApplicationCount;
import org.wso2.carbon.apimgt.core.models.analytics.SubscriptionCount;
import org.wso2.carbon.apimgt.core.models.analytics.SubscriptionInfo;

import java.util.List;

/**
 * Implementation class of Analyzer operations.
 */
public class AnalyzerImpl implements Analyzer {

    private static final Logger log = LoggerFactory.getLogger(AnalyzerImpl.class);

    private String username;
    private AnalyticsDAO analyticsDAO;

    public AnalyzerImpl(String username, AnalyticsDAO analyticsDAO) {
        this.username = username;
        this.analyticsDAO = analyticsDAO;
    }

    @Override
    public List<ApplicationCount> getApplicationCount(String fromTime,
                                                      String toTime) throws APIManagementException {
        List<ApplicationCount> applicationCountList;
        try {
            applicationCountList = getAnalyticsDAO().getApplicationCount(fromTime, toTime);
        } catch (APIMgtDAOException e) {
            String errorMsg = "Error occurred while fetching application count information";
            throw new AnalyticsException(errorMsg, e, ExceptionCodes.APIMGT_DAO_EXCEPTION);
        }
        return applicationCountList;
    }

    @Override
    public List<APIInfo> getAPIInfo(String fromTime, String toTime) throws APIManagementException {
        List<APIInfo> apiInfoList;
        try {
            apiInfoList = getAnalyticsDAO().getAPIInfo(fromTime, toTime);
        } catch (APIMgtDAOException e) {
            String errorMsg = "Error occurred while fetching API information";
            throw new AnalyticsException(errorMsg, e, ExceptionCodes.APIMGT_DAO_EXCEPTION);
        }
        return apiInfoList;
    }

    @Override
    public List<APICount> getAPICount(String fromTime, String toTime) throws APIManagementException {
        List<APICount> apiCountList;
        try {
            apiCountList = getAnalyticsDAO().getAPICount(fromTime, toTime);
        } catch (APIMgtDAOException e) {
            String errorMsg = "Error occurred while fetching API count information";
            throw new AnalyticsException(errorMsg, e, ExceptionCodes.APIMGT_DAO_EXCEPTION);
        }
        return apiCountList;
    }

    @Override
    public List<APISubscriptionCount> getAPISubscriptionCount(String createdBy) throws APIManagementException {
        List<APISubscriptionCount> apiSubscriptionCountList;
        try {
            apiSubscriptionCountList = getAnalyticsDAO().getAPISubscriptionCount(createdBy);
        } catch (APIMgtDAOException e) {
            String errorMsg = "Error occurred while fetching API subscription count information";
            throw new AnalyticsException(errorMsg, e, ExceptionCodes.APIMGT_DAO_EXCEPTION);
        }
        return apiSubscriptionCountList;
    }

    @Override
    public List<SubscriptionCount> getSubscriptionCount(String fromTime, String toTime) throws
            APIManagementException {
        List<SubscriptionCount> subscriptionCountList;
        try {
            subscriptionCountList = getAnalyticsDAO().getSubscriptionCount(fromTime, toTime);
        } catch (APIMgtDAOException e) {
            String errorMsg = "Error occurred while fetching Subscription count information";
            throw new AnalyticsException(errorMsg, e, ExceptionCodes.APIMGT_DAO_EXCEPTION);
        }
        return subscriptionCountList;
    }

    /**
     * Retrieves Subscriptions info details.
     *
     * @param fromTime  Filter for from timestamp
     * @param toTime    Filter for to timestamp
     * @return valid {@link SubscriptionInfo} List or null
     * @throws APIManagementException if error occurs while accessing data layer
     */
    @Override
    public List<SubscriptionInfo> getSubscriptionInfo(String fromTime, String toTime) throws
            APIManagementException {
        List<SubscriptionInfo> subscriptionInfoList;
        try {
            subscriptionInfoList = getAnalyticsDAO().getSubscriptionInfo(fromTime, toTime);
        } catch (APIMgtDAOException e) {
            String errorMsg = "Error occurred while fetching Subscription information";
            throw new AnalyticsException(errorMsg, e, ExceptionCodes.APIMGT_DAO_EXCEPTION);
        }
        return subscriptionInfoList;
    }

    public AnalyticsDAO getAnalyticsDAO() {
        return analyticsDAO;
    }

    public String getUsername() {
        return username;
    }
}
