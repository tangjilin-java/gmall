package ab.tjl.gmall.service;

import ab.tjl.gmall.bean.UmsMember;
import ab.tjl.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:用户业务接口
 * @Date:Created in 16:22 2019/9/4
 * @Modified By:
 */
public interface UserService {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);
}
