package usyd.elec5620.sqlllm.mapper.customizeddb;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import usyd.elec5620.sqlllm.vo.User;

import java.util.List;
import java.util.Map;

@Repository
public interface TableMapper extends BaseMapper<Object> {

    @Select("select table_name, table_comment, create_time, update_time " +
            " from information_schema.tables " +
            " where table_schema = (select database())")
    List<Map<String,Object>> selectTableList();

    @Select("select * from information_schema.COLUMNS where TABLE_NAME = #{tableName}")
    List<Map<String, Object>> selectColumsList(@Param("tableName") String tableName);


    @Insert("insert into user values(null, #{user.username},#{user.password},#{user.type}, #{user.times})")
    int addUser(@Param("user") User user);

    @Select("select * from user where username = #{username} and password = #{password}")
    User searchUser(@Param("username") String username, @Param("password") String password);

    @Update("update user SET times = #{user.times} where username = #{user.username}")
    int updateUser(@Param("user")User user);

    @Select("select * from ${tableName}")
    List<Map<String, Object>> selectAllData(@Param("tableName") String tableName);

    @Select("${sql}")
    List<Map<String, Object>> executeSql(@Param("sql")String sql);

    @Select("select * from user")
    List<User> allUser();

    @Select("select * from user where username = #{username}")
    User getUserByUsername(@Param("username") String username);
}
