package com.revature;

import com.revature.service.BankService;
import org.apache.log4j.Logger;

public class BankLauncher {

    private static final Logger logger = Logger.getLogger(BankLauncher.class);

    public static void main(String[] args) {
        while(true) {
            try {
                BankService.dashboard();
            } catch (RuntimeException e) {
                logger.info(e.getMessage());
            }
        }
    }
}
