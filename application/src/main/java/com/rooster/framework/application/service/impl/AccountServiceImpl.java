package com.rooster.framework.application.service.impl;

import com.rooster.framework.application.service.AccountService;
import com.rooster.framework.core.annotations.Component;

@Component
public class AccountServiceImpl implements AccountService {

    @Override
    public Long getAccountNumber(String userName) {
        return 123344455656767L;
    }
}
