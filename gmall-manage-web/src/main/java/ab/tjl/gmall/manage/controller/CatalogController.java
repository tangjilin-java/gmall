package ab.tjl.gmall.manage.controller;

import ab.tjl.gmall.bean.PmsBaseCatalog1;
import ab.tjl.gmall.bean.PmsBaseCatalog2;
import ab.tjl.gmall.bean.PmsBaseCatalog3;
import ab.tjl.gmall.service.CatalogService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:后台平台三级目录属性管理列表
 * @Date:Created in 16:24 2019/9/8
 * @Modified By:
 */
@Controller
@CrossOrigin //跨域问题
public class CatalogController {
    @Reference
    CatalogService catalogService;

    /**
     * Descrption: 获取三级目录
     * @Param: [catalog2Id]
     * @Return: java.util.List<ab.tjl.gmall.bean.PmsBaseCatalog3>
     */
    @RequestMapping("/getCatalog3")
    @ResponseBody
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){

        List<PmsBaseCatalog3> catalog1s = catalogService.getCatalog3(catalog2Id);
        return catalog1s;
    }

    /**
     * Descrption: 获取二级目录
     * @Param: [catalog1Id]
     * @Return: java.util.List<ab.tjl.gmall.bean.PmsBaseCatalog2>
     */
    @RequestMapping("/getCatalog2")
    @ResponseBody
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){

        List<PmsBaseCatalog2> catalog1s = catalogService.getCatalog2(catalog1Id);
        return catalog1s;
    }

    /**
     * Descrption:获取一级目录
     * @Param: []
     * @Return: java.util.List<ab.tjl.gmall.bean.PmsBaseCatalog1>
     */
    @RequestMapping("/getCatalog1")
    @ResponseBody
    public List<PmsBaseCatalog1> getCatalog1(){

        List<PmsBaseCatalog1> catalog1s = catalogService.getCatalog1();
        return catalog1s;
    }
}
