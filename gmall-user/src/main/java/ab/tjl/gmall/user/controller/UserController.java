package ab.tjl.gmall.user.controller;

import ab.tjl.gmall.service.UserService;
import ab.tjl.gmall.bean.UmsMember;
import ab.tjl.gmall.bean.UmsMemberReceiveAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author:tangjilin
 * @Description:用户控制层
 * @Date:Created in 16:22 2019/9/4
 * @Modified By:
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/getReceiveAddressByMemberId")
    @ResponseBody
    public List<UmsMemberReceiveAddress>  getReceiveAddressByMemberId(String memberId){
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = userService.getReceiveAddressByMemberId(memberId);
        return umsMemberReceiveAddresses;
    }

    @RequestMapping("/getAllUser")
    @ResponseBody
    public List<UmsMember>  getAllUser(){
        List<UmsMember> umsMembers = userService.getAllUser();
        return umsMembers;
    }

    @RequestMapping("/index")
    @ResponseBody
    public String  index(){
        return "hello user!!";
    }
}
