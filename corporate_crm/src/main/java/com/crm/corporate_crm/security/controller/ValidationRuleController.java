package com.crm.corporate_crm.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.crm.corporate_crm.security.dto.InfoRequest;
import com.crm.corporate_crm.security.dto.RegisterRequest;
import com.crm.corporate_crm.security.service.ValidationRuleService;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/validation")
public class ValidationRuleController {

    @GetMapping("/register")
    public Map<String, Object> getRegisterRules(){
        return ValidationRuleService.extractRule(RegisterRequest.class);
    }

    @GetMapping("/info")
    public Map<String, Object> getInfoRules(){
        return ValidationRuleService.extractRule(InfoRequest.class);
    }
    
    
}
