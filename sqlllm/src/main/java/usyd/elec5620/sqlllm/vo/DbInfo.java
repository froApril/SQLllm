package usyd.elec5620.sqlllm.vo;

import lombok.Data;

@Data
public class DbInfo {
    private String ip;
    private String port;
    private String dbName;
    private String driveClassName;
    private String username;
    private String password;

    public DbInfo(String ip, String port, String dbName, String driveClassName, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.dbName = dbName;
        this.driveClassName = driveClassName;
        this.username = username;
        this.password = password;
    }
}
