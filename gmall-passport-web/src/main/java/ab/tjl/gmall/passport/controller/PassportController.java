package ab.tjl.gmall.passport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author:tangjilin
 * @Description:认证中心
 * @Date:Created in 19:01 2019/9/24
 * @Modified By:
 */
@Controller
public class PassportController {

    @RequestMapping("/index")
    public String  index(){
        return "index";
    }
}
