package usyd.elec5620.sqlllm.service.openAI;

import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import usyd.elec5620.sqlllm.entity.OpenAiEntity.OpenAiResponse;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OpenAiServiceImpl implements OpenAiService{

    @Value("${ChatGPT.variables.apiKey}")
    private String apiKey;
    @Value("${ChatGPT.variables.maxTokens}")
    private String maxTokens;

    @Value("${ChatGPT.variables.model}")
    private String model;

    @Value("${ChatGPT.variables.temperature}")
    private String temperature;

    @Override
    public String send(String prompt) {
        JSONObject bodyJson = new JSONObject();

        JSONArray message = new JSONArray();

        JSONObject msg1 = new JSONObject();
        msg1.put("role", "user");
        msg1.put("content", prompt);

        message.add(msg1);

        bodyJson.put("messages", message);
        bodyJson.put("max_tokens", Integer.parseInt(maxTokens));
        bodyJson.put("temperature", Double.parseDouble(temperature));
        bodyJson.put("model", model);
        Map<String,Object> headMap = new HashMap<>();
        headMap.put("Authorization", "Bearer " + apiKey);
        HttpResponse httpResponse = HttpUtil.createPost("https://api.openai.com/v1/chat/completions")
                .header(Header.AUTHORIZATION, "Bearer " + apiKey)
                .body(JSONUtil.toJsonStr(bodyJson))
                .execute();
        String resStr = httpResponse.body();
        log.info("resStr: {}", resStr);

        OpenAiResponse openAiResponse = JSONUtil.toBean(resStr, OpenAiResponse.class);

        log.info("here: {}", openAiResponse.getChoices().get(0).getMessage().getContent());

        return openAiResponse.getChoices().get(0).getMessage().getContent();

    }
}
