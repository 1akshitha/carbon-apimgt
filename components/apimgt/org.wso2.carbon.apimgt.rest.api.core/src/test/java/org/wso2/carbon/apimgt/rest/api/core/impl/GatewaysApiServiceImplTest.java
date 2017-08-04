package org.wso2.carbon.apimgt.rest.api.core.impl;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.carbon.apimgt.core.dao.APISubscriptionDAO;
import org.wso2.carbon.apimgt.core.dao.ApiDAO;
import org.wso2.carbon.apimgt.core.dao.ApplicationDAO;
import org.wso2.carbon.apimgt.core.dao.LabelDAO;
import org.wso2.carbon.apimgt.core.dao.PolicyDAO;
import org.wso2.carbon.apimgt.core.impl.APIGatewayPublisherImpl;
import org.wso2.carbon.apimgt.rest.api.core.dto.LabelDTO;
import org.wso2.carbon.apimgt.rest.api.core.dto.LabelInfoDTO;
import org.wso2.carbon.apimgt.rest.api.core.dto.RegistrationDTO;
import org.wso2.msf4j.Request;

import javax.ws.rs.core.Response;

public class GatewaysApiServiceImplTest{

    APISubscriptionDAO apiSubscriptionDAO;
    PolicyDAO policyDAO;
    ApiDAO apiDAO;
    LabelDAO labelDAO;
    APIGatewayPublisherImpl  gatewayPublisherImpl;
    ApplicationDAO applicationDAO ;
    LabelInfoDTO labelinfoDTO = new LabelInfoDTO();
    RegistrationDTO regDTO = new RegistrationDTO();
    Request request;
    final String contentType= "application/json";

    @BeforeMethod
    public void setUp() throws Exception {
         apiSubscriptionDAO = Mockito.mock(APISubscriptionDAO.class);
         policyDAO = Mockito.mock(PolicyDAO.class);
         apiDAO = Mockito.mock(ApiDAO.class);
         labelDAO = Mockito.mock(LabelDAO.class);
         gatewayPublisherImpl  = Mockito.mock(APIGatewayPublisherImpl.class);
         applicationDAO = Mockito.mock(ApplicationDAO.class);

         LabelDTO labelDTO = new LabelDTO();
         labelDTO.setName("gatewayLabel1");
         labelDTO.addAccessUrlsItem("https://localhost:9292");
         labelinfoDTO.addLabelListItem(labelDTO) ;
         labelinfoDTO.setOverwriteLabels("false");
         regDTO.setLabelInfo(labelinfoDTO);

         request = Mockito.mock(Request.class);

    }

    @Test(description = "testing gateway registration")
    public void testGatewaysRegisterPost() throws Exception {
        GatewaysApiServiceImpl gatewayServiceImpl = new GatewaysApiServiceImpl(apiSubscriptionDAO, policyDAO, apiDAO,
                labelDAO, applicationDAO, gatewayPublisherImpl);
        Response response = gatewayServiceImpl.gatewaysRegisterPost(regDTO, contentType, request);
        Assert.assertEquals
                (response.getStatus(), Response.Status.OK.getStatusCode());

    }

    @Test(description = "testing gateway registration without label")
    public void testGatewaysRegisterPostWithNoLabelInfo() throws Exception {
        RegistrationDTO regDTO = Mockito.mock(RegistrationDTO.class);

        GatewaysApiServiceImpl gatewayServiceImpl = new GatewaysApiServiceImpl(null, null, null, null, null, null);
        Response response = gatewayServiceImpl.gatewaysRegisterPost(regDTO, contentType, request);
        Assert.assertEquals(response.getStatus(), 400);
    }
}