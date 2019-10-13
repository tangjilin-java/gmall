package ab.tjl.gmall.user.service.impl;

import ab.tjl.gmall.bean.UmsMember;
import ab.tjl.gmall.bean.UmsMemberReceiveAddress;
import ab.tjl.gmall.service.UserService;
import ab.tjl.gmall.user.mapper.UmsMemberReceiveAddressesMapper;
import ab.tjl.gmall.user.mapper.UserMapper;
import ab.tjl.gmall.util.RedisUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:用户业务层实现类
 * @Date:Created in 16:23 2019/9/4
 * @Modified By:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UmsMemberReceiveAddressesMapper umsMemberReceiveAddressesMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<UmsMember> getAllUser() {
        //List<UmsMember> umsMemberList = userMapper.selectAllUser();
        List<UmsMember> umsMemberList = userMapper.selectAll();
        return umsMemberList;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {

        //简单select查询方法
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);

        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses =
                umsMemberReceiveAddressesMapper.select(umsMemberReceiveAddress);

       /*//通用mapper的selectexample方法
        Example e = new Example(UmsMemberReceiveAddress.class);
        e.createCriteria().andEqualTo(memberId);
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressesMapper.selectByExample(e);
        */
        return umsMemberReceiveAddresses;
    }

    @Override
    public UmsMember login(UmsMember umsMember) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();

            if (jedis != null) {
                String umsMemberStr = jedis.get("user:" + umsMember.getPassword() + umsMember.getUsername() + ":info");

                if (StringUtils.isNotBlank(umsMemberStr)) {
                    //密码正确
                    UmsMember umsMemberFromCache = JSON.parseObject(umsMemberStr, UmsMember.class);
                    return umsMemberFromCache;
                }
            }
            //连接redis失败  开启数据库读取数据
            UmsMember umsMemberFromDb = loginFromDb(umsMember);
            if (umsMemberFromDb != null) {
                jedis.setex("user:" + umsMember.getPassword() + umsMember.getUsername() + ":info", 60 * 60 * 24, JSON.toJSONString(umsMemberFromDb));
            }
            return umsMemberFromDb;

        } finally {
            jedis.close();
        }
    }

    @Override
    public void addUserToken(String token, String memberId) {
        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:" + memberId + ":token", 60 * 60 * 2, token);
        jedis.close();
    }

    @Override
    public UmsMember addOauthUser(UmsMember umsMember) {
        userMapper.insertSelective(umsMember);
        return umsMember;
    }

    @Override
    public UmsMember checkOauthUser(UmsMember umsCheck) {
        UmsMember umsMember = userMapper.selectOne(umsCheck);
        return umsMember;
    }

    @Override
    public UmsMemberReceiveAddress getReceiveAddressById(String receiveAddressId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setId(receiveAddressId);
        UmsMemberReceiveAddress umsMemberReceiveAddress1 = umsMemberReceiveAddressesMapper.selectOne(umsMemberReceiveAddress);
        return umsMemberReceiveAddress1;
    }

    private UmsMember loginFromDb(UmsMember umsMember) {

        List<UmsMember> umsMembers = userMapper.select(umsMember);
        if (umsMembers != null) {
            return umsMembers.get(0);
        }
        return null;
    }
}
