package ab.tjl.gmall.user.service.impl;

import ab.tjl.gmall.service.UserService;
import ab.tjl.gmall.bean.UmsMember;
import ab.tjl.gmall.bean.UmsMemberReceiveAddress;
import ab.tjl.gmall.user.mapper.UmsMemberReceiveAddressesMapper;
import ab.tjl.gmall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
