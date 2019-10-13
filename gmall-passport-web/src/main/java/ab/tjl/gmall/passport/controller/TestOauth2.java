package ab.tjl.gmall.passport.controller;

import ab.tjl.gmall.util.HttpclientUtil;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class TestOauth2 {

    public static String getCode(){

        // 1 获得授权码
        // 1329358402
        // http://passport.gmall.com:8085/vlogin

        String s1 = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=2521304745&response_type=code&redirect_uri=http://passport.gmall.com:8085/vlogin");

        System.out.println(s1);

        // 在第一步和第二部返回回调地址之间,有一个用户操作授权的过程
        // 2 返回授权码到回调地址
        String s2 = "http://passport.gmall.com:8085/vlogin?code=14a572adbd2b8e459f71fcc19b4a3fac";

        return null;
    }

    public static String getAccess_token(){
        // 换取access_token
        // client_secret=2b641ab1537f32e95936845824cf0ab3
        // client_id=2521304745
        String s3 = "https://api.weibo.com/oauth2/access_token?";//?client_id=1329358402&client_secret=2b641ab1537f32e95936845824cf0ab3&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8085/vlogin&code=CODE";
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("client_id","2521304745");
        paramMap.put("client_secret","b3bd4d4f8aa89490e232cf6873a466b9");
        paramMap.put("grant_type","authorization_code");
        paramMap.put("redirect_uri","http://passport.gmall.com:8085/vlogin");
        paramMap.put("code","14a572adbd2b8e459f71fcc19b4a3fac");// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
        String access_token_json = HttpclientUtil.doPost(s3, paramMap);

       Map<String,String> access_map = JSON.parseObject(access_token_json,Map.class);

       System.out.println(access_map.get("access_token"));
       System.out.println(access_map.get("uid"));

        return access_map.get("access_token");
    }

    public static Map<String,String> getUser_info(){

        // 4 用access_token查询用户信息
        String s4 = "https://api.weibo.com/2/users/show.json?access_token=2.00lpE9WHZXIdkCf9284f8d85yTgcGB&uid=6892464345";
        String user_json = HttpclientUtil.doGet(s4);
        Map<String,String> user_map = JSON.parseObject(user_json,Map.class);

        System.out.println(user_map.get("1"));

        return user_map;
    }


    public static void main(String[] args) {

       // getCode();
        //getAccess_token();
        getUser_info();


    }
}
