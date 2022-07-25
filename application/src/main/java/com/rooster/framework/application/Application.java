package com.rooster.framework.application;

import com.rooster.framework.application.client.UserAccountClient;
import com.rooster.framework.core.injection.RoosterInjection;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        long startedTime = System.currentTimeMillis();
        RoosterInjection.startApplication(Application.class);
        UserAccountClient client = RoosterInjection.getServices(UserAccountClient.class);
        if (client != null) client.displayAccount();
        long endedTime = System.currentTimeMillis();
        System.out.println("Time to run " + (endedTime - startedTime));
    }

}
