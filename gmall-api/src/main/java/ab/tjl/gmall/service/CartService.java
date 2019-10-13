package ab.tjl.gmall.service;

import ab.tjl.gmall.bean.OmsCartItem;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:购物车业务接口
 * @Date:Created in 10:16 2019/9/24
 * @Modified By:
 */
public interface CartService {
    OmsCartItem ifCartExistByUser(String memberId, String skuId);

    void addCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItemFromDb);

    void flushCartCache(String memberId);

    List<OmsCartItem> cartList(String userId);

    void checkCart(OmsCartItem omsCartItem);

    void delCart();
}
