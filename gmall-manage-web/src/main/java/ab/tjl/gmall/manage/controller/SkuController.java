package ab.tjl.gmall.manage.controller;

import ab.tjl.gmall.bean.PmsSkuInfo;
import ab.tjl.gmall.service.SkuService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author:tangjilin
 * @Description:Sku属性列表
 * @Date:Created in 19:12 2019/9/10
 * @Modified By:
 */
@Controller
@CrossOrigin
public class SkuController {
    @Reference
    SkuService skuService;

    /**
     * Descrption: 保存sku属性值
     * @Param: [pmsSkuInfo]
     * @Return: java.lang.String
     */
    @RequestMapping("/saveSkuInfo")
    @ResponseBody
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){

        // 将spuId封装给productId
        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());

        // 处理默认图片
        String skuDefaultImg = pmsSkuInfo.getSkuDefaultImg();
        if(StringUtils.isBlank(skuDefaultImg)){
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuImageList().get(0).getImgUrl());
        }

        skuService.saveSkuInfo(pmsSkuInfo);

        return "success";
    }
}
