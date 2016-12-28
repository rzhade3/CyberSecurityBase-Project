package sec.project.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sec.project.repository.AccountRepository;

@Controller
public class MainViewController {

    @Autowired
    private AccountRepository accountRepository;
    
    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/main";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginMapping(Principal principal) {
        return "login";
    }
    
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String accountMapping(Model model, Principal principal) {
        if (principal != null) {
            String name = principal.getName();
            model.addAttribute("username", name);
            model.addAttribute("posts", accountRepository.findByUsername(name).getPosts());
        }
        return "account";
    }
    
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupMapping(Model model, Principal principal) {
        if (principal != null) {
            String name = principal.getName();
            model.addAttribute("username", name);
        }
        return "signup";
    }

    @RequestMapping(value = "/newpost", method = RequestMethod.GET)
    public String postMapping(Model model, Principal principal) {
        if (principal != null) {
            String name = principal.getName();
            model.addAttribute("username", name);
        }
        return "newPost";
    }
    
    @RequestMapping(value = "/editpost", method = RequestMethod.GET)
    public String editPostMapping() {
        return "editPost";
    }
    
    @RequestMapping(value = "/change", method = RequestMethod.GET)
    public String changeMapping(Model model, Principal principal) {
        if (principal != null) {
            String name = principal.getName();
            model.addAttribute("username", name);
            return "change";
        }
        return "redirect:/main";
    }
}
