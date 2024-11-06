package com.sena.tecmiecommercebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.util.Date;

@Service
public class MailService {
    @Autowired
    private JavaMailSenderImpl javaMailSender;
    @Value("${spring.mail.username}")
    private String sendMailer;
     // 检测邮件信息类 detecting the mail information class

    private void checkMail(String to, String subject, String text){
        if (StringUtils.isEmpty(to)){
            throw new RuntimeException("The email recipient cannot be empty");
        }
        if (StringUtils.isEmpty(subject)){
            throw new RuntimeException("The email subject cannot be empty");
        }
        if (StringUtils.isEmpty(text)){
            throw new RuntimeException("The email content cannot be empty");
        }
    }

    // send email 发送邮件文本
    public String sendTextMailMessage(String to, String subject, String text){
        try {
            //true代表支持负责的类型
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
            //邮件发信人
            mimeMessageHelper.setFrom(sendMailer);
            //邮件收信人 1个或多个
            mimeMessageHelper.setTo(to.split(","));
            //邮件主题
            mimeMessageHelper.setSubject(subject);
            //邮件内容
            mimeMessageHelper.setText(text);
            //邮件发送时间
            mimeMessageHelper.setSentDate(new Date());

            //发送邮件
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            System.out.println("Email sent successfully："+sendMailer+"->"+to);
            return "Email sent successfully";
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Email sending failed："+sendMailer+"->"+to);
            return "Email sending failed";
        }
    }

}
