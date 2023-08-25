package usyd.elec5620.sqlllm.entity.defaultdb;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("test_user")
public class DefaultDbUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;
    /** 姓名 */
    private String name;
}
