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

import org.testng.Assert;
import org.testng.annotations.Test;
import org.wso2.carbon.apimgt.core.dao.ApplicationDAO;
import org.wso2.carbon.apimgt.core.models.Application;

import java.sql.SQLException;

public class ApplicationDAOImplIT extends DAOIntegrationTestBase{

    @Test
    public void testAddAndGetApplication() throws Exception {
        Application app = addTestApplication();
        ApplicationDAO applicationDAO = new ApplicationDAOImpl();
        Application appFromDB = applicationDAO.getApplication(app.getUUID());
        Assert.assertNotNull(appFromDB);
        validateApp(appFromDB, app);
    }

    @Test
    public void testUpdateApplication() throws Exception {
        Application currentApp = addTestApplication();
        ApplicationDAO applicationDAO = new ApplicationDAOImpl();
        Application newApp = SampleTestObjectCreator.createAlternativeApplication();
        newApp.setUUID(currentApp.getUUID());
        newApp.setCreatedTime(currentApp.getCreatedTime());
        applicationDAO.updateApplication(currentApp.getUUID(), newApp);
        Application appFromDB = applicationDAO.getApplication(newApp.getUUID());
        Assert.assertNotNull(appFromDB);
        validateApp(appFromDB, newApp);
    }

    @Test
    public void testDeleteApplication() throws Exception {
        Application app = addTestApplication();
        ApplicationDAO applicationDAO = new ApplicationDAOImpl();
        applicationDAO.deleteApplication(app.getUUID());
        Application appFromDB = applicationDAO.getApplication(app.getUUID());
        Assert.assertNull(appFromDB);
    }

    @Test
    public void testGetApplicationsForUser() throws Exception {

    }

    @Test
    public void testGetApplicationsForGroup() throws Exception {

    }

    @Test
    public void testSearchApplicationsForUser() throws Exception {

    }

    @Test
    public void testSearchApplicationsForGroup() throws Exception {

    }

    @Test
    public void testGetApplicationById() throws Exception {

    }

    private void validateApp(Application appFromDB, Application expectedApp) {
        Assert.assertEquals(appFromDB.getName(), expectedApp.getName());
        Assert.assertEquals(appFromDB.getCallbackUrl(), expectedApp.getCallbackUrl());
        Assert.assertEquals(appFromDB.getGroupId(), expectedApp.getGroupId());
        Assert.assertEquals(appFromDB.getStatus(), expectedApp.getStatus());
        Assert.assertEquals(appFromDB.getUUID(), expectedApp.getUUID());
        Assert.assertEquals(appFromDB.getTier(), expectedApp.getTier());
        Assert.assertEquals(appFromDB.getCreatedUser(), expectedApp.getCreatedUser());
        Assert.assertEquals(appFromDB.getCreatedTime(), expectedApp.getCreatedTime());
        Assert.assertEquals(appFromDB.getUpdatedUser(), expectedApp.getUpdatedUser());
        Assert.assertEquals(appFromDB.getUpdatedTime(), expectedApp.getUpdatedTime());
    }

    private Application addTestApplication() throws SQLException {
        ApplicationDAO applicationDAO = new ApplicationDAOImpl();
        Application application = SampleTestObjectCreator.createDefaultApplication();
        applicationDAO.addApplication(application);
        return application;
    }

}