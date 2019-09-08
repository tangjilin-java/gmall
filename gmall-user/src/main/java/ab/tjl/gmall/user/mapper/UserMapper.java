package ab.tjl.gmall.user.mapper;

import ab.tjl.gmall.bean.UmsMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:用户持久层接口
 * @Date:Created in 16:24 2019/9/4
 * @Modified By:
 */
public interface UserMapper extends Mapper<UmsMember> {
    List<UmsMember> selectAllUser();
}
