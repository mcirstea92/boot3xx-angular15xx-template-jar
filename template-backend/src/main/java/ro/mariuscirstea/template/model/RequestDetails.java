package ro.mariuscirstea.template.model;

import ro.mariuscirstea.template.entity.USR_User;

public class RequestDetails {

    private USR_User user;
    private String ip;
    private String userAgent;

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public USR_User getUser() {
        return user;
    }

    public void setUser(USR_User user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
