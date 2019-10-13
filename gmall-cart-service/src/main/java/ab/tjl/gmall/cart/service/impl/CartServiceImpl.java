package ab.tjl.gmall.cart.service.impl;

import ab.tjl.gmall.bean.OmsCartItem;
import ab.tjl.gmall.cart.service.mapper.OmsCartItemMapper;
import ab.tjl.gmall.service.CartService;
import ab.tjl.gmall.util.RedisUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:tangjilin
 * @Description:购物车业务接口实现类
 * @Date:Created in 11:10 2019/9/24
 * @Modified By:
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Override
    public OmsCartItem ifCartExistByUser(String memberId, String skuId) {

        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        OmsCartItem omsCartItem1 = omsCartItemMapper.selectOne(omsCartItem);
        return omsCartItem1;

    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {
        if (StringUtils.isNotBlank(omsCartItem.getMemberId())) {
            omsCartItemMapper.insertSelective(omsCartItem);//Selective避免添加空值
        }
    }

    @Override
    public void updateCart(OmsCartItem omsCartItemFromDb) {

        Example e = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("id",omsCartItemFromDb.getId());

        omsCartItemMapper.updateByExampleSelective(omsCartItemFromDb,e);

    }

    @Override
    public void flushCartCache(String memberId) {

        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        List<OmsCartItem> omsCartItems = omsCartItemMapper.select(omsCartItem);

        // 同步到redis缓存中
        Jedis jedis = redisUtil.getJedis();

        Map<String,String> map = new HashMap<>();
        for (OmsCartItem cartItem : omsCartItems) {
            cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));//使用BigDecimal的multiply算法计算总价格
            map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
        }

        jedis.del("user:"+memberId+":cart");
        jedis.hmset("user:"+memberId+":cart",map);

        jedis.close();
    }

    @Override
    public List<OmsCartItem> cartList(String userId) {
        Jedis jedis = null;
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        try {
            jedis = redisUtil.getJedis();

            List<String> hvals = jedis.hvals("user:" + userId + ":cart");

            for (String hval : hvals) {
                OmsCartItem omsCartItem = JSON.parseObject(hval, OmsCartItem.class);
                omsCartItems.add(omsCartItem);
            }

        }catch (Exception e){
            // 处理异常，记录系统日志
            e.printStackTrace();
            //String message = e.getMessage();
            //logService.addErrLog(message);
            return null;
        }finally {
            jedis.close();
        }

        return omsCartItems;
    }

    @Override
    public void checkCart(OmsCartItem omsCartItem) {

        Example e = new Example(OmsCartItem.class);

        e.createCriteria().andEqualTo("memberId",omsCartItem.getMemberId()).andEqualTo("productSkuId",omsCartItem.getProductSkuId());

        omsCartItemMapper.updateByExampleSelective(omsCartItem,e);

        // 缓存同步
        flushCartCache(omsCartItem.getMemberId());

    }

    @Override
    public void delCart() {

    }
}

