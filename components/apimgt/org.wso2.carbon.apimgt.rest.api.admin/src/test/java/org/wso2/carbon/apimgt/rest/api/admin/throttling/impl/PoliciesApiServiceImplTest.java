/*
 *
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 * /
 */

package org.wso2.carbon.apimgt.rest.api.admin.throttling.impl;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.wso2.carbon.apimgt.core.api.APIMgtAdminService;
import org.wso2.carbon.apimgt.core.models.policy.APIPolicy;
import org.wso2.carbon.apimgt.core.models.policy.ApplicationPolicy;
import org.wso2.carbon.apimgt.core.models.policy.CustomPolicy;
import org.wso2.carbon.apimgt.core.models.policy.SubscriptionPolicy;
import org.wso2.carbon.apimgt.rest.api.admin.dto.AdvancedThrottlePolicyDTO;
import org.wso2.carbon.apimgt.rest.api.admin.dto.ApplicationThrottlePolicyDTO;
import org.wso2.carbon.apimgt.rest.api.admin.dto.CustomRuleDTO;
import org.wso2.carbon.apimgt.rest.api.admin.dto.SubscriptionThrottlePolicyDTO;
import org.wso2.carbon.apimgt.rest.api.admin.impl.PoliciesApiServiceImpl;
import org.wso2.carbon.apimgt.rest.api.admin.mappings.AdvancedThrottlePolicyMappingUtil;
import org.wso2.carbon.apimgt.rest.api.admin.mappings.ApplicationThrottlePolicyMappingUtil;
import org.wso2.carbon.apimgt.rest.api.admin.mappings.CustomPolicyMappingUtil;
import org.wso2.carbon.apimgt.rest.api.admin.mappings.SubscriptionThrottlePolicyMappingUtil;
import org.wso2.msf4j.Request;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PoliciesApiServiceImplTest {
    private final static Logger logger = LoggerFactory.getLogger(PoliciesApiServiceImplTest.class);

    @Test
    public void policiesThrottlingAdvancedGetTest() throws Exception {
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);

        APIPolicy policy1 = new APIPolicy("samplePolicy1");
        APIPolicy policy2 = new APIPolicy("samplePolicy2");
        List<APIPolicy> policies = new ArrayList<>();
        policies.add(policy1);
        policies.add(policy2);
        Mockito.doReturn(policies).doThrow(new IllegalArgumentException()).when(adminService).getApiPolicies();

        Response response = policiesApiService.policiesThrottlingAdvancedGet(null, null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingAdvancedPolicyIdDeleteTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        String uuid = UUID.randomUUID().toString();

        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(adminService)
                .deletePolicyByUuid(uuid, APIMgtAdminService.PolicyLevel.api);
        Response response = policiesApiService.policiesThrottlingAdvancedIdDelete(uuid, null, null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingAdvancedPolicyIdGetTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        String uuid = UUID.randomUUID().toString();

        APIPolicy policy1 = new APIPolicy(uuid, "samplePolicy1");
        policy1.setDescription("Sample Policy");
        policy1.setDeployed(true);
        policy1.setDisplayName("Simple Policy");
        Mockito.doReturn(policy1).doThrow(new IllegalArgumentException()).when(adminService).getApiPolicyByUuid(uuid);

        Response response = policiesApiService.policiesThrottlingAdvancedIdGet(uuid, null, null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingAdvancedPolicyIdPutTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        String uuid = UUID.randomUUID().toString();
        AdvancedThrottlePolicyDTO dto = new AdvancedThrottlePolicyDTO();

        APIPolicy policy1 = new APIPolicy(uuid, "samplePolicy1");
        Mockito.doReturn(policy1).doThrow(new IllegalArgumentException()).when(adminService).getApiPolicyByUuid(uuid);

        Response response = policiesApiService.policiesThrottlingAdvancedIdPut(uuid, dto, null, null, getRequest());
        Assert.assertEquals(201, response.getStatus());
    }

    @Test
    public void policiesThrottlingAdvancedPostTest()  throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        AdvancedThrottlePolicyDTO dto = new AdvancedThrottlePolicyDTO();
        String uuid = UUID.randomUUID().toString();
        dto.setId(uuid);
        dto.setDisplayName("Sample Policy Display Name");
        dto.setDescription("Simple Description");
        dto.setPolicyName("Simple Policy Name");
        dto.setIsDeployed(true);

        APIPolicy policy1 = AdvancedThrottlePolicyMappingUtil.fromAdvancedPolicyDTOToPolicy(dto);
        Mockito.when(adminService.addApiPolicy(Mockito.any(APIPolicy.class))).thenReturn(uuid);
        Mockito.when(adminService.getApiPolicyByUuid(uuid)).thenReturn(policy1);
        Response response = policiesApiService.policiesThrottlingAdvancedPost(dto, getRequest());
        Assert.assertEquals(201, response.getStatus());
    }

    @Test
    public void policiesThrottlingApplicationGetTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);

        ApplicationPolicy policy1 = new ApplicationPolicy("sampleAppPolicy1");
        ApplicationPolicy policy2 = new ApplicationPolicy("sampleAppPolicy2");
        List<ApplicationPolicy> policies = new ArrayList<>();
        policies.add(policy1);
        policies.add(policy2);
        Mockito.doReturn(policies).doThrow(new IllegalArgumentException()).when(adminService).getApplicationPolicies();

        Response response = policiesApiService.policiesThrottlingApplicationGet(null, null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingApplicationPolicyIdDeleteTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        String uuid = UUID.randomUUID().toString();

        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(adminService)
                .deletePolicyByUuid(uuid, APIMgtAdminService.PolicyLevel.application);

        Response response = policiesApiService.policiesThrottlingApplicationIdDelete(uuid, null, null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingApplicationPolicyIdGetTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        String uuid = UUID.randomUUID().toString();
        ApplicationPolicy policy = new ApplicationPolicy(uuid, "SampleApplicationPolicy");

        Mockito.doReturn(policy).doThrow(new IllegalArgumentException()).when(adminService)
                .getApplicationPolicyByUuid(uuid);

        Response response = policiesApiService.policiesThrottlingApplicationIdGet(uuid, null, null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingApplicationPolicyIdPutTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        String uuid = UUID.randomUUID().toString();
        ApplicationThrottlePolicyDTO dto = new ApplicationThrottlePolicyDTO();

        ApplicationPolicy policy = ApplicationThrottlePolicyMappingUtil.fromApplicationThrottlePolicyDTOToModel(dto);

        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(adminService)
                .updateApplicationPolicy(policy);
        Mockito.doReturn(policy).doThrow(new IllegalArgumentException()).when(adminService)
                .getApplicationPolicyByUuid(uuid);

        Response response = policiesApiService.policiesThrottlingApplicationIdPut(uuid, dto, null, null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingCustomGetTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        CustomPolicy policy1 = new CustomPolicy(UUID.randomUUID().toString(), "SamplePolicy1");
        CustomPolicy policy2 = new CustomPolicy(UUID.randomUUID().toString(), "SamplePolicy2");
        List<CustomPolicy> policies = new ArrayList<>();
        policies.add(policy1);
        policies.add(policy2);

        Mockito.doReturn(policies).doThrow(new IllegalArgumentException()).when(adminService).getCustomRules();


        Response response = policiesApiService.policiesThrottlingCustomGet(null,
                                                                           null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingCustomPostTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        CustomRuleDTO dto = new CustomRuleDTO();
        String uuid = UUID.randomUUID().toString();
        dto.setId(uuid);

        CustomPolicy policy = CustomPolicyMappingUtil.fromCustomPolicyDTOToModel(dto);

        Mockito.when(adminService.addCustomRule(Mockito.any(CustomPolicy.class))).thenReturn(uuid);
        Mockito.when(adminService.getCustomRuleByUUID(uuid)).thenReturn(policy);
        Response response = policiesApiService.policiesThrottlingCustomPost(dto, getRequest());
        Assert.assertEquals(201, response.getStatus());
    }

    @Test
    public void policiesThrottlingCustomRuleIdDeleteTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        String uuid = UUID.randomUUID().toString();

        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(adminService).deleteCustomRule(uuid);

        Response response = policiesApiService.policiesThrottlingCustomRuleIdDelete(uuid, null, null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingCustomRuleIdGetTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        String uuid = UUID.randomUUID().toString();
        CustomPolicy policy = new CustomPolicy(uuid, "Policy");

        Mockito.doReturn(policy).doThrow(new IllegalArgumentException()).when(adminService).getCustomRuleByUUID(uuid);

        Response response = policiesApiService.policiesThrottlingCustomRuleIdGet(uuid, null,
                                                                                 null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingCustomRuleIdPutTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        String uuid = UUID.randomUUID().toString();
        CustomPolicy policy = new CustomPolicy(uuid, "Policy");
        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(adminService).updateCustomRule(policy);
        Mockito.doReturn(policy).doThrow(new IllegalArgumentException()).when(adminService).getCustomRuleByUUID(uuid);
        CustomRuleDTO dto = CustomPolicyMappingUtil.fromCustomPolicyToDTO(policy);
        Response response = policiesApiService.policiesThrottlingCustomRuleIdPut(uuid, dto, null, null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingApplicationPostTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        ApplicationThrottlePolicyDTO dto = new ApplicationThrottlePolicyDTO();
        String uuid = UUID.randomUUID().toString();
        dto.setId(uuid);
        dto.setPolicyName("SamplePolicy");
        dto.setDisplayName("DisplayName");
        dto.setDescription("Policy Description");
        dto.setIsDeployed(true);
        ApplicationPolicy policy = ApplicationThrottlePolicyMappingUtil.fromApplicationThrottlePolicyDTOToModel(dto);
        Mockito.when(adminService.addApplicationPolicy(Mockito.any(ApplicationPolicy.class))).thenReturn(uuid);
        Mockito.when(adminService.getApplicationPolicyByUuid(uuid)).thenReturn(policy);

        Response response = policiesApiService.policiesThrottlingApplicationPost(dto, getRequest());
        Assert.assertEquals(201, response.getStatus());
    }

    @Test
    public void policiesThrottlingSubscriptionGetTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        List<SubscriptionPolicy> policies = new ArrayList<>();
        policies.add(new SubscriptionPolicy("Policy1"));
        policies.add(new SubscriptionPolicy("Policy2"));

        Mockito.doReturn(policies).doThrow(new IllegalArgumentException()).when(adminService).getSubscriptionPolicies();

        Response response = policiesApiService.policiesThrottlingSubscriptionGet(null, null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingSubscriptionPolicyIdDeleteTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        String uuid = UUID.randomUUID().toString();

        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(adminService)
                .deletePolicyByUuid(uuid, APIMgtAdminService.PolicyLevel.subscription);

        Response response = policiesApiService.policiesThrottlingSubscriptionIdDelete(uuid, null, null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingSubscriptionPolicyIdGetTest() throws Exception  {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        String uuid = UUID.randomUUID().toString();
        SubscriptionPolicy policy = new SubscriptionPolicy(uuid, "Policy");
        Mockito.doReturn(policy).doThrow(new IllegalArgumentException()).when(adminService)
                .getSubscriptionPolicyByUuid(uuid);

        Response response = policiesApiService.policiesThrottlingSubscriptionIdGet(uuid, null, null, getRequest());
        Assert.assertEquals(response.getStatus(), 200);
    }

    @Test
    public void policiesThrottlingSubscriptionPolicyIdPutTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        String uuid = UUID.randomUUID().toString();
        SubscriptionThrottlePolicyDTO dto = new SubscriptionThrottlePolicyDTO();
        dto.setRateLimitTimeUnit("m");
        dto.setRateLimitCount(1);
        dto.setStopOnQuotaReach(true);

        SubscriptionPolicy policy = SubscriptionThrottlePolicyMappingUtil.fromSubscriptionThrottlePolicyDTOToModel(dto);

        Mockito.doNothing().doThrow(new IllegalArgumentException()).when(adminService)
                .updateSubscriptionPolicy(policy);
        Mockito.doReturn(policy).doThrow(new IllegalArgumentException()).when(adminService)
                .getSubscriptionPolicyByUuid(uuid);

        Response response = policiesApiService.policiesThrottlingSubscriptionIdPut(uuid, dto, null, null, getRequest());
        Assert.assertEquals(201, response.getStatus());
    }

    @Test
    public void policiesThrottlingSubscriptionPostTest() throws Exception {
        printTestMethodName();
        APIMgtAdminService adminService = Mockito.mock(APIMgtAdminService.class);
        PoliciesApiServiceImpl policiesApiService = new PoliciesApiServiceImpl(adminService);
        String uuid = UUID.randomUUID().toString();
        SubscriptionThrottlePolicyDTO dto = new SubscriptionThrottlePolicyDTO();
        dto.setRateLimitTimeUnit("m");
        dto.setRateLimitCount(1);
        dto.setStopOnQuotaReach(true);
        SubscriptionPolicy policy = SubscriptionThrottlePolicyMappingUtil.fromSubscriptionThrottlePolicyDTOToModel(dto);

        Mockito.when(adminService.addSubscriptionPolicy(Mockito.any(SubscriptionPolicy.class))).thenReturn(uuid);
        Mockito.when(adminService.getSubscriptionPolicyByUuid(uuid)).thenReturn(policy);

        Response response = policiesApiService.policiesThrottlingSubscriptionPost(dto, getRequest());
        Assert.assertEquals(201, response.getStatus());
    }

    private Request getRequest() {
        return Mockito.mock(Request.class);
    }

    private static void printTestMethodName() {
        logger.info("------------------ Test method: " + Thread.currentThread().getStackTrace()[2].getMethodName() +
                    " ------------------");
    }
}
