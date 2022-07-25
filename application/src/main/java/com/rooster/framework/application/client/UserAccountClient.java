package com.rooster.framework.application.client;

import com.rooster.framework.application.service.AccountService;
import com.rooster.framework.application.service.UserService;
import com.rooster.framework.core.annotations.Autowired;
import com.rooster.framework.core.annotations.Component;
import com.rooster.framework.core.annotations.Qualifier;

import java.util.logging.Logger;

@Component
public class UserAccountClient {

    private final Logger log = Logger.getLogger(UserAccountClient.class.getName());

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    @Qualifier("accountServiceImpl")
    private AccountService accountService;


    public void displayAccount() {
        String userName = userService.getUserName();
        Long accountNumber = accountService.getAccountNumber(userName);
        log.info("User name {}" + userName + ", account number: " + accountNumber);
    }

}
