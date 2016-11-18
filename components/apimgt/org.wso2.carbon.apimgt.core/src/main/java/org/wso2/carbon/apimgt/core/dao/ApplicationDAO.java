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

package org.wso2.carbon.apimgt.core.dao;


import org.wso2.carbon.apimgt.core.models.Application;
import org.wso2.carbon.apimgt.core.models.ApplicationSummaryResults;

import java.sql.SQLException;
import javax.annotation.CheckForNull;

/**
 * Provides access to Application data layer
 */
public interface ApplicationDAO {
    /**
     * Retrieve a given instance of an Application
     * @param appID The UUID that uniquely identifies an Application
     * @return valid {@link Application} object or null
     * @throws SQLException
     *
     */
    @CheckForNull
    Application getApplication(String appID) throws SQLException;

    /**
     * Fetches an Application by name.
     *
     * @param applicationName Name of the Application
     * @param userId          Name of the User.
     * @throws SQLException
     */
    Application getApplicationByName(String userId, String applicationName,  String groupId) throws SQLException;

    /**
     * Retrieves summary data of all available Applications that belongs to a user or a groups.
     *
     * @param createdUser Username of user
     * @param groupingId Id of group
     * @return An array of {@link Application}
     * @throws SQLException
     */
    Application[] getApplications(String createdUser, String groupingId) throws SQLException;

    /**
     * Retrieves summary data of all available Applications. This method supports result pagination and
     * ensures results returned are those that belong to the specified username
     * @param offset The number of results from the beginning that is to be ignored
     * @param limit The maximum number of results to be returned after the offset
     * @param userName The username to filter results by
     * @return {@link ApplicationSummaryResults} matching results
     * @throws SQLException
     */
    ApplicationSummaryResults getApplicationsForUser(int offset, int limit, String userName)
                                                                                throws SQLException;

    /**
     * Retrieves summary data of all available Applications. This method supports result pagination and
     * ensures results returned are those that belong to the specified Group ID
     * @param offset The number of results from the beginning that is to be ignored
     * @param limit The maximum number of results to be returned after the offset
     * @param groupID The Group ID to filter results by
     * @return {@link ApplicationSummaryResults} matching results
     * @throws SQLException
     *
     */
    ApplicationSummaryResults getApplicationsForGroup(int offset, int limit, String groupID)
            throws SQLException;

    /**
     * Retrieves summary data of all available Applications that match the given search criteria. This method supports
     * result pagination and ensuring results returned are for Apps belonging to the specified username
     * @param searchAttribute The attribute of an Application against which the search will be performed
     * @param searchString The search string provided
     * @param offset The number of results from the beginning that is to be ignored
     * @param limit The maximum number of results to be returned after the offset
     * @param userName The username to filter results by
     * @return {@link ApplicationSummaryResults} matching results
     * @throws SQLException
     *
     */
    ApplicationSummaryResults searchApplicationsForUser(String searchAttribute, String searchString, int offset,
                                                        int limit, String userName) throws SQLException;

    /**
     * Retrieves summary data of all available Applications that match the given search criteria. This method supports
     * result pagination and ensuring results returned are for Apps belonging to the specified Group ID
     * @param searchAttribute The attribute of an Application against which the search will be performed
     * @param searchString The search string provided
     * @param offset The number of results from the beginning that is to be ignored
     * @param limit The maximum number of results to be returned after the offset
     * @param groupID The Group ID to filter results by
     * @return {@link ApplicationSummaryResults} matching results
     * @throws SQLException
     *
     */
    ApplicationSummaryResults searchApplicationsForGroup(String searchAttribute, String searchString, int offset,
                                                        int limit, String groupID) throws SQLException;

    /**
     * Add a new instance of an Application
     * @param application The {@link Application} object to be added
     * @throws SQLException
     *
     */
    void addApplication(Application application) throws SQLException;

    /**
     * Update an existing Application
     * @param appID The UUID of the Application that needs to be updated
     * @param updatedApp Substitute {@link Application} object that will replace the existing Application
     * @throws SQLException
     *
     */
    void updateApplication(String appID, Application updatedApp) throws SQLException;

    /**
     * Remove an existing Application
     * @param appID The UUID of the Application that needs to be deleted
     * @throws SQLException
     *
     */
    void deleteApplication(String appID) throws SQLException;

    /**
     * Check whether given application name is already available in the system
     *
     * @param appName  application name
     * @return true if application name is already available
     * @throws SQLException if failed to get applications for given subscriber
     */
    boolean isApplicationNameExists(String appName) throws SQLException;

}
