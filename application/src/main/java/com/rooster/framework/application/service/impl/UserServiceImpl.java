package com.rooster.framework.application.service.impl;

import com.rooster.framework.application.service.UserService;
import com.rooster.framework.core.annotations.Component;

@Component
public class UserServiceImpl implements UserService {

    @Override
    public String getUserName() {
        return "Sandro Oliveira";
    }
}
