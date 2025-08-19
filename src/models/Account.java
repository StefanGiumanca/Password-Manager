package models;

import encryption.CryptoUtils;

public class Account {
    private String siteName;
    private String username;
    private String password;

    public Account(String siteName, String username, String password)
    {
        this.siteName = siteName;
        this.username = username;
        this.password = password;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return siteName + " | " + username + " | " + password;
    }
}
