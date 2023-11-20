package com.example.sneaker_sophia.controller.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ghn-api")
public class GHNApiController {

    private static final String GHN_API_BASE_URL = "https://online-gateway.ghn.vn/shiip/public-api";
    private static final String TOKEN = "3097cfa7-8529-11ee-96dc-de6f804954c9";
    private static final int SHOP_ID = 4704366;

    @GetMapping("/getProvinceData")
    public void getProvinceData() {
        String apiUrl = GHN_API_BASE_URL + "/master-data/province";
        callApi(apiUrl);
    }

    @GetMapping("/getDistrictData")
    public void getDistrictData(@RequestParam int provinceId) {
        String apiUrl = GHN_API_BASE_URL + "/master-data/district";
        apiUrl += "?province_id=" + provinceId;
        callApi(apiUrl);
    }

    @GetMapping("/getWardData")
    public void getWardData(@RequestParam int districtId) {
        String apiUrl = GHN_API_BASE_URL + "/master-data/ward";
        apiUrl += "?district_id=" + districtId;
        callApi(apiUrl);
    }
    @GetMapping("/calculateShippingFee")
    public ResponseEntity<Map<String, Object>> calculateShippingFee(@RequestParam int serviceId) {
        String apiUrl = GHN_API_BASE_URL + "/v2/shipping-order/fee";
        apiUrl += "?service_id=" + serviceId;
        apiUrl += "&shop_id=" + SHOP_ID;

        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(apiUrl);
            httpPost.addHeader("token", TOKEN);

            HttpResponse response = httpClient.execute(httpPost);

            // Extract the response content as a string
            String responseBody = EntityUtils.toString(response.getEntity());

            // Parse the response body
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseData = objectMapper.readValue(responseBody, Map.class);

            // Create a JSON object to wrap the 'fee' property
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("fee", responseData.get("fee"));

            // Return the response body as JSON
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Internal Server Error"));
        }
    }



    @GetMapping("/getAvailableServices")
    public void getAvailableServices(@RequestParam int fromDistrictId, @RequestParam int toDistrictId) {
        String apiUrl = GHN_API_BASE_URL + "/v2/shipping-order/available-services";
        apiUrl += "?shop_id=" + SHOP_ID;
        apiUrl += "&from_district=" + fromDistrictId;
        apiUrl += "&to_district=" + toDistrictId;
        callApi(apiUrl);
    }

//    @GetMapping("/calculateShippingFee")
//    public ResponseEntity<String> calculateShippingFee(@RequestParam int serviceId) {
//        String apiUrl = GHN_API_BASE_URL + "/v2/shipping-order/fee";
//        apiUrl += "?service_id=" + serviceId;
//        apiUrl += "&shop_id=" + SHOP_ID;
//
//        try {
//            HttpClient httpClient = HttpClients.createDefault();
//            HttpPost httpPost = new HttpPost(apiUrl);
//            httpPost.addHeader("token", TOKEN);
//
//            HttpResponse response = httpClient.execute(httpPost);
//
//            // Extract the response content as a string
//            String responseBody = EntityUtils.toString(response.getEntity());
//
//            // Return the response body as JSON
//            return ResponseEntity.ok(responseBody);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Internal Server Error\"}");
//        }
//    }


    private void callApi(String apiUrl) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(apiUrl);
            httpPost.addHeader("token", TOKEN);

            HttpResponse response = httpClient.execute(httpPost);

            // Xử lý phản hồi từ API ở đây

            System.out.println("API Response Code: " + response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

