package usyd.elec5620.sqlllm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import usyd.elec5620.sqlllm.config.DynamicDataSourceConfig;
import usyd.elec5620.sqlllm.mapper.customizeddb.TableMapper;
import usyd.elec5620.sqlllm.proxy.JdkParamDsMethodProxy;
import usyd.elec5620.sqlllm.service.defaultdb.DefaultDbUserService;
import usyd.elec5620.sqlllm.util.DataSourceUtil;
import usyd.elec5620.sqlllm.vo.DbInfo;
import usyd.elec5620.sqlllm.vo.DbStatus;
import usyd.elec5620.sqlllm.vo.ResponseResult;
import usyd.elec5620.sqlllm.vo.User;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class AuthController {

    @Autowired
    private TableMapper tableMapper;


    @CrossOrigin
    @PostMapping("/signup")
    public Object signUp(@RequestBody User user) throws Exception {
        String newDsKey = System.currentTimeMillis() + "";
        this.tableMapper = (TableMapper) JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, DynamicDataSourceConfig.userDb);
        int res = this.tableMapper.addUser(user);
        if (res > 0) {
            return ResponseResult.success("Add user success");
        }
        return ResponseResult.error("Sign up fail");
    }


    @CrossOrigin
    @PostMapping("/login")
    public Object login(@RequestBody User user) throws Exception {
        String newDsKey = System.currentTimeMillis() + "";
        this.tableMapper = (TableMapper) JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, DynamicDataSourceConfig.userDb);
        User target = tableMapper.searchUser(user.getUsername(), user.getPassword());
        System.out.println(target);
        if (target != null) {
            target.setPassword("");
            return ResponseResult.success(target);
        }
        return ResponseResult.error("no such user");
    }

    @CrossOrigin
    @PostMapping("/users")
    public Object getAllUserInfo(@RequestBody Map<String, String> obj) throws Exception {
        String newDsKey = System.currentTimeMillis() + "";
        this.tableMapper = (TableMapper) JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, DynamicDataSourceConfig.userDb);
        User currentUser = tableMapper.getUserByUsername(obj.get("username"));
        if (currentUser.getType() != 0) {
            return ResponseResult.error("You are not admin");
        }
        List<User> users = tableMapper.allUser();
        return ResponseResult.success(users);
    }

    @CrossOrigin
    @PostMapping("/addQueryTime")
    public Object addQueryTime(@RequestBody Map<String, String> obj) throws Exception {
        String newDsKey = System.currentTimeMillis() + "";
        this.tableMapper = (TableMapper) JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, DynamicDataSourceConfig.userDb);
        User target = tableMapper.getUserByUsername(obj.get("username"));
        if (target != null) {
            target.setTimes(target.getTimes() + 1);
            tableMapper.updateUser(target);
            return ResponseResult.success("user is updated");
        }
        return ResponseResult.error("no current user");
    }

    @CrossOrigin
    @PostMapping("/minusQueryTime")
    public Object minusQueryTime(@RequestBody Map<String, String> obj) throws Exception {
        String newDsKey = System.currentTimeMillis() + "";
        this.tableMapper = (TableMapper) JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, DynamicDataSourceConfig.userDb);
        User target = tableMapper.getUserByUsername(obj.get("username"));
        if (target != null) {
            target.setTimes(target.getTimes() - 1);
            tableMapper.updateUser(target);
            return ResponseResult.success("user is updated");
        }
        return ResponseResult.error("no current user");
    }

}
