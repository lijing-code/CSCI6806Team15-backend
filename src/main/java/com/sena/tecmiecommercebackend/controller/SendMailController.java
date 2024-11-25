package com.sena.tecmiecommercebackend.controller;


import com.sena.tecmiecommercebackend.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sendmail")
public class SendMailController {
    @Autowired
    private MailService mailService;

    /**
     * 发送文本邮件
     */
    @GetMapping("/sendTextMail")
    public String sendTextMail(String to, String subject, String text){
        String s = mailService.sendTextMailMessage(to, subject, text);
        return s;
    }
}
