package usyd.elec5620.sqlllm.mapper.defaultdb;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import usyd.elec5620.sqlllm.entity.defaultdb.DefaultDbUser;

import java.util.List;
import java.util.Map;

@Repository
public interface DefaultDbUserMapper extends BaseMapper<DefaultDbUser> {
    @Select("select table_name, table_comment, create_time, update_time " +
            " from information_schema.tables " +
            " where table_schema = (select database())")
    List<Map<String,Object>> selectTableList();
}
