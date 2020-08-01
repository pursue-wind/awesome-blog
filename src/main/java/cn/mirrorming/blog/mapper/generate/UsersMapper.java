package cn.mirrorming.blog.mapper.generate;

import cn.mirrorming.blog.domain.dto.user.UserDTO;
import cn.mirrorming.blog.domain.po.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UsersMapper extends BaseMapper<Users> {

    /**
     * 查询用户的非敏感信息
     *
     * @param id
     * @return
     */
    @Select("select id, email, `name`, sex, score, birthday, description, avatar, weibo from users where id=#{id} ")
    UserDTO selectUserById(Integer id);

    @ResultType(UserDTO.class)
    @Select({
            "<script>",
            "   select id, email, `name`, sex, score, birthday, description, avatar, weibo from users where id in",
            "   <foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "      #{id}",
            "   </foreach>",
            "</script>"
    })
    List<UserDTO> selectUserByIds(@Param("ids") Collection<Integer> ids);
}