package com.revature;

import com.revature.service.BankService;

public class BankLauncher {

    public static void main(String[] args) {
        try {
            BankService.dashboard();
        } catch(RuntimeException e) {
            System.out.println("An unexpected error occurred");
            BankService.dashboard();
        }
    }
}
