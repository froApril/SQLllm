package usyd.elec5620.sqlllm.service.openAI;

import org.springframework.stereotype.Service;

@Service
public interface OpenAiService {
    String send(String prompt);

}
