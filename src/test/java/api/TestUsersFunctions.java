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

@Listeners({API_TestListener.class})
@Feature("Проверка функций пользователя")
public class TestUsersFunctions extends BaseApiTest {
    @Test(description = "Получение содержимого корзины")
    public void getBasket() {
        String url = Utility.getServer().getBasketPath();
        String body =
                "[]";
        String[] response = getResponse(
                getRequestTo(Method.POST)
                        .withPath(url)
                        .withBody(body)
        );
        System.out.println(response[0] + " ---- >" +response[1]);
        String name = JsonPath.from(response[1]).getString("location.name");
        Assert.assertTrue(response[0].equals("200"),"Код ответа = 200");
        Assert.assertTrue(name.contains("EMEX"),"Ответ содержит название офиса");
    }

    @Test(description = "Получение заказов")
    public void getInmotion() {
        String url = Utility.getServer().getInmotionPath();

        //String[] response = getResponse(
                Response response = getRequestTo(Method.GET)
                        .withPath(url)
                        .withHeader("Accept-Encoding","gzip, deflate, br, zstd")
                        .withHeader("Content-Type","text/html; charset=utf-8")
                .send();
      //  );
        System.out.println(response.statusCode() + " ---- >" +response.getStatusLine());
        Assert.assertTrue(response.statusCode()==200,"Код ответа = 200");
    }

}
