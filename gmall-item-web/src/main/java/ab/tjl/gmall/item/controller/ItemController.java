package ab.tjl.gmall.item.controller;

import ab.tjl.gmall.bean.PmsProductSaleAttr;
import ab.tjl.gmall.bean.PmsSkuInfo;
import ab.tjl.gmall.bean.PmsSkuSaleAttrValue;
import ab.tjl.gmall.service.SkuService;
import ab.tjl.gmall.service.SpuService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:tangjilin
 * @Description:商品详情页控制层
 * @Date:Created in 15:57 2019/9/11
 * @Modified By:
 */
@Controller
@CrossOrigin
public class ItemController {
    @Reference
    SkuService skuService;

    @Reference
    SpuService spuService;

    /**
     * Descrption: 商品详情页面
     * @Param: [skuId, map, request]
     * @Return: java.lang.String
     */
    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap map, HttpServletRequest request){

        String remoteAddr = request.getRemoteAddr();
        // request.getHeader("");// nginx负载均衡

        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId,remoteAddr);
        //sku对象
        map.put("skuInfo",pmsSkuInfo);
        //sku销售属性列表
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),pmsSkuInfo.getId());
        map.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrs);

        //查询当前sku的spu的其它sku的集合的hash表
        Map<String , String > skuSaleAttrHash = new HashMap<>();
        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());

        for (PmsSkuInfo skuInfo : pmsSkuInfos) {
            String k = "";
            String v = skuInfo.getId();


            List<PmsSkuSaleAttrValue> skuSaleAttrValueList =skuInfo.getSkuSaleAttrValueList();

            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                k += pmsSkuSaleAttrValue.getSaleAttrValueId() + "|";//"234|236"
            }
            skuSaleAttrHash.put(k,v);
        }
        //将sku的销售属性hash表放到页面 这样就不用再去访问数据库 但是存在一定问题
        String skuSaleAttrHashJsonStr = JSON.toJSONString(skuSaleAttrHash);
        map.put("skuSaleAttrHashJsonStr",skuSaleAttrHashJsonStr);
        return "item";
    }

    /**
     * Descrption: 测试thymeleaf
     * @Param: [modelMap]
     * @Return: java.lang.String
     */
    @RequestMapping("/index")
    public String index(ModelMap modelMap){

        List<String> list = new ArrayList<>();
        for (int i = 0; i <5 ; i++) {
            list.add("循环数据"+i);
        }

        modelMap.put("list",list);
        modelMap.put("hello","hello thymeleaf !!");

        modelMap.put("check","0");


        return "index";
    }
}
