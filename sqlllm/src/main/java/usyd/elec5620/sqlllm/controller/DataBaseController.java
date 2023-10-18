package usyd.elec5620.sqlllm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import usyd.elec5620.sqlllm.mapper.customizeddb.TableMapper;
import usyd.elec5620.sqlllm.proxy.JdkParamDsMethodProxy;
import usyd.elec5620.sqlllm.service.defaultdb.DefaultDbUserService;
import usyd.elec5620.sqlllm.util.DataSourceUtil;
import usyd.elec5620.sqlllm.vo.DbInfo;
import usyd.elec5620.sqlllm.vo.DbStatus;
import usyd.elec5620.sqlllm.vo.ResponseResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class DataBaseController {

    @Autowired
    private DefaultDbUserService defaultDbUserService;

    @Autowired
    private TableMapper tableMapper;

    private DbStatus status = DbStatus.DEFAULT_DATABASE;


    @PostMapping("/switch")
    public Object switchToDb(@RequestBody DbInfo dbInfo) {
        try {
            String newDsKey = System.currentTimeMillis() + "";
            // test connection
            DataSourceUtil.checkDbInfo(dbInfo);
            this.tableMapper = (TableMapper)JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, dbInfo);
            status = DbStatus.CUSTOMIZED_DATABASE;
            return ResponseResult.success("Switch to database: " + dbInfo.getDbName());
        } catch (Exception e) {
            return ResponseResult.error("Wrong database info");
        }
    }

    @GetMapping("/reset")
    public Object resetToDefaultDb() {
        status = DbStatus.DEFAULT_DATABASE;
        return ResponseResult.success("Database has been reset");
    }

    @GetMapping("/table")
    public Object findWithDbInfo() throws Exception {
        List<Map<String, Object>> tables;
        if (status == DbStatus.DEFAULT_DATABASE) {
            tables = defaultDbUserService.selectTableList();
        } else {
            tables = tableMapper.selectTableList();
        }
        return ResponseResult.success(tables);
    }

    @GetMapping("/table/info")
    public Object getTableInfo(@RequestBody Map<String, String> obj) {
        String tableName = obj.get("tableName");
        Map<String, List> res = new HashMap<>();
        List<Map<String, Object>> tableInfo;
        List<Map<String, Object>> data;

        if (status == DbStatus.DEFAULT_DATABASE) {
            tableInfo = defaultDbUserService.getTableInfo(tableName);
            data = defaultDbUserService.getAllDataFromTable(tableName);
        } else {
            tableInfo = tableMapper.selectColumsList(tableName);
            data = tableMapper.selectAllData(tableName);
        }
        ArrayList<String> names = new ArrayList<>();
        for (Map table: tableInfo) {
            names.add((String) table.get("COLUMN_NAME"));
        }
        res.put("columns", names);
        res.put("data", data);

        return ResponseResult.success(res);
    }
}
