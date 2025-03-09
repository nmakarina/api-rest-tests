package requests;

import enums.Server;
import enums.User;
import io.qameta.allure.Step;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import utils.Utility;

import java.util.Map;

import static io.restassured.RestAssured.given;

public final class AuthBy {
    private final Server server;
    public final User user;

    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36";
    private String contentType = "application/json";

    public Map<String, String> cookies;
    public Headers headers;

    public AuthBy(User user) {
        System.out.println("AUTHORIZE for " + user.getLogin() + " on server " + Utility.getServer());
        this.server = Utility.getServer();
        this.user = user;
        getCookies();
        getHeaders();
    }
    @Step("Получить cookie")
    public Map<String, String> getCookies() {
        String body = "{ \n\"username\": \"" + user.getLogin() + "\",\n"
                + "\"password\": \"" + user.getPassword() + "\"\n}";

        RequestSpecification rs = given()
                .header("Content-Type", contentType)
                .header("User-Agent", userAgent)
                .body(body);

        Response response = rs.when().log().all().post(server.getLoginPath());

        Assert.assertTrue(response.statusCode() == 200, "Авторизация не выполнена");

        cookies = response.
                then().log().all().
                extract().cookies();

        System.out.println("GET COOKIES");
        cookies.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "=" + e.getValue()));
        return cookies;
    }
    @Step("Получить заголовки")
    public Headers getHeaders() {
        String body = "{ \n\"username\": \"" + user.getLogin() + "\",\n"
                + "\"password\": \"" + user.getPassword() + "\"\n}";

        RequestSpecification rs = given()
                .header("Content-Type", contentType)
                .header("User-Agent", userAgent)
                .body(body);

        Response response = rs.when().post(server.getLoginPath());

        Assert.assertTrue(response.statusCode() == 200, "Авторизация не выполнена");

        headers = response.
                then().log().all().
                extract().headers();

        System.out.println("GET HEADERS");
        headers.asList().stream().forEach(e -> System.out.println(e.getName() + "=" + e.getValue()));
        return headers;
    }
}
