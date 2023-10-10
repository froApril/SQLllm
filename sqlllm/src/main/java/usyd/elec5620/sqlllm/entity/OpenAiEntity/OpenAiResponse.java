package usyd.elec5620.sqlllm.entity.OpenAiEntity;

import lombok.Data;

import java.util.List;

@Data
public class OpenAiResponse {
    private String id;
    private String object;
    private String created;
    private String model;
    private List<OpenAiChoice> choices;
}
