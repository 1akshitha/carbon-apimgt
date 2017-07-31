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
package org.wso2.carbon.apimgt.core.template;

import org.testng.annotations.Test;
import org.wso2.carbon.apimgt.core.SampleTestObjectCreator;
import org.wso2.carbon.apimgt.core.models.API;

/**
 * Test cases for APIConfigContext
 *
 */
public class APIConfigContextTestCase {
	
	@Test
	public void createValidAPIConfigContext() throws APITemplateException {
		
		APIConfigContext apiConfigContext = new APIConfigContext(SampleTestObjectCreator.createDefaultAPI().build(), "org.test");
		apiConfigContext.validate();		
	}
	
	@Test(expectedExceptions = APITemplateException.class)
	public void createInvalidAPIConfigContext() throws APITemplateException {
		
		API emptyNamedAPI = new API.APIBuilder("provider", "", "1.0.0").build();	
		APIConfigContext apiConfigContext = new APIConfigContext(emptyNamedAPI, "org.test");		
		apiConfigContext.validate();		
	}

}
