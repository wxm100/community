package com.ming.community.controller;

import com.ming.community.dto.AccessTtokenDTO;
import com.ming.community.dto.GithubUser;
import com.ming.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        @GetMapping("/callback")
        public String callback(@RequestParam(name = "code") String code,
                               @RequestParam(name = "state") String state)
        {
            AccessTtokenDTO accessTtokenDTO=new AccessTtokenDTO();
            accessTtokenDTO.setClient_id(clientId);
            accessTtokenDTO.setClient_secret(clientSecret);
            accessTtokenDTO.setCode(code);
            accessTtokenDTO.setRedirect_uri(redirectUri);
            accessTtokenDTO.setState(state);
            String accessTtoken=githubProvider.getAccesToken(accessTtokenDTO);
            GithubUser user=githubProvider.getUser(accessTtoken);
            System.out.println(user);
            System.out.println(user.getName());

            return "index";
        }

}
