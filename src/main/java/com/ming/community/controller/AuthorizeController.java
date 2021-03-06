package com.ming.community.controller;

import com.ming.community.mapper.UserMapper;
import com.ming.community.model.User;
import com.ming.community.dto.AccessTtokenDTO;
import com.ming.community.dto.GithubUser;
import com.ming.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String  clientSecret;
    @Value("${github.client.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

        @GetMapping("/callback")
        public String callback(@RequestParam(name = "code") String code,
                               @RequestParam(name = "state") String state,
                               HttpServletRequest request)
        {
            AccessTtokenDTO accessTtokenDTO=new AccessTtokenDTO();
            accessTtokenDTO.setClient_id(clientId);
            accessTtokenDTO.setClient_secret(clientSecret);
            accessTtokenDTO.setCode(code);
            accessTtokenDTO.setRedirect_uri(redirectUri);
            accessTtokenDTO.setState(state);
            String accessTtoken=githubProvider.getAccesToken(accessTtokenDTO);
            GithubUser githubUser=githubProvider.getUser(accessTtoken);
           if(githubUser != null)
           {
               User user=new User();
               user.setToken(UUID.randomUUID().toString());
               user.setName(githubUser.getName());
               user.setAccountId(String.valueOf(githubUser.getId()));
               user.setGmtCreate(System.currentTimeMillis());
               user.setGmtModified(user.getGmtCreate());
               userMapper.insert(user);
               request.getSession().setAttribute("user",githubUser);
               return "redirect:/";
           }
           else {
               return "redirect:/";
           }


        }

}
