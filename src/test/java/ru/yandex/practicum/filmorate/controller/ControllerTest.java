package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.model.FilmorateObject;
import ru.yandex.practicum.filmorate.tools.GsonAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
abstract class ControllerTest<IController> {
    static String endPoint;
    static Controller controller;
    static List<TestData> testDataWithErrForCreate = new ArrayList<>();
    static List<TestData> testDataWithErrForUpdate = new ArrayList<>();
    static List<TestData> testDataCorrectForCreate = new ArrayList<>();
    static List<TestData> testDataCorrectForUpdate = new ArrayList<>();
    @Autowired
    protected TestRestTemplate restTemplate;
    @Test
    @Order(1)
    void createWithCorrectData() {
        for (TestData testData : testDataCorrectForCreate) {

            ResponseEntity<String> response = restTemplate.postForEntity(endPoint, testData.getObj(), String.class);
            assertEquals(testData.getResponseStatusCode(), response.getStatusCode());

            Gson gson = GsonAdapter.getGsonWithAdapter();

            FilmorateObject updatedObject = gson.fromJson(response.getBody(), (Type) testData.getType());
            assertNotNull(updatedObject);
            assertEquals(testData.getExpectedObj().getId(), updatedObject.getId());
            assertEquals(testData.getExpectedObj(), updatedObject);
        }
    }

    @Test
    @Order(2)
    void createWrongData() {
        for (TestData testData : testDataWithErrForCreate) {
            ResponseEntity<String> response = restTemplate.postForEntity(endPoint, testData.getObj(), String.class);
            assertEquals(testData.getResponseStatusCode(), response.getStatusCode());
            assertEquals(testData.getExpectedBody(), response.getBody());
        }
    }

    @Test
    @Order(3)
    void updateCorrectData() {
        for (TestData testData : testDataCorrectForUpdate) {
            HttpEntity<FilmorateObject> entity = new HttpEntity<>(testData.getObj());
            ResponseEntity<String> response = restTemplate.exchange(endPoint, HttpMethod.PUT, entity, String.class);
            assertEquals(testData.getResponseStatusCode(), response.getStatusCode());

            Gson gson = GsonAdapter.getGsonWithAdapter();

            FilmorateObject updatedObject = gson.fromJson(response.getBody(), (Type) testData.getType());
            assertNotNull(updatedObject);
            assertEquals(testData.getExpectedObj().getId(), updatedObject.getId());
            assertEquals(testData.getExpectedObj(), updatedObject);
        }
    }

    @Test
    @Order(4)
    void updateWrongData() {
        for (TestData testData : testDataWithErrForUpdate) {
            HttpEntity<FilmorateObject> entity = new HttpEntity<>(testData.getObj());
            ResponseEntity<String> response = restTemplate.exchange(endPoint, HttpMethod.PUT, entity, String.class);
            assertEquals(testData.getResponseStatusCode(), response.getStatusCode());
        }
    }

}
