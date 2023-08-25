package usyd.elec5620.sqlllm.util;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.util.StringUtils;
import usyd.elec5620.sqlllm.config.DynamicDataSource;
import usyd.elec5620.sqlllm.context.SpringContextHolder;
import usyd.elec5620.sqlllm.vo.DbInfo;

import javax.sql.DataSource;

public class DataSourceUtil {


    public static DataSource makeNewDataSource(DbInfo dbInfo) {
        String url = "jdbc:mysql://"+dbInfo.getIp() + ":"+dbInfo.getPort()+"/"+dbInfo.getDbName()
                +"?characterEncoding=UTF-8";
        String driveClassName = dbInfo.getDriveClassName() == null || dbInfo.getDriveClassName().equals("") ? "com.mysql.cj.jdbc.Driver":dbInfo.getDriveClassName();
        return DataSourceBuilder.create().url(url)
                .driverClassName(driveClassName)
                .username(dbInfo.getUsername())
                .password(dbInfo.getPassword())
                .build();
    }

    public static void addDataSourceToDynamic(String key, DataSource dataSource) {
        DynamicDataSource dynamicDataSource = SpringContextHolder.getContext().getBean(DynamicDataSource.class);
        dynamicDataSource.addDataSource(key, dataSource);
    }

    public static void addDataSourceToDynamic(String key, DbInfo dbInfo) throws Exception{
        DataSource dataSource = checkDbInfo(dbInfo);
        addDataSourceToDynamic(key, dataSource);
    }

    public static DataSource checkDbInfo(DbInfo dbInfo) throws Exception{
        DataSource dataSource = makeNewDataSource(dbInfo);
        dataSource.getConnection();
        return dataSource;
    }

}
