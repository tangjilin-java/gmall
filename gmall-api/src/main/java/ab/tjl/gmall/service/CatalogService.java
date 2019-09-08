package ab.tjl.gmall.service;

import ab.tjl.gmall.bean.PmsBaseCatalog1;
import ab.tjl.gmall.bean.PmsBaseCatalog2;
import ab.tjl.gmall.bean.PmsBaseCatalog3;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:一级目录接口
 * @Date:Created in 16:41 2019/9/8
 * @Modified By:
 */
public interface CatalogService {
    List<PmsBaseCatalog1> getCatalog1();

    List<PmsBaseCatalog2> getCatalog2(String catalog1Id);

    List<PmsBaseCatalog3> getCatalog3(String catalog2Id);
}
