package cn.mirrorming.blog.mapper.auto;

import cn.mirrorming.blog.domain.po.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper extends BaseMapper<Users> {
    int updateBatch(List<Users> list);

    int batchInsert(@Param("list") List<Users> list);

    int insertOrUpdate(Users record);

    int insertOrUpdateSelective(Users record);
}