package com.kucess.notebook.bussiness.captcha;

import com.kucess.notebook.bussiness.captcha.dto.CaptchaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CaptchaService {

    @Value("${captcha.secret}")
    private String secret;

    private static final String GOOGLE_RECAPTCHA_VERIFY_URL =
            "https://www.google.com/recaptcha/api/siteverify";

    private final RestTemplateBuilder restTemplate;

    public boolean verifyCaptcha(String response, String ip){
        ResponseEntity<CaptchaResponse> captchaResponseResponseEntity =
                restTemplate.build()
                        .postForEntity(GOOGLE_RECAPTCHA_VERIFY_URL+
                                        "?secret={secret}&response={response}&remoteip={remoteip}",
                                null, CaptchaResponse.class, secret, response, ip);

        Optional<CaptchaResponse> captchaResponseOptional =
                Optional.ofNullable(captchaResponseResponseEntity.getBody());
        return captchaResponseOptional.isPresent() && captchaResponseOptional.get().isSuccess();
    }
}
