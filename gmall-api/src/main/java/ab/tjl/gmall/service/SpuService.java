package ab.tjl.gmall.service;

import ab.tjl.gmall.bean.PmsProductImage;
import ab.tjl.gmall.bean.PmsProductInfo;
import ab.tjl.gmall.bean.PmsProductSaleAttr;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:SPU接口
 * @Date:Created in 10:43 2019/9/9
 * @Modified By:
 */
public interface SpuService {
    List<PmsProductInfo> spuList(String catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductImage> spuImageList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId);
}
