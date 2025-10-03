package com.datocms;

import com.datocms.dto.GetDataRequestDTO;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DatoCMSClientTest {

    private static final String TEST_API_KEY = "your-api-key";
    private static final String TEST_ENVIRONMENT = "main";

    @Test
    void testGetDataApiCall() throws IOException, InterruptedException {
        DatoCMSClient client = new DatoCMSClient(TEST_API_KEY, TEST_ENVIRONMENT);

        GetDataRequestDTO labelsDTO = new GetDataRequestDTO(
                "test",
                "your-query",
                Map.of("var", "123456789")
        );

        Object result = client.getData(labelsDTO, Object.class);
        assertNotNull(result);
    }
}