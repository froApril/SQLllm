package usyd.elec5620.sqlllm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import usyd.elec5620.sqlllm.context.DynamicDataSourceContextHolder;

import javax.sql.DataSource;
import java.util.Map;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private Map<Object, Object> backupTargetDataSources;

    public DynamicDataSource(DataSource defaultDataSource, Map<Object, Object> targetDataSource) {
        backupTargetDataSources = targetDataSource;
        super.setDefaultTargetDataSource(defaultDataSource);
        super.setTargetDataSources(backupTargetDataSources);
        super.afterPropertiesSet();
    }

    public void addDataSource(String key, DataSource dataSource) {
        this.backupTargetDataSources.put(key, dataSource);
        super.setTargetDataSources(this.backupTargetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getContextKey();
    }
}
