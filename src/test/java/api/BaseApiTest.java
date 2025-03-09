package api;

import enums.Method;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import meta.TestFlow;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.asserts.SoftAssert;
import requests.Auth;
import requests.RequestTo;
import utils.Utility;

import java.time.format.DateTimeFormatter;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseApiTest extends TestFlow {
    private static Auth auth;
    private static TreeMap<String, String> cookie = new TreeMap<String, String>();
    private static Headers headers;
    private static RequestTo requestTo;

    public DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyyMMdd");
    public DateTimeFormatter ymds = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static SoftAssert softAssert;

    @DataProvider
    public Object[] exportFormats(){
        return Stream.of("pdf", "xlsx").toArray();
    }
    @DataProvider
    public Object[] usersExportFormats(){
        return Stream.of("csv", "xlsm").toArray();
    }
    @DataProvider
    public Object[] templateFormats(){
        return Stream.of("csv", "xlsx").toArray();
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(){
        auth = new Auth();
        cookie.putAll(auth.getCookies());
        headers = auth.getHeaders();
    }

    //@BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        String apiAddress = "/token/status/";
        requestTo = new RequestTo(Utility.getServer().getPath() + apiAddress /**+ getHeader("Authorization")**/);
        requestTo.withMethod(Method.GET);
        getResponse(requestTo);
        softAssert = new SoftAssert();
    }

    @Step("Создать запрос")
    public RequestTo getRequestTo(Method method){
        requestTo = new RequestTo(Utility.getServer().getPath());
        requestTo.withMethod(method);
        requestTo.withCookie(getCookies());
        //requestTo.withHeader("Authorization", getHeader("Authorization"));
        return requestTo;
    }

    @Step("Получить cookie по названию {0}")
    public String getCookie(String name){
        return cookie.get(name);
    }

    @Step("Получить cookie")
    public String getCookies(){
        return cookie.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(";")) + ";";
    }

    @Step("Получить header по названию {0}")
    public String getHeader(String name){
        System.out.println(headers.get(name).getValue());
        return headers.get(name).getValue();
    }

    @Step("Получить headers")
    public Headers getHeaders(){
        return headers;
    }

    @Step("Получить актуальный токен")
    public String getToken(){
        System.out.println(cookie.get("Authorization"));
        return cookie.get("Authorization").replace("Bearer ", "");
    }

    @Attachment(value = "Сервер", type = "text/plain")
    public String checkServer() {
        return Utility.getServer().getPath();
    }

    @Attachment(value = "Запрос", type = "text/plain")
    public String checkRequestTo(RequestTo requestTo) {
        return requestTo.toString();
    }

    @Attachment(value = "Тело ответа", type = "text/plain")
    public String checkResponseMessage(String message) {
        return message;
    }

    protected String[] getResponse(RequestTo requestTo) {
        checkServer();
        checkRequestTo(requestTo);
        Response result = requestTo.
                send();
        checkResponseMessage("Статус-код ответа: " + result.getStatusCode() + "\n" + result.getStatusLine());
        int statusCode = result.getStatusCode();
        if (statusCode < 200 || statusCode > 202) {
            Assert.assertTrue(statusCode==200, "Response code is not 200 (success), but " + statusCode + ", and message:'" + result.getStatusLine() + "'");
        }
        if (result.getBody().prettyPrint().contains("ResponseInfo")) {
            JsonPath path = JsonPath.from(result.getBody().prettyPrint());
            Assert.assertEquals(path.getString("ResponseInfo.Status"), "0", "Status is not 0!\n"
                    + path.getString("ResponseInfo"));
        }
        String[] res = new String[2];
        res[0] = String.valueOf(result.statusCode());
        res[1] = result.getBody().prettyPrint();
        return res;
    }
}
