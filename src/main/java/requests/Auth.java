package requests;

import io.qameta.allure.Step;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import utils.Utility;

import java.util.Date;
import java.util.Map;

import static io.restassured.RestAssured.given;

public final class Auth {
    private final String server;

    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36";
    private String contentType = "application/json";

    public Map<String, String> cookies;
    public Headers headers;
    public Response loginResponse;


    public Auth() {
        System.out.println("AUTHORIZE for " + Utility.getUser().getLogin() + " on server " + Utility.getServer());
        server = Utility.getServer().getLoginPath();
        //getCookies();
        //getHeaders();
    }
    @Step("Получить cookie")
    public Map<String, String> getCookies() {
        Date dt = new Date();
        String body =
                "{\n" +
                        "    \"login\": \"" + Utility.getUser().getLogin() + "\",\n" +
                        "    \"password\": \"" + Utility.getUser().getPassword() + "\",\n" +
                        "    \"t\": "+dt.getTime()+"\n" +
                        "}";
        RequestSpecification rs = given()
                .header("Content-Type", contentType)
                .header("User-Agent", userAgent)
                .body(body);

        loginResponse = rs.when().log().all().post(server).andReturn();
        System.out.println(loginResponse.statusCode());
        System.out.println(loginResponse.getStatusLine());

        Assert.assertTrue(loginResponse.statusCode() == 200, "Авторизация не выполнена");

        cookies = loginResponse.
                then().log().all().
                extract().cookies();

        System.out.println("GET COOKIES");
        cookies.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "=" + e.getValue()));
        return cookies;
    }
    @Step("Получить заголовки")
    public Headers getHeaders() {
        String body = "{ \n\"username\": \"" + Utility.getUser().getLogin() + "\",\n"
                + "\"password\": \"" + Utility.getUser().getPassword() + "\"\n}";

        Assert.assertTrue(loginResponse.statusCode() == 200, "Авторизация не выполнена");

        headers = loginResponse.
                then().log().all().
                extract().headers();

        System.out.println("GET HEADERS");
        headers.asList().stream().forEach(e -> System.out.println(e.getName() + "=" + e.getValue()));
        return headers;
    }
}
