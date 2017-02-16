/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.apimgt.authenticator.utils;

import org.wso2.carbon.apimgt.core.models.AccessTokenRequest;
import org.wso2.msf4j.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Cookie;
/**
 * This method authenticate the user.
 *
 */
public class AuthUtil {

    /**
     * This method authenticate the user.
     *
     */
    public static String getHttpOnlyCookieHeader(Cookie cookie) {
        return cookie + "; HttpOnly";
    }

    private static Map<String, Map<String, String>> consumerKeySecretMap = new HashMap<>();

    private static List<String> roleList;

    public static String getAppContext(Request request) {
        //TODO this method should provide uuf app context. Consider the scenarios of reverse proxy as well.
        return "/" + request.getProperty("REQUEST_URL").toString().split("/")[1];
    }

    public static Map<String, Map<String, String>> getConsumerKeySecretMap() {
        return consumerKeySecretMap;
    }


    /**
     * This method is used to generate access token request to login for uuf apps.
     *
     */
    public static AccessTokenRequest createAccessTokenRequest(String username, String password, String grantType,
            String[] scopes, String clientId, String clientSecret) {

        AccessTokenRequest tokenRequest = new AccessTokenRequest();
        tokenRequest.setClientId(clientId);
        tokenRequest.setClientSecret(clientSecret);
        tokenRequest.setGrantType(grantType);
        tokenRequest.setResourceOwnerUsername(username);
        tokenRequest.setResourceOwnerPassword(password);
        tokenRequest.setScopes(scopes);
        return tokenRequest;

    }

    public static List<String> getRoleList() {
        return roleList;
    }

    public static void setRoleList(List<String> roleList) {
        AuthUtil.roleList = roleList;
    }



}
