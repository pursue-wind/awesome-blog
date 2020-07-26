package cn.mirrorming.blog.mapper.generate;

import cn.mirrorming.blog.domain.dto.user.UserDTO;
import cn.mirrorming.blog.domain.po.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UsersMapper extends BaseMapper<Users> {
    int updateBatch(List<Users> list);

    int updateBatchSelective(List<Users> list);

    int batchInsert(@Param("list") List<Users> list);

    int insertOrUpdate(Users record);

    int insertOrUpdateSelective(Users record);

    /**
     * 查询用户的非敏感信息
     *
     * @param id
     * @return
     */
    @Select("select id, email, `name`, sex, score, birthday, description, avatar, weibo from users where id=#{id} ")
    UserDTO selectUserById(Integer id);
}