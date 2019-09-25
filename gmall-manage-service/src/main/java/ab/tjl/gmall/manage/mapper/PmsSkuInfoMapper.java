package ab.tjl.gmall.manage.mapper;

import ab.tjl.gmall.bean.PmsSkuInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:sku信息映射
 * @Date:Created in 19:45 2019/9/10
 * @Modified By:
 */
public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo> {
    List<PmsSkuInfo> selectSkuSaleAttrValueListBySpu(String productId);
}
