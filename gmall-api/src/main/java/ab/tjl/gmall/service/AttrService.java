package ab.tjl.gmall.service;

import ab.tjl.gmall.bean.PmsBaseAttrInfo;
import ab.tjl.gmall.bean.PmsBaseAttrValue;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:平台属性接口
 * @Date:Created in 19:07 2019/9/8
 * @Modified By:
 */
public interface AttrService {
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);
}
