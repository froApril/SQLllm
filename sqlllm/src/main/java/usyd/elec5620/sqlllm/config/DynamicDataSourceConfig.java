package usyd.elec5620.sqlllm.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import usyd.elec5620.sqlllm.constants.DataSourceConstants;
import usyd.elec5620.sqlllm.vo.DbInfo;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:config/jdbc.properties")
@MapperScan(basePackages = "usyd.elec5620.sqlllm.mapper")
public class DynamicDataSourceConfig {

    public static DbInfo userDb = new DbInfo(  "34.87.233.59",
                                                "3306",
                                                "elect5620",
                                                "com.mysql.cj.jdbc.Driver",
                                                "root",
                                                "elec5620");

    @Bean(DataSourceConstants.DS_KEY_DEFAULT)
    @ConfigurationProperties(prefix = "spring.datasource.default")
    public DataSource defaultDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DataSource dynamicDataSource() {
        Map<Object, Object> dataSourceMap = new HashMap<>(1);
        dataSourceMap.put(DataSourceConstants.DS_KEY_DEFAULT, defaultDataSource());

        return new DynamicDataSource(defaultDataSource(), dataSourceMap);
    }
}
