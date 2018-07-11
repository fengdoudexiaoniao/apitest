package ping.wg.apittest.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ping.wg.apittest.dao.ApiRepository;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ApiRepository apiRepository;
    @RequestMapping("/list2")
    public String userList2(Model model) throws Exception {
        model.addAttribute("hello","Hello, Spring Boot!");
        model.addAttribute("apiList", apiRepository.findAll());
        return "list";
    }

}







