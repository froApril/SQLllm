package usyd.elec5620.sqlllm.entity.OpenAiEntity;

import lombok.Data;

@Data
public class OpenAiRequest {
    private String askStr;
    private String replyStr;
}
