package com.github.thestyleofme.demo.pojo;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/10/26 15:28
 * @since 1.0.0
 */
public class MysqlDatasource {

    private String url;
    private String username;
    private String password;
    private String driver;
    private String defaultSchema;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDefaultSchema() {
        return defaultSchema;
    }

    public void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }
}
