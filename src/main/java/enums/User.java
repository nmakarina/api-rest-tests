package enums;

public enum User {
    USER("not_set", "not_set"),
    ADM_USER("adm", "adm");

    private String password;
    private String login;


    User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getPassword() {
        String pass = System.getProperty("AT_"+User.this.name()+"_PASS");
        if (pass == null || pass.isEmpty()) {
            pass = System.getenv("AT_"+User.this.name()+"_PASS");
            if (pass == null || pass.isEmpty()) {
                return password;
            }
        }
        return pass;
    }

    public String getLogin() {
        String lgn = System.getProperty("AT_"+User.this.name()+"_LOGIN");
        if (lgn == null || lgn.isEmpty()) {
            lgn = System.getenv("AT_"+User.this.name()+"_LOGIN");
            if (lgn == null || lgn.isEmpty()) {
                return login;
            }
        }
        return lgn;
    }

}
