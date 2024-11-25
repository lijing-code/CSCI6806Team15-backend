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

    /**
     * 检测邮件信息类
     */
    private void checkMail(String to, String subject, String text){
        if (StringUtils.isEmpty(to)){
            throw new RuntimeException("邮件收件人不能为空");
        }
        if (StringUtils.isEmpty(subject)){
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(text)){
            throw new RuntimeException("邮件内容不能为空");
        }
    }

    /**
     * 发送纯文本邮件
     */
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
            System.out.println("邮件发送成功："+sendMailer+"->"+to);
            return "邮件发送成功";
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("邮件发送失败："+sendMailer+"->"+to);
            return "邮件发送失败";
        }
    }

}
