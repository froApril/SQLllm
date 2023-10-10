package usyd.elec5620.sqlllm.entity.OpenAiEntity;

import lombok.Data;

@Data
public class OpenAiMessage {
    private String role;
    private String content;
}
