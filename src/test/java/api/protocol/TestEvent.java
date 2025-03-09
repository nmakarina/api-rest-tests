package api.protocol;

import api.BaseApiTest;
import enums.Method;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import meta.API_TestListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


@Listeners({API_TestListener.class})
@Feature("Api событий")
public class TestEvent extends BaseApiTest {


    @Test(priority = 113, description = "Экспорт событий", dataProvider = "exportFormats")
    @TmsLink("#/api-event-controller/exportTable_1")
    public void exportTable_1(String format) {
        String body = "{\n" +
                "    \"filter\": {\n" +
                "       \"page\": \"0\",\n" +
                "       \"limit\": \"25\",\n" +
                "       \"startDate\": \"2023-10-20T00:00:00.000Z\",\n" +
                "       \"endDate\": \"2024-02-01T23:59:59.000Z\",\n" +
                "       \"sort\": \"-date\"\n" +
                "    },\n" +
                "    \"selectedIds\": []\n" +
                "}";
        String serverFileName = "Протоколирование." + format;
        String url = "/api/v1/protocols/backups/2/export/";

        byte[] downloadedFile = getResponse(
                getRequestTo(Method.POST)
                        .withPath(url + format)
                        .withParams("offsetInMinutes", "60")
                        .withParams("columns", "status,date,eventTypeName,objectTypeName,object,user,comment,ip")
                        .withBody(body)
        )[1].getBytes();

        System.out.println(serverFileName + " fileSize: " + Math.round(downloadedFile.length / 1024.0) + "KB");
        Assert.assertTrue(downloadedFile.length > 0, "Файл " + serverFileName + " не был загружен");
    }
}
