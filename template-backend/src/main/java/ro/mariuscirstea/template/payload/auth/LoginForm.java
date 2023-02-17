package ro.mariuscirstea.template.payload.auth;

public class LoginForm {

    private String user;

    private String password;

    public LoginForm() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
