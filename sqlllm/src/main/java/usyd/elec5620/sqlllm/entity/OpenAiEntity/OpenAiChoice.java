package usyd.elec5620.sqlllm.entity.OpenAiEntity;

import lombok.Data;

@Data
public class OpenAiChoice {
    private Integer index;
    private OpenAiMessage message;
    private String finish_reason;

}
