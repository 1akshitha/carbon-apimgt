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

import org.wso2.carbon.apimgt.core.api.APIManager;
import org.wso2.carbon.apimgt.core.dao.APIManagementException;
import org.wso2.carbon.apimgt.models.API;
import org.wso2.carbon.apimgt.models.APIIdentifier;

import java.util.List;
import java.util.Set;

public class AbstractAPIManager implements APIManager {
    @Override
    public List<API> getAllAPIs() throws APIManagementException {
        return null;
    }

    @Override
    public API getAPI(String apiPath) throws APIManagementException {
        return null;
    }

    @Override
    public API getAPIbyUUID(String uuid) throws APIManagementException {
        return null;
    }

    @Override
    public API getAPI(APIIdentifier identifier) throws APIManagementException {
        return null;
    }

    @Override
    public boolean isAPIAvailable(APIIdentifier identifier) throws APIManagementException {
        return false;
    }

    @Override
    public boolean isContextExist(String context) throws APIManagementException {
        return false;
    }

    @Override
    public boolean isApiNameExist(String apiName) throws APIManagementException {
        return false;
    }

    @Override
    public Set<String> getAPIVersions(String providerName, String apiName) throws APIManagementException {
        return null;
    }

    @Override
    public String getSwagger20Definition(APIIdentifier apiId) throws APIManagementException {
        return null;
    }
}
