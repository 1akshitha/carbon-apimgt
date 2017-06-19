/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import axios from 'axios';
import qs from 'qs';

class Auth {
    constructor() {
        /* TODO: Move this to configuration ~tmkb*/
        this.host = "https://localhost:9292";
        this.token = "/login/token/publisher";
        this.isLogged = false;
        this.user = {};
        this.bearer = "Bearer ";
        this.contextPath = "/publisher";
    }


    static refreshTokenOnExpire() {
        var timestampSkew = 100;
        var currentTimestamp = Math.floor(Date.now() / 1000);
        var tokenTimestamp = localStorage.getItem("expiresIn");
        var rememberMe = (localStorage.getItem("rememberMe") == 'true');
        if (rememberMe && (tokenTimestamp - currentTimestamp < timestampSkew)) {
            var bearerToken = "Bearer " + AuthClient.getCookie("WSO2_AM_REFRESH_TOKEN_1");
            var loginPromise = authManager.refresh(bearerToken);
            loginPromise.then(function (data, status, xhr) {
                authManager.setAuthStatus(true);
                var expiresIn = data.validityPeriod + Math.floor(Date.now() / 1000);
                window.localStorage.setItem("expiresIn", expiresIn);
            });
            loginPromise.error(
                function (error) {
                    var error_data = JSON.parse(error.responseText);
                    var message = "Error while refreshing token" + "<br/> You will be redirect to the login page ...";
                    noty({
                        text: message,
                        type: 'error',
                        dismissQueue: true,
                        modal: true,
                        progressBar: true,
                        timeout: 5000,
                        layout: 'top',
                        theme: 'relax',
                        maxVisible: 10,
                        callback: {
                            afterClose: function () {
                                window.location = loginPageUri;
                            },
                        }
                    });

                }
            );
        }
    }

    /**
     * Static method to handle unauthorized user action error catch, It will look for response status code and skip !401 errors
     * @param {object} error_response
     */
    static unauthorizedErrorHandler(error_response) {
        if (error_response.status !== 401) { /* Skip unrelated response code to handle in unauthorizedErrorHandler*/
            throw error_response;
            /* re throwing the error since we don't handle it here and propagate to downstream error handlers in catch chain*/
        }
        let message = "The session has expired" + ".<br/> You will be redirect to the login page ...";
        if (typeof noty !== 'undefined') {
            noty({
                text: message,
                type: 'error',
                dismissQueue: true,
                modal: true,
                progressBar: true,
                timeout: 5000,
                layout: 'top',
                theme: 'relax',
                maxVisible: 10,
                callback: {
                    afterClose: function () {
                        window.location = loginPageUri;
                    },
                }
            });
        } else {
            throw error_response;
        }
    }

    /**
     * Get JavaScript accessible cookies saved in browser, by giving the cooke name.
     * @param {String} name : Name of the cookie which need to be retrived
     * @returns {String|null} : If found a cookie with given name , return its value,Else null value is returned
     */
    static getCookie(name) {
        let pairs = document.cookie.split(";");
        let cookie = null;
        for (let pair of pairs) {
            pair = pair.split("=");
            let cookie_name = pair[0].trim();
            let value = encodeURIComponent(pair[1]);
            if (cookie_name === name) {
                cookie = value;
                break;
            }
        }
        return cookie;
    }


    /**
     * Set a cookie with given name and value assigned to it. Cookies can be only set to the same origin,
     * which the script is running
     * @param {String} name : Name of the cookie which need to be set
     * @param {String} value : Value of the cookie, expect it to be URLEncoded
     * @param {String} days : (optional) validity period of the cookie
     */
    static setCookie(name, value, days) {
        let expires = "";
        if (days) {
            const date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            expires = "; expires=" + date.toUTCString();
        }
        document.cookie = name + "=" + value + expires + "; path=/";
    }

    /**
     * TODO: Implement this method to return the user logged state by considering the cookies stored in the browser,
     * This may give a partial indication of whether the user has logged in or not, The actual API call may get denied
     * if the cookie stored access token is invalid/expired
     *
     * @returns {boolean} Is any user has logged in or not
     */
    static getAuthStatus() {
        //this.isLogged = !(!$.cookie('token') && !$.cookie('user'));
        return this.isLogged;
    }

    setUserName(username) {
        this.user.username = username;
    }

    getUserName() {
        return this.user.username;
    }

    setUserScope(scope) {
        this.user.scope = scope;
    }

    getUserScope() {
        return this.user.scope;
    }

    getTokenEndpoint() {
        return this.host + this.token;
    }

    /**
     * By given username and pa ssword Authenticate the user
     * @param {String} username : Username of the user
     * @param {String} password : Plain text password
     * @returns {AxiosPromise} : Promise object with the login request made
     */
    authenticateUser(username, password) {
        const headers = {
            'Authorization': 'Basic deidwe',
            'Accept': 'application/json',
            'Content-Type': 'application/x-www-form-urlencoded'
        };
        const data = {
            username: username,
            password: password,
            grant_type: 'password',
            validity_period: 3600,
            scopes: 'apim:api_view apim:api_create apim:api_publish apim:tier_view apim:tier_manage apim:subscription_view apim:subscription_block apim:subscribe'
        };
        let promised_response = axios.post(this.getTokenEndpoint(), qs.stringify(data), {headers: headers});
        promised_response.then(response => {
            let WSO2_AM_TOKEN_1 = response.data.partialToken;
            Auth.setCookie('WSO2_AM_TOKEN_1', WSO2_AM_TOKEN_1);
        });
        return promised_response;
    }

    logout() {
        var authzHeader = this.bearer + Auth.getCookie("WSO2_AM_TOKEN_1");
        var url = this.contextPath + '/auth/apis/login/revoke';
        var headers = {
            'Accept': 'application/json',
            'Authorization': authzHeader
        };
        return axios.post(url, null, {headers: headers});
    }

    refresh(authzHeader) {
        var params = {
            grant_type: 'refresh_token',
            validity_period: '3600',
            scopes: 'apim:api_view apim:api_create apim:api_publish apim:tier_view apim:tier_manage' +
            ' apim:subscription_view apim:subscription_block apim:subscribe'
        };
        var referrer = (document.referrer.indexOf("https") !== -1) ? document.referrer : null;
        var url = this.contextPath + '/auth/apis/login/token';
        var headers = {
            'Authorization': authzHeader,
            'Accept': 'application/json',
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Alt-Referer': referrer
        };
        return axios.post(url, qs.stringify(params), {headers: headers});
    }
}

export default Auth;
