package ab.tjl.gmall.manage.controller;

import ab.tjl.gmall.bean.PmsBaseAttrInfo;
import ab.tjl.gmall.bean.PmsBaseAttrValue;
import ab.tjl.gmall.service.AttrService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:商品属性列表
 * @Date:Created in 19:00 2019/9/8
 * @Modified By:
 */
@Controller
@CrossOrigin//跨域
public class AttrController {

    @Reference
    AttrService attrService;


    /**
     * Descrption: 保存平台属性信息
     * @Param: [pmsBaseAttrInfo]
     * @Return: java.lang.String
     */
    @RequestMapping("/saveAttrInfo")
    @ResponseBody
    public String  saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){

        String success = attrService.saveAttrInfo(pmsBaseAttrInfo);

        return "success";
    }

    /**
     * Descrption: 平台属性信息展示
     * @Param: [catalog3Id]
     * @Return: java.util.List<ab.tjl.gmall.bean.PmsBaseAttrInfo>
     */
    @RequestMapping("/attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id){

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.attrInfoList(catalog3Id);
        return pmsBaseAttrInfos;
    }

    /**
     * Descrption: 获取平台属性信息列表
     * @Param: [catalog3Id]
     * @Return: java.util.List<ab.tjl.gmall.bean.PmsBaseAttrInfo>
     */
    @RequestMapping("/getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(String attrId){

        List<PmsBaseAttrValue> pmsBaseAttrValues = attrService.getAttrValueList(attrId);
        return pmsBaseAttrValues;
    }
}
