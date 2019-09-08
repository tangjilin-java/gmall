package ab.tjl.gmall.manage.service.impl;

import ab.tjl.gmall.bean.PmsBaseCatalog1;
import ab.tjl.gmall.bean.PmsBaseCatalog2;
import ab.tjl.gmall.bean.PmsBaseCatalog3;
import ab.tjl.gmall.manage.mapper.PmsBaseCatalog1Mapper;
import ab.tjl.gmall.manage.mapper.PmsBaseCatalog2Mapper;
import ab.tjl.gmall.manage.mapper.PmsBaseCatalog3Mapper;
import ab.tjl.gmall.service.CatalogService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:以及目录接口实现类
 * @Date:Created in 16:43 2019/9/8
 * @Modified By:
 */
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;
    @Autowired
    private PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;
    @Autowired
    private PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {


        return pmsBaseCatalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {

        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(catalog1Id);
        List<PmsBaseCatalog2> pmsBaseCatalog2s = pmsBaseCatalog2Mapper.select(pmsBaseCatalog2);
        return pmsBaseCatalog2s;
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalog2Id);
        List<PmsBaseCatalog3> pmsBaseCatalog3s = pmsBaseCatalog3Mapper.select(pmsBaseCatalog3);
        return pmsBaseCatalog3s;
    }
}
