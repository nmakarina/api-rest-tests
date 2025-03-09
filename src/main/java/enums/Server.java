package enums;

public enum Server {
    DEV("http://10.10.17.10"),
    DEMO("https://emex.ru");

    private String path;

    Server(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }


    public String getLoginPath() {
        return "/api/account/login";
    }
    public String getBasketPath() {
        return "/api/basket/basket";
    }
    public String getInmotionPath() {
        return "/inmotion";
    }
    public String getVersionPath() {
        return "/api/home/version";
    }

    public String getUserData() {
        return "/api/user/data";
    }


}
