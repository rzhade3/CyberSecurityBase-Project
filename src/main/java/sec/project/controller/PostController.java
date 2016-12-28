package sec.project.controller;

import java.security.Principal;
import sec.project.repository.PostRepository;
import sec.project.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.repository.AccountRepository;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String list(Model model, Principal principal) {
        model.addAttribute("posts", postRepository.findAll());
        if (principal != null) {
            String name = principal.getName();
            model.addAttribute("username", name);
        }
        return "main";
    }
    
    @RequestMapping(value = "/newpost", method = RequestMethod.POST)
    public String addPost(@RequestParam String title, @RequestParam String content, Authentication authentication) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAccount(accountRepository.findByUsername(authentication.getName()));
        postRepository.save(post);
        return "redirect:/main";
    }
    
    // Returns the default edit post page
    @RequestMapping(value = "/editpost/{id}", method = RequestMethod.GET)
    public String editPost(Model model, @PathVariable long id) {
        Post post = postRepository.findOne(id);
        model.addAttribute("shouldBeChanged", post);
        return "editpost";
    }
    
    @RequestMapping(value = "/editpost/{id}", method = RequestMethod.PATCH)
    public String updatePost(@RequestParam String content, @PathVariable long id) {
        Post post = postRepository.findOne(id);
        post.setContent(content);
        postRepository.save(post);
        return "redirect:/account";
    }
    
    @RequestMapping(value = "/editpost/{id}", method = RequestMethod.DELETE)
    public String deletePost(@PathVariable long id) {
        postRepository.delete(id);
        return "redirect:/account";
    }
}
