package com.revature.service;

import com.revature.model.User;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BankService {

    private static Scanner sc = new Scanner(System.in);

    public static void registerUser() {
        System.out.print("Please enter your username: ");
        String username = sc.next().trim();
        System.out.print("Enter your password: ");
        String password = sc.next().trim();
        System.out.println("username: " + username + "\npassword: " + password);
    }

    public static void loginScreen() {
        System.out.print("Enter 1 if you are a registered User or Customer or press 2 to register for an account: ");
        int val;
        try {
            val = sc.nextInt();
            if(val == 1) {
                showLogin();
            } else if(val == 2) {
                registerUser();
            }
        } catch (InputMismatchException e) {
            sc.next();
            System.out.println("That was an invalid input please try again.");
            loginScreen();
        }
    }

    public static void showLogin() {
        System.out.println("Showing Login Screen");
    }
}
