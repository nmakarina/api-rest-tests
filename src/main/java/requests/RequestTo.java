package requests;

import enums.Method;
import io.qameta.allure.Step;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public final class RequestTo implements Cloneable {
    private String URL, server, path = "";
    private String body = "", cookie = "", content = "application/json", multiPart = "";
    private HashMap<String, String> cookies;
    private Map<String, String> params, headers, formParams;
    private Method method = Method.POST;
    private Headers responseHeaders;

    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";
    private String contentType = "application/json";

    public RequestTo(String URL) {
        this.URL = URL;
        cookies = new HashMap<>();
        params = new HashMap<>();
        headers = new HashMap<>();
        formParams = new HashMap<>();
        server = "";
    }

    public Headers getResponseHeaders() {
        return responseHeaders;
    }

    @Step("Добавить cookie {0}")
    public RequestTo withCookie(String cookie) {
        this.cookie = cookie;
        return this;
    }

    @Step("Добавить cookie с название {0} и значением {1}")
    public RequestTo withExactCookie(String key, String value) {
        cookies.put(key, value);
        return this;
    }

    @Step("Добавить тело запроса")
    public RequestTo withBody(String body) {
        this.body = body;
        return this;
    }

    @Step("Указать основной сервер {0}")
    public RequestTo withServer(String serverUrl) {
        server = serverUrl;
        return this;
    }

    @Step("Добавить путь {0}")
    public RequestTo withPath(String path) {
        this.path = path;
        return this;
    }

    @Step("Указать тип контента {0}")
    public RequestTo withContentType(String content) {
        this.content = content;
        return this;
    }

    @Step("Указать тип запроса {0}")
    public RequestTo withMethod(Method method) {
        this.method = method;
        return this;
    }

    @Step("Добавить заголовок {0} со значением {1}")
    public RequestTo withHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    @Step("Добавить параметр {0} со значением {1}")
    public RequestTo withParams(String par, String value) {
        params.put(par, value);
        return this;
    }

    @Step("Добавить параметры")
    public RequestTo withParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Step("Добавить параметры формы")
    public RequestTo withFormParams(Map<String, String> params) {
        this.formParams = params;
        return this;
    }

    @Step("Добавить мультипараметры формы")
    public RequestTo withMultiPart(String content) {
        this.multiPart = content;
        return this;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getBody() {
        return body;
    }

    @Override
    public RequestTo clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new RequestTo(URL).withMethod(method).withCookie(cookie).withBody(body).withPath(path).withServer(server).withParams(params);
    }

    @Step("Отправить запрос")
    public Response send() {
        String[] result = new String[2];
        result[0] = "0";
        result[1] = "No result!";

        RequestSpecification rs = given().
                header("Content-Type", contentType).
                header("User-Agent", userAgent).
                relaxedHTTPSValidation().
                log().
                all();

        if (!params.isEmpty()) {
            rs.queryParams(params);
        }
        if (!formParams.isEmpty()) {
            rs.formParams(formParams);
        }
        if (!cookie.isEmpty()) {
            rs.cookie(cookie);
        }
        if (!cookies.isEmpty()) {
            rs.cookies(cookies);
        }
        if (!headers.isEmpty()) {
            rs.headers(headers);
        }
        if (!body.isEmpty()) {
            rs.body(body);
        }
        if (!multiPart.isEmpty()) {
            if (multiPart.contains(".png")) {
                try {
                    rs.multiPart("file", new File(multiPart).getName(), Files.readAllBytes(new File(multiPart).toPath()), "image/png");
                    //rs.multiPart("id", "blob", "application/json");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (multiPart.contains(".xlsx")) {
                try {
                    rs.multiPart("file", new File(multiPart).getName(), Files.readAllBytes(new File(multiPart).toPath()), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                rs.multiPart("file", "application/octet-stream", multiPart.getBytes());
            }
        }
        Response response = null;
        switch (method) {
            case GET:
                response = rs.when().get(server + URL + path);
                break;
            case DELETE:
                response = rs.when().delete(server + URL + path);
                break;
            case POST:
                response = rs.when().post(server + URL + path);
                break;
            case PUT:
                response = rs.when().put(server + URL + path);
                break;
        }
        responseHeaders = response.headers();
        return  response;
    }

    @Override
    public String toString() {
        return "RequestTo {" +
                "URL='" + URL + path + '\'' +
                ", body='" + body + '\'' +
                ", method=" + method +
                '}';
    }
}
