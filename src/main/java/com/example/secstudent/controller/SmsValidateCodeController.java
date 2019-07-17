package com.example.secstudent.controller;

import com.example.secstudent.model.SmsCode;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SmsValidateCodeController {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    public static final String SESSION_KEY = "SESSION_KEY_SMS_CODE";

    @GetMapping("/code/sms")
    public void createCode(HttpServletRequest request) {
        SmsCode smsCode = createSmsCode();
        System.out.println("验证码发送成功：" + smsCode);
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, smsCode);
    }

    private SmsCode createSmsCode() {
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        return new SmsCode(code, 600);
    }
}
