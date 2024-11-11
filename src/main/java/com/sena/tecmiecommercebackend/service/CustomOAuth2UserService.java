package com.sena.tecmiecommercebackend.service;

<<<<<<< HEAD
=======
import java.util.Collections;
>>>>>>> c9a7f7a (add Google Account Auto login)
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.sena.tecmiecommercebackend.repository.IUserRepository;
import com.sena.tecmiecommercebackend.repository.entity.User;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
<<<<<<< HEAD
        System.out.println("检查这个话有没有打出来");
        // 获取用户信息
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        // String lastName = (String) attributes.get("family_name");

        // 检查用户是否已存在于数据库
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // 如果用户不存在，则将其保存到数据库
=======
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        // 检查用户是否存在，不存在则创建
        User user = userRepository.findByEmail(email);
        if (user == null) {
>>>>>>> c9a7f7a (add Google Account Auto login)
            user = new User();
            user.setFirstName(name);
            user.setLastName("");
            user.setEmail(email);
<<<<<<< HEAD
            // 这里密码字段可以为空，或设置为OAuth用户的标识符
            user.setPassword(""); // 或者你可以设置一个默认值

            userRepository.save(user);
        }

        return oAuth2User;
    }
}


=======
            user.setPassword("");
            user.setRoles(Collections.singletonList("USER")); // 默认角色
            userRepository.save(user);
        }
        
        return oAuth2User;
    }
}
>>>>>>> c9a7f7a (add Google Account Auto login)
