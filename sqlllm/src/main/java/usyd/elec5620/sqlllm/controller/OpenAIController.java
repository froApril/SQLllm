package usyd.elec5620.sqlllm.controller;

import cn.hutool.json.JSONUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import usyd.elec5620.sqlllm.entity.OpenAiEntity.OpenAiRequest;
import usyd.elec5620.sqlllm.entity.OpenAiEntity.OpenAiResponse;
import usyd.elec5620.sqlllm.service.openAI.OpenAiService;
import usyd.elec5620.sqlllm.vo.ResponseResult;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class OpenAIController {

    @Resource
    private OpenAiService openAiService;

    @PostMapping("/askAi")
    public Object askAi(@RequestBody OpenAiRequest openAiRequest) throws Exception {
        try {
            String reply = openAiService.send(openAiRequest.getAskStr());
            openAiRequest.setReplyStr(reply);
            return JSONUtil.toJsonStr(openAiRequest);
        }catch (Exception e) {
            return ResponseResult.error("Open API error");
        }
    }

    @PostMapping("/askAi/sql")
    public Object askAiSqlQuery(@RequestBody Map<String, String> obj) {
        String question = obj.get("sqlQuestion");
        String tableName = obj.get("tableName");
        String columns = obj.get("columns");
        String prompt = question + "\n" + "in the table " + tableName + " with columns: " + columns;
        String reply = openAiService.send(prompt);
        OpenAiRequest pair = new OpenAiRequest();
        pair.setAskStr(prompt);
        pair.setReplyStr(reply);
        return JSONUtil.toJsonStr(pair);
    }
}
