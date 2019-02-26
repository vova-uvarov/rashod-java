package com.vuvarov.rashod.web;


import com.vuvarov.rashod.model.dto.AccountBalance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/balances")
public class BalanceController {
    
    @GetMapping
    public List<AccountBalance> all(){
        
    }
}
