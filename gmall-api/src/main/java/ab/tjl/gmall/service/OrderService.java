package ab.tjl.gmall.service;

import ab.tjl.gmall.bean.OmsOrder;

/**
 * @author:tangjilin
 * @Description:订单业务接口
 * @Date:Created in 19:33 2019/9/27
 * @Modified By:
 */
public interface OrderService {
    String genTradeCode(String memberId);

    String checkTradeCode(String memberId,String tradeCode);

    void saveOrder(OmsOrder omsOrder);
}
