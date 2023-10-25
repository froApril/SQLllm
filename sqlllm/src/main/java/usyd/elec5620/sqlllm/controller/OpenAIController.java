package usyd.elec5620.sqlllm.controller;

import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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


    @CrossOrigin
    @PostMapping("/askAi")
    public Object askAi(@RequestBody Map<String, String> obj) throws Exception{
        String newDsKey = System.currentTimeMillis() + "";
        this.tableMapper = (TableMapper) JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, DynamicDataSourceConfig.userDb);
<<<<<<< HEAD
        String username = obj.get("username");
        String askStr = obj.get("askStr");
=======
        System.out.println(session.getId());
        String response = checkUserQueryTime(session);
        if (response.equals("No query times")) {
            return ResponseResult.error(response);
        }
        String reply = openAiService.send(openAiRequest.getAskStr());
        openAiRequest.setReplyStr(reply);
        return JSONUtil.toJsonStr(openAiRequest);
    }
>>>>>>> 6d23249a36d4e7a94832db37e9b8e7e2ffc76440

        User target = tableMapper.getUserByUsername(username);
        System.out.println(target);
        if (target.getTimes() > 0) {
            target.setTimes(target.getTimes() - 1);
            tableMapper.updateUser(target);
        } else {
            return ResponseResult.error("No query times");
        }
        OpenAiRequest openAiRequest = new OpenAiRequest();
        String reply = openAiService.send(askStr);
        openAiRequest.setReplyStr(reply);
        openAiRequest.setAskStr(askStr);
        return JSONUtil.toJsonStr(openAiRequest);
    }

    @PostMapping("/askAi/sql")
    public Object askAiSqlQuery(@RequestBody Map<String, String> obj) throws Exception {
        String question = obj.get("sqlQuestion");
        String tableName = obj.get("tableName");
        String columns = obj.get("columns");
        String username = obj.get("username");
        String prompt = question + "\n" + "in the table " + tableName + " with columns: " + columns;
        String newDsKey = System.currentTimeMillis() + "";
        this.tableMapper = (TableMapper) JdkParamDsMethodProxy.createProxyInstance(tableMapper, newDsKey, DynamicDataSourceConfig.userDb);

        User target = tableMapper.getUserByUsername(username);
        if (target.getTimes() > 0) {
            target.setTimes(target.getTimes() - 1);
            tableMapper.updateUser(target);
        } else {
            return ResponseResult.error("No query times");
        }

        String reply = openAiService.send(prompt);
        OpenAiRequest pair = new OpenAiRequest();
        pair.setAskStr(prompt);
        pair.setReplyStr(reply);
        return JSONUtil.toJsonStr(pair);
    }

}
