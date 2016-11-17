/*
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
 */

package org.wso2.carbon.apimgt.core.dao.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DAOIntegrationTestBase {
    private DataSource dataSource;
    private static final String sqlFilePath = ".." + File.separator + ".." + File.separator + ".." + File.separator
                                              + "features" + File.separator + "apimgt" + File.separator
                                              + "org.wso2.carbon.apimgt.core.feature" + File.separator + "resources"
                                              + File.separator + "dbscripts" + File.separator + "h2.sql";

    @org.testng.annotations.BeforeMethod
    public void setUp() throws Exception {
        dataSource = new InMemoryDataSource();
        DAOUtil.initialize(dataSource);

        try (Connection connection = DAOUtil.getConnection()) {
            DBScriptRunnerUtil.executeSQLScript(sqlFilePath, connection);
        }
    }

    @org.testng.annotations.AfterMethod
    public void tempDBCleanup() throws SQLException, IOException {
        ((InMemoryDataSource) dataSource).resetDB();
    }

}
