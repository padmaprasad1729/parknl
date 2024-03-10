package com.infosys.parknl;

import com.infosys.parknl.conf.ParkNLConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation .class)
public class StreetControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @LocalServerPort
    int port;

    @BeforeEach
    public void init() {
        RestAssured.port = port;
        System.out.println("RestAssured.baseURI = " + RestAssured.baseURI);
        System.out.println("RestAssured.port = " + RestAssured.port);
    }

    @Test
    @Order(1)
    public void given_GETWithParam_NoMatchingStreet() {
        String streetName = "TestStreet";
        given()
                .when()
                .get(ParkNLConstants.API_VERSION_V1 + "/streets/" + streetName)
                .then()
                .log().ifValidationFails()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    @Order(2)
    public void given_AddStreet() {
        String streetName = "TestStreet";
        String requestJson = "{\n" +
                "    \"streetName\": \"TestStreet\",\n" +
                "    \"priceInCentPerMinute\": 15\n" +
                "}";
        String responseJson = "{\n" +
                "    \"streetName\": \"TestStreet\",\n" +
                "    \"priceInCentPerMinute\": 15\n" +
                "}";

        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.contentType(ContentType.JSON);
        httpRequest.body(requestJson);
        Response response = httpRequest.post(ParkNLConstants.API_VERSION_V1 + "/streets");

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(response.body().asPrettyString(), responseJson);
    }

    @Test
    @Order(3)
    public void given_GetWithParamMatchingStreet() {

        String streetName = "TestStreet";
        given()
                .when()
                .get(ParkNLConstants.API_VERSION_V1 + "/streets/" + streetName)
                .then()
                .log().ifValidationFails()
                .statusCode(OK.value())
                .extract().body().asPrettyString().equals("{\n" +
                        "    \"streetName\": \"TestStreet\",\n" +
                        "    \"priceInCentPerMinute\": 15\n" +
                        "}");
    }

    @Test
    @Order(4)
    public void given_RemoveStreet() {

        String streetName = "TestStreet";
        given()
                .when()
                .delete(ParkNLConstants.API_VERSION_V1 + "/streets/" + streetName)
                .then()
                .log().ifValidationFails()
                .statusCode(OK.value());
    }
}
