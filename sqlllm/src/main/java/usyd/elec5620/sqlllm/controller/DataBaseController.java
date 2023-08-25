package usyd.elec5620.sqlllm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import usyd.elec5620.sqlllm.mapper.customizeddb.TableMapper;
import usyd.elec5620.sqlllm.proxy.JdkParamDsMethodProxy;
import usyd.elec5620.sqlllm.service.defaultdb.DefaultDbUserService;
import usyd.elec5620.sqlllm.util.DataSourceUtil;
import usyd.elec5620.sqlllm.vo.DbInfo;
import usyd.elec5620.sqlllm.vo.DbStatus;
import usyd.elec5620.sqlllm.vo.ResponseResult;

import java.util.List;
import java.util.Map;

@RestController
public class DataBaseController {

    @Autowired
    private DefaultDbUserService defaultDbUserService;

    @Autowired
    private TableMapper tableMapper;

    private DbStatus status = DbStatus.DEFAULT_DATABASE;


    @PostMapping("/switch")
    public Object switchToDb(DbInfo dbInfo) {
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

    @PostMapping("/reset")
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
}
