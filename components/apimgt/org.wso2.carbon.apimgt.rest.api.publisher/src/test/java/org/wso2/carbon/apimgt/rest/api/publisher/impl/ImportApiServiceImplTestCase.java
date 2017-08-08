package org.wso2.carbon.apimgt.rest.api.publisher.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.apimgt.core.api.APIPublisher;
import org.wso2.carbon.apimgt.core.impl.APIPublisherImpl;
import org.wso2.carbon.apimgt.rest.api.publisher.utils.RestAPIPublisherUtil;
import org.wso2.carbon.messaging.CarbonMessage;
import org.wso2.msf4j.Request;

import javax.ws.rs.core.Response;

import java.io.File;
import java.io.FileInputStream;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RestAPIPublisherUtil.class)
public class ImportApiServiceImplTestCase {

    private static final Logger log = LoggerFactory.getLogger(ImportApiServiceImplTestCase.class);
    private static final String USER = "admin";

    @Test
    public void testImportApisPostError() throws Exception {
        printTestMethodName();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("swagger.json").getFile());
        FileInputStream fis = null;
        fis = new FileInputStream(file);
        ImportApiServiceImpl importApiService = new ImportApiServiceImpl();
        APIPublisher apiPublisher = Mockito.mock(APIPublisherImpl.class);
        PowerMockito.mockStatic(RestAPIPublisherUtil.class);
        PowerMockito.when(RestAPIPublisherUtil.getApiPublisher(USER)).
                thenReturn(apiPublisher);
        Response response = importApiService.
                importApisPost(fis, null, null, null, getRequest());
        assertEquals(response.getStatus(), 500);
        assertTrue(response.getEntity().toString().contains("Error while importing the given APIs"));
    }

    @Test
    public void testImportApisPutError() throws Exception {
        printTestMethodName();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("swagger.json").getFile());
        FileInputStream fis = null;
        fis = new FileInputStream(file);
        ImportApiServiceImpl importApiService = new ImportApiServiceImpl();
        APIPublisher apiPublisher = Mockito.mock(APIPublisherImpl.class);
        PowerMockito.mockStatic(RestAPIPublisherUtil.class);
        PowerMockito.when(RestAPIPublisherUtil.getApiPublisher(USER)).
                thenReturn(apiPublisher);
        Response response = importApiService.
                importApisPut(fis, null, null, null, getRequest());
        assertEquals(response.getStatus(), 500);
        assertTrue(response.getEntity().toString().contains("Error while importing the given APIs"));
    }


    // Sample request to be used by tests
    private Request getRequest() throws Exception {
        CarbonMessage carbonMessage = Mockito.mock(CarbonMessage.class);
        Request request = new Request(carbonMessage);
        PowerMockito.whenNew(Request.class).withArguments(carbonMessage).thenReturn(request);
        return request;
    }

    private static void printTestMethodName () {
        log.info("------------------ Test method: " + Thread.currentThread().getStackTrace()[2].getMethodName() +
                " ------------------");
    }
}
