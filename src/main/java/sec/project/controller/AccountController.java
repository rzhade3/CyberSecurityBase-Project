package sec.project.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.repository.AccountRepository;

@Controller
public class AccountController {

    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String addAccount(@RequestParam String username, @RequestParam String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(encoder.encode(password));
        accountRepository.save(account);
        return "redirect:/login";
    }
    
    @RequestMapping(value = "/change", method = RequestMethod.PATCH)
    public String changePassword(Principal principal, @RequestParam String password) {
        String name = principal.getName();
        Account account = accountRepository.findByUsername(name);
        account.setPassword(encoder.encode(password));
        accountRepository.save(account);
        return "redirect:/main";
    }
}
