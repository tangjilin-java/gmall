package ab.tjl.gmall.manage.service.impl;

import ab.tjl.gmall.bean.PmsSkuAttrValue;
import ab.tjl.gmall.bean.PmsSkuImage;
import ab.tjl.gmall.bean.PmsSkuInfo;
import ab.tjl.gmall.bean.PmsSkuSaleAttrValue;
import ab.tjl.gmall.manage.mapper.PmsSkuAttrValueMapper;
import ab.tjl.gmall.manage.mapper.PmsSkuImageMapper;
import ab.tjl.gmall.manage.mapper.PmsSkuInfoMapper;
import ab.tjl.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import ab.tjl.gmall.service.SkuService;
import ab.tjl.gmall.util.RedisUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author:tangjilin
 * @Description:sku接口实现类
 * @Date:Created in 19:43 2019/9/10
 * @Modified By:
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {

        // 插入skuInfo
        int i = pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String skuId = pmsSkuInfo.getId();

        // 插入平台属性关联
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        // 插入销售属性关联
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

        // 插入图片信息
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
    }

    public PmsSkuInfo getSkuByIdFromDb(String skuId) {
        // sku商品对象
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        // sku的图片集合
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        skuInfo.setSkuImageList(pmsSkuImages);
        return skuInfo;
    }

    @Override
    public PmsSkuInfo getSkuById(String skuId, String ip) {
        System.out.println("ip为" + ip + "的用户:" + Thread.currentThread().getName() + "进入的商品详情的请求");
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();

        // 连接缓存
        Jedis jedis = redisUtil.getJedis();

        //查询缓存
        String skuKey = "sku" + skuId + ":info";
        String skuJson = jedis.get(skuKey);
        if (StringUtils.isNotBlank(skuJson)) {
            System.out.println("ip为" + ip + "的用户:" + Thread.currentThread().getName() + "从缓存中获取商品详情");
            pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
        } else {
            //如果没有缓存就去查询sql
            System.out.println("ip为" + ip + "的用户:" + Thread.currentThread().getName() + "发现缓存中没有，申请缓存的分布式锁：" + "sku:" + skuId + ":lock");

            //设置分布式锁 拿到锁的线程有10秒的过期时间
            String token = UUID.randomUUID().toString();
            String OK = jedis.set("sku:" + skuId + ":lock", token, "nx", "px", 10 * 1000);
            if (StringUtils.isNotBlank(OK) && OK.equals("OK")) {
                //设置成功 有权在10秒的过期时间内访问数据库
                System.out.println("ip为" + ip + "的用户:" + Thread.currentThread().getName() + "有权在10秒的过期时间内访问数据库：" + "sku:" + skuId + ":lock");

                pmsSkuInfo = getSkuByIdFromDb(skuId);
                //为了让用户手速慢的也能体会自旋 睡5秒
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (pmsSkuInfo != null) {
                    //mysql查询结果存入redis
                    jedis.set("sku:" + skuId + ":info", JSON.toJSONString(pmsSkuInfo));
                } else {
                    //数据库中不存在该sku
                    //为了防止缓存穿透，将null或者空字符串设置给redis，并且将这个null或者空字符串的过期时间为3分钟
                    jedis.setex("sku:" + skuId + ":info", 60 * 3, JSON.toJSONString(""));
                }

                // 在访问mysql后，将mysql的分布锁释放
                System.out.println("ip为" + ip + "的用户:" + Thread.currentThread().getName() + "使用完毕，将锁归还：" + "sku:" + skuId + ":lock");
                String lockToken = jedis.get("sku:" + skuId + ":lock");
                if (StringUtils.isNotBlank(lockToken) && lockToken.equals(token)) {
                    //jedis.eval("lua");可与用lua脚本，在查询到key的同时删除该key，防止高并发下的意外的发生
                    jedis.del("sku:" + skuId + ":lock");// 用token确认删除的是自己的sku的锁
                }
            } else {
                // 设置失败，自旋（该线程在睡眠几秒后，重新尝试访问本方法）
                System.out.println("ip为" + ip + "的用户:" + Thread.currentThread().getName() + "没有拿到锁，开始自旋");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return getSkuById(skuId, ip);//return 不会创建一个新的线程B 就不会出现孤儿线程B
            }

        }

        jedis.close();

        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {

        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);
        return pmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getAllSku(String catalog3Id) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String skuId = pmsSkuInfo.getId();

            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        }
        return pmsSkuInfos;
    }

    @Override
    public boolean checkPrice(String productSkuId, BigDecimal price) {
        boolean b = false;

        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(productSkuId);
        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        BigDecimal price1 = pmsSkuInfo1.getPrice();

        if (price1.compareTo(price) == 0){
            b = true;
        }

        return b;
    }
}
