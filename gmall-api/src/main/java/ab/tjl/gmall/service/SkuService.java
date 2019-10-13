package ab.tjl.gmall.service;

import ab.tjl.gmall.bean.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author:tangjilin
 * @Description:sku接口
 * @Date:Created in 19:42 2019/9/10
 * @Modified By:
 */
public interface SkuService {
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(String skuId,String ip);

    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId);

    List<PmsSkuInfo> getAllSku(String catalog3Id);

    boolean checkPrice(String productSkuId, BigDecimal price);
}
