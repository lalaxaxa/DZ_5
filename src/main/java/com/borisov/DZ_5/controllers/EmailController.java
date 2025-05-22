package com.borisov.DZ_5.controllers;

import com.borisov.DZ_5.dto.EmailResponse;
import com.borisov.DZ_5.dto.EmailSendDTO;
import com.borisov.DZ_5.services.EmailService;
import com.borisov.DZ_5.util.EmailErrorResponse;
import com.borisov.DZ_5.util.EmailValidationException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/email")
public class EmailController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private EmailService emailService;
    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<EmailResponse> send(
            @RequestBody @Valid EmailSendDTO emailSendDTO,
            BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new EmailValidationException(getValidErrMsg(bindingResult));
        emailService.sendEmail(emailSendDTO);

        return ResponseEntity.ok(new EmailResponse(true, "Письмо успешно отправлено"));
    }

    private String getValidErrMsg(BindingResult bindingResult){
        StringBuilder errMsg = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errMsg.append(fieldError.getField())
                    .append(" - ").append(fieldError.getDefaultMessage())
                    .append(";");
        }
        return errMsg.toString();
    }

    @ExceptionHandler
    private ResponseEntity<EmailErrorResponse> handleException(EmailValidationException e){
        LOGGER.error(e.getMessage());
        EmailErrorResponse response = new EmailErrorResponse(e.getMessage(), System.currentTimeMillis());
        return  new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
