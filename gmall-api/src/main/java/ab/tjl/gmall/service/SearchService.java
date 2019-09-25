package ab.tjl.gmall.service;

import ab.tjl.gmall.bean.PmsSearchParam;
import ab.tjl.gmall.bean.PmsSearchSkuInfo;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:搜索服务层接口
 * @Date:Created in 17:39 2019/9/22
 * @Modified By:
 */
public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
