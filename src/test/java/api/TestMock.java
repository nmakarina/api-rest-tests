package api;

import com.github.tomakehurst.wiremock.WireMockServer;
import enums.Method;
import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import meta.API_TestListener;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.Utility;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Listeners({API_TestListener.class})
@Feature("Моки")
public class TestMock  extends BaseApiTest {
    private WireMockServer wireMockServer;
    @BeforeMethod
    public void setup() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();

        wireMockServer.stubFor(post(urlEqualTo(Utility.getServer().getUserData()))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "    \"userId\": 111111,\n" +
                                "    \"name\": \"Иван\",\n" +
                                "    \"surname\": \"Литвинов\",\n" +
                                "    \"phone\": \"79999999999\",\n" +
                                "    \"formattedPhone\": \"+7 (999) 999-99-99\",\n" +
                                "    \"email\": \"ivan999@mail.com\",\n" +
                                "    \"userType\": \"Potr\",\n" +
                                "    \"locationId\": 999,\n" +
                                "    \"features\": [\n" +
                                "        \n" +
                                "    ],\n" +
                                "    \"optovikLogo\": null,\n" +
                                "    \"hasPassword\": false\n" +
                                "}")));

    }

    @Test
    public void testWithMockedApi() {
        // Call available API
        String url = Utility.getServer().getVersionPath();
        String body =
                "{\n" +
                        "    \"url\": \"/\"\n" +
                        "}";
        String[] response = getResponse(
                getRequestTo(Method.POST)
                        .withPath(url)
                        .withBody(body)
        );
        System.out.println(response[0] + " ---- >" +response[1]);
        String version = JsonPath.from(response[1]).getString("version");
        Assert.assertTrue(response[0].equals("200"),"Код ответа = 200");
        Assert.assertTrue(version.contains("1") || version.contains("2"),"Ответ содержит номер версии");

        // Pass response to mocked API
        url = Utility.getServer().getUserData();
        String[] responseUserData = getResponse(
                getRequestTo(Method.POST, "http://localhost:8080")
                        .withPath(url)
                        .withBody("{\n" +
                                "    \"ver\": \""+version+"\"\n" +
                                "}")
        );

        // Verify mock response
        System.out.println(responseUserData[0] + " ---- >" +responseUserData[1]);
        String userId = JsonPath.from(responseUserData[1]).getString("userId");
        Assert.assertTrue(responseUserData[0].equals("200"),"Код ответа = 200");
        Assert.assertFalse(userId.isEmpty(),"Получен userId");
    }

    @AfterClass
    public void teardown() {
        wireMockServer.stop();
    }

}
