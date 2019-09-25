package ab.tjl.gmall.manage.controller;

import ab.tjl.gmall.bean.PmsProductImage;
import ab.tjl.gmall.bean.PmsProductInfo;
import ab.tjl.gmall.bean.PmsProductSaleAttr;
import ab.tjl.gmall.manage.util.PmsUploadUtil;
import ab.tjl.gmall.service.SpuService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:商品SPU
 * @Date:Created in 10:38 2019/9/9
 * @Modified By:
 */
@Controller
@CrossOrigin
public class SpuController {

    @Reference
    SpuService spuService;

    /**
     * Descrption: 产品图片
     * @Param: [spuId]
     * @Return: java.util.List<ab.tjl.gmall.bean.PmsProductImage>
     */
    @RequestMapping("/spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(String spuId){

        List<PmsProductImage> pmsProductImages = spuService.spuImageList(spuId);
        return pmsProductImages;
    }

    /**
     * Descrption: 产品属性值列表
     * @Param: [spuId]
     * @Return: java.util.List<ab.tjl.gmall.bean.PmsProductSaleAttr>
     */
    @RequestMapping("/spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){

        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }

    /**
     * Descrption: 图片分布式上传
     * @Param: [multipartFile]
     * @Return: java.lang.String
     */
    @RequestMapping("/fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){

        //将图片或者音视频上传到分布式的文件存储系统

        //将图片的存储的地址返回到页面
        String imgUrl = PmsUploadUtil.uploadImage(multipartFile);
        System.out.println(imgUrl);
        return imgUrl;
    }

    /**
     * Descrption: 保存Spu信息
     * @Param: [pmsProductInfo]
     * @Return: java.lang.String
     */
    @RequestMapping("/saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){

        spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    /**
     * Descrption: 展示spu
     * @Param: [catalog3Id]
     * @Return: java.util.List<ab.tjl.gmall.bean.PmsProductInfo>
     */
    @RequestMapping("/spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(String catalog3Id){

        List<PmsProductInfo> pmsProductInfos = spuService.spuList(catalog3Id);
        return pmsProductInfos;
    }
}
