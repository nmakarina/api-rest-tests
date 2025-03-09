package api;

import enums.Method;
import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import meta.API_TestListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.Utility;

import java.util.Date;

@Listeners({API_TestListener.class})
@Feature("Логин")
public class TestLogin extends BaseApiTest {
    @Test(priority = 24, description = "Авторизация с неверным паролем")
    public void userTokenGenerate() {
        String url = Utility.getServer().getLoginPath();
        Date dt = new Date();
        String body =
        "{\n" +
                "    \"login\": \"" + Utility.getUser().getLogin() + "\",\n" +
                "    \"password\": \"wrong_pass\",\n" +
                "    \"t\": "+dt.getTime()+"\n" +
                "}";
        Response response = getRequestTo(Method.POST)
                .withPath(url)
                .withBody(body)
                .send();
        System.out.println(response.statusCode()+"  ---->  "+response.getStatusLine());
        Assert.assertTrue(response.statusCode()==500, "Код ответа = 500");
        Assert.assertTrue(response.getBody().prettyPrint().contains("Неверный логин или пароль"), "Неверный логин или пароль");
    }


    @Test(description = "Получение версии сайта")
    public void getVersion() {
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
    }
}
