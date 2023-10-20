package usyd.elec5620.sqlllm.controller;

import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import usyd.elec5620.sqlllm.config.DynamicDataSourceConfig;
import usyd.elec5620.sqlllm.entity.OpenAiEntity.OpenAiRequest;
import usyd.elec5620.sqlllm.entity.OpenAiEntity.OpenAiResponse;
import usyd.elec5620.sqlllm.mapper.customizeddb.TableMapper;
import usyd.elec5620.sqlllm.proxy.JdkParamDsMethodProxy;
import usyd.elec5620.sqlllm.service.openAI.OpenAiService;
import usyd.elec5620.sqlllm.vo.DbStatus;
import usyd.elec5620.sqlllm.vo.ResponseResult;
import usyd.elec5620.sqlllm.vo.User;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class OpenAIController {

    @Resource
    private OpenAiService openAiService;

    @Autowired
    private TableMapper tableMapper;


    @PostMapping("/askAi")
    public Object askAi(@RequestBody OpenAiRequest openAiRequest, HttpSession session) throws Exception{
        String newDsKey = System.currentTimeMillis() + "";
        this.tableMapper = (TableMapper) JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, DynamicDataSourceConfig.userDb);
        String response = checkUserQueryTime(session);
        if (response.equals("No query times")) {
            return ResponseResult.error(response);
        }
        String reply = openAiService.send(openAiRequest.getAskStr());
        openAiRequest.setReplyStr(reply);
        return JSONUtil.toJsonStr(openAiRequest);
    }

    private String checkUserQueryTime(HttpSession session) throws Exception{
        String newDsKey = System.currentTimeMillis() + "";
        this.tableMapper = (TableMapper) JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, DynamicDataSourceConfig.userDb);
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            if (session.getAttribute("query_time") != null) {
                int guest_query_time = (int)session.getAttribute("query_time");
                if (guest_query_time == 0) {
                    return "No query times";
                } else {
                    session.setAttribute("query_time", guest_query_time - 1);
                }
            } else {
                session.setAttribute("query_time", 3);
            }
        } else {
            int user_query_time = currentUser.getTimes();
            if (user_query_time == 0) {
                return "No query times";
            } else {
                // update user
                currentUser.setTimes(user_query_time - 1);
                tableMapper.updateUser(currentUser);
            }
        }
        return "ok";
    }

    @PostMapping("/askAi/sql")
    public Object askAiSqlQuery(@RequestBody Map<String, String> obj, HttpSession session) throws Exception {
        String question = obj.get("sqlQuestion");
        String tableName = obj.get("tableName");
        String columns = obj.get("columns");
        String prompt = question + "\n" + "in the table " + tableName + " with columns: " + columns;
        System.out.println(prompt);

        String response = checkUserQueryTime(session);
        if (response.equals("No query times")) {
            return ResponseResult.error(response);
        }
        String reply = openAiService.send(prompt);
        OpenAiRequest pair = new OpenAiRequest();
        pair.setAskStr(prompt);
        pair.setReplyStr(reply);
        return JSONUtil.toJsonStr(pair);
    }

}
