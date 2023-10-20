package usyd.elec5620.sqlllm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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


    @GetMapping("/login")
    public Object login(@RequestBody User user, HttpSession session) throws Exception {
        String newDsKey = System.currentTimeMillis() + "";
        this.tableMapper = (TableMapper) JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, DynamicDataSourceConfig.userDb);
        User target = tableMapper.searchUser(user.getUsername(), user.getPassword());
        if (target != null) {
            session.setAttribute("currentUser", target);
            return ResponseResult.success("find user " + target.getUsername());
        }
        return ResponseResult.error("no such user");
    }

    @GetMapping("/users")
    public Object getAllUserInfo(HttpSession session) throws Exception {
        if (session.getAttribute("currentUser") == null) {
            return ResponseResult.error("Please login");
        }
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser.getType() != 0) {
            return ResponseResult.error("You are not admin");
        }
        String newDsKey = System.currentTimeMillis() + "";
        this.tableMapper = (TableMapper) JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, DynamicDataSourceConfig.userDb);
        List<User> users = tableMapper.allUser();
        return ResponseResult.success(users);
    }

    @PostMapping("/addQueryTime")
    public Object addQueryTime(HttpSession session) throws Exception {
        String newDsKey = System.currentTimeMillis() + "";
        this.tableMapper = (TableMapper) JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, DynamicDataSourceConfig.userDb);
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            currentUser.setTimes(currentUser.getTimes() + 1);
            tableMapper.updateUser(currentUser);
            return ResponseResult.success("user is updated");
        }
        return ResponseResult.error("no current user");
    }

    @PostMapping("/minusQueryTime")
    public Object minusQueryTime(HttpSession session) throws Exception {
        String newDsKey = System.currentTimeMillis() + "";
        this.tableMapper = (TableMapper) JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, DynamicDataSourceConfig.userDb);
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            currentUser.setTimes(currentUser.getTimes() - 1);
            tableMapper.updateUser(currentUser);
            return ResponseResult.success("user is updated");
        }
        return ResponseResult.error("no current user");
    }

}
