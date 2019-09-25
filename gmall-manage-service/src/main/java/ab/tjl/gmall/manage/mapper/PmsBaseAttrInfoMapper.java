package ab.tjl.gmall.manage.mapper;

import ab.tjl.gmall.bean.PmsBaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:平台属性映射
 * @Date:Created in 19:11 2019/9/8
 * @Modified By:
 */
public interface PmsBaseAttrInfoMapper extends Mapper<PmsBaseAttrInfo> {
    List<PmsBaseAttrInfo> selectAttrValueListByValueId(@Param("valueIdStr") String valueIdStr);
}
