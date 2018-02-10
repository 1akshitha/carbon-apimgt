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
"use strict";
import APIClient from "./APIClient";

/**
 * Class representing a Factory of APIClients
 */
class APIClientFactory {
    /**
     * Initialize a single instance of APIClientFactory
     * @returns {APIClientFactory}
     */
    constructor() {
        if (APIClientFactory._instance) {
            return APIClientFactory._instance;
        }

        this._APIClientMap = new Map();
        APIClientFactory._instance = this;
    }

    /**
     *
     * @param {Object} environment
     * @returns {APIClient} APIClient object for the environment
     */
    getAPIClient(environment) {
        let api_Client = this._APIClientMap.get(environment.label);

        if (api_Client) {
            return api_Client;
        }

        api_Client = new APIClient(environment);
        this._APIClientMap.set(environment.label, api_Client);
        return api_Client;
    }

    /**
     * Remove an APIClient object from the environment
     * @param {String} environmentLabel
     */
    destroyAPIClient(environmentLabel) {
        this._APIClientMap.delete(environmentLabel);
    }

    /**
     * Get an instance of APIClientFactory
     * @returns {APIClientFactory} An instance of APIClientFactory
     */
    static getInstance() {
        return new APIClientFactory();
    }
}

/**
 * Single instance of APIClientFactory
 * @type {APIClientFactory}
 * @private
 */
APIClientFactory._instance = null;

export default APIClientFactory;