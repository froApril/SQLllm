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
}
