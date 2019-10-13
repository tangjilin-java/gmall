package ab.tjl.gmall.cart.controller;

import ab.tjl.gmall.annotations.LoginRequired;
import ab.tjl.gmall.bean.OmsCartItem;
import ab.tjl.gmall.bean.PmsSkuInfo;
import ab.tjl.gmall.service.CartService;
import ab.tjl.gmall.service.SkuService;
import ab.tjl.gmall.util.CookieUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:tangjilin
 * @Description:购物车
 * @Date:Created in 8:52 2019/9/24
 * @Modified By:
 */
@Controller
public class CartController {
    @Reference
    SkuService skuService;

    @Reference
    CartService cartService;

/*
    @RequestMapping("/toTrade")
    @LoginRequired(loginSuccess = true)//表示登录成功才能使用
    public String toTrade(HttpServletRequest request,HttpServletResponse response, HttpSession session, ModelMap modelMap) {
         String memberId = (String)request.getAttribute("memberId");
         String nickname = (String)request.getAttribute("nickname");

        return "toTrade";
    }*/
    /**
     * Descrption: 异步选中商品 替换cartListInner
     * @Param: [isChecked, skuId, request, response, session, modelMap]
     * @Return: java.lang.String
     */
    @RequestMapping("/checkCart")
    @LoginRequired(loginSuccess = false)//表示登录失败也可以使用
    public String checkCart(String isChecked,String skuId,HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {

        String memberId = (String)request.getAttribute("memberId");
        //String nickname = (String)request.getAttribute("nickname");

        // 调用服务，修改状态
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setIsChecked(isChecked);
        cartService.checkCart(omsCartItem);

        // 将最新的数据从缓存中查出，渲染给内嵌页
        List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
        modelMap.put("cartList",omsCartItems);

        // 被勾选商品的总额
        BigDecimal totalAmount =getTotalAmount(omsCartItems);
        modelMap.put("totalAmount",totalAmount);
        return "cartListInner";
    }


    /**
     * Descrption: 购物车列表信息
     * @Param: [request, response, session, modelMap]
     * @Return: java.lang.String
     */
    @RequestMapping("/cartList")
    @LoginRequired(loginSuccess = false)
    public String cartList(HttpServletRequest request,ModelMap modelMap) {

        List<OmsCartItem> omsCartItems = new ArrayList<>();
        String memberId = (String)request.getAttribute("memberId");
        //String nickname = (String)request.getAttribute("nickname");

        if(StringUtils.isNotBlank(memberId)){
            // 已经登录查询db
            omsCartItems = cartService.cartList(memberId);
        }else{
            // 没有登录查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isNotBlank(cartListCookie)){
                omsCartItems = JSON.parseArray(cartListCookie,OmsCartItem.class);
            }
        }

        for (OmsCartItem omsCartItem : omsCartItems) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(omsCartItem.getQuantity()));
        }

        modelMap.put("cartList",omsCartItems);
        // 被勾选商品的总额
        BigDecimal totalAmount =getTotalAmount(omsCartItems);
        modelMap.put("totalAmount",totalAmount);
        return "cartList";
    }


    /**
     * Descrption: 计算商品总金额
     * @Param: [omsCartItems]
     * @Return: java.math.BigDecimal
     */
    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAmount = new BigDecimal("0");

        for (OmsCartItem omsCartItem : omsCartItems) {
            BigDecimal totalPrice = omsCartItem.getTotalPrice();


            if(omsCartItem.getIsChecked().equals("1")){
                totalAmount = totalAmount.add(totalPrice);
            }else {
                return null;
            }

        }

        return totalAmount;
    }


    /**
     * Descrption: 添加商品到购物车
     * @Param: [skuId, quantity, request, response, session]
     * @Return: java.lang.String
     */
    @RequestMapping("/addToCart")
    @LoginRequired(loginSuccess = false)//表示登录失败也可以使用
    public String addToCart(String skuId, int quantity, HttpServletRequest request, HttpServletResponse response) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();

        // 调用商品服务查询商品信息
        PmsSkuInfo skuInfo = skuService.getSkuById(skuId, "");

        // 将商品信息封装成购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(skuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        omsCartItem.setProductId(skuInfo.getProductId());
        omsCartItem.setProductName(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("11111111111");
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setQuantity(new BigDecimal(quantity));
        omsCartItem.setIsChecked("1");


        // 判断用户是否登录
        String memberId = (String)request.getAttribute("memberId");
        String nickname = (String)request.getAttribute("nickname");


        if (StringUtils.isBlank(memberId)) {
            // 用户没有登录

            // cookie里原有的购物车数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isBlank(cartListCookie)) {
                // cookie为空
                omsCartItems.add(omsCartItem);
            } else {
                // cookie不为空
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
                // 判断添加的购物车数据在cookie中是否存在
                boolean exist = if_cart_exist(omsCartItems, omsCartItem);
                if (exist) {
                    // 之前添加过，更新购物车添加数量
                    for (OmsCartItem cartItem : omsCartItems) {
                        if (cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())) {
                            cartItem.setQuantity(cartItem.getQuantity().add(omsCartItem.getQuantity()));
                        }
                    }
                } else {
                    // 之前没有添加，新增当前的购物车
                    omsCartItems.add(omsCartItem);
                }
            }

            // 更新cookie
            CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
        } else {
            // 用户已经登录
            // 从db中查出购物车数据
            OmsCartItem omsCartItemFromDb = cartService.ifCartExistByUser(memberId,skuId);

            if(omsCartItemFromDb==null){
                // 该用户没有添加过当前商品
                omsCartItem.setMemberId(memberId);
                omsCartItem.setMemberNickname(nickname);
                omsCartItem.setQuantity(new BigDecimal(quantity));
                cartService.addCart(omsCartItem);

            }else{
                // 该用户添加过当前商品
                omsCartItemFromDb.setQuantity(omsCartItemFromDb.getQuantity().add(omsCartItem.getQuantity()));
                cartService.updateCart(omsCartItemFromDb);
            }

            // 同步缓存
            cartService.flushCartCache(memberId);
        }


        return "redirect:/success.html";
    }

    /**
     * Descrption: 判断购物车是否存在
     * @Param: [omsCartItems, omsCartItem]
     * @Return: boolean
     */
    private boolean if_cart_exist(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {

        boolean b = false;

        for (OmsCartItem cartItem : omsCartItems) {
            String productSkuId = cartItem.getProductSkuId();

            if (productSkuId.equals(omsCartItem.getProductSkuId())) {
                b = true;
            }
        }


        return b;
    }
}
