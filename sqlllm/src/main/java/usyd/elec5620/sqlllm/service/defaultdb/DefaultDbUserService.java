package usyd.elec5620.sqlllm.service.defaultdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import usyd.elec5620.sqlllm.annotation.DS;
import usyd.elec5620.sqlllm.constants.DataSourceConstants;
import usyd.elec5620.sqlllm.entity.defaultdb.DefaultDbUser;
import usyd.elec5620.sqlllm.mapper.defaultdb.DefaultDbUserMapper;

import java.util.List;
import java.util.Map;

@Service
public class DefaultDbUserService {
    @Autowired
    private DefaultDbUserMapper defaultDbUserMapper;

    @DS
    public List<DefaultDbUser> getAllUser(){
        return defaultDbUserMapper.selectList(null);
    }

    @DS
    public List<Map<String,Object>> selectTableList() {
        return defaultDbUserMapper.selectTableList();
    };

    @DS
    public List<Map<String,Object>> getTableInfo(String tableName) {
        return defaultDbUserMapper.selectColumsList(tableName);
    };

    @DS
    public List<Map<String, Object>> getAllDataFromTable(String tableName) {
        return defaultDbUserMapper.selectAllDataFromTable(tableName);
    }

    @DS
    public List<Map<String, Object>> getData(String sql) {
        return defaultDbUserMapper.executeSql(sql);
    }
}
