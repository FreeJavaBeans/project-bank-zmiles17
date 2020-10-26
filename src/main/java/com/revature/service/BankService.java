package com.revature.service;

import com.revature.model.Employee;
import com.revature.model.User;
import com.revature.model.Customer;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BankService {

    private static Scanner sc = new Scanner(System.in);

    private static User user;

    public static void createOrFindUser(String option) {
        System.out.print("Please enter your username: ");
        String username = sc.next().trim();
        System.out.print("Enter your password: ");
        String password = sc.next().trim();
//        System.out.println("username: " + username + "\npassword: " + password);

        // This needs to be refactored to persist or retrieve data
        user = new User(username, password);


    }

    public static void dashboard() {
        System.out.print("Enter 1 if you are a registered User or Customer or press 2 to register for an account: ");
        int val;
        try {
            val = sc.nextInt();
            if(val == 1) {
                createOrFindUser("Login");
            } else if(val == 2) {
                createOrFindUser("Register");
            }
        } catch (InputMismatchException e) {
            sc.next();
            System.out.println("That was an invalid input please try again.");
            dashboard();
        }

        if(user instanceof Customer) {
            customerMenu();
        } else if(user instanceof Employee) {
            employeeMenu();
        } else {
            userMenu();
        }
    }

    public static void userMenu() {
        System.out.print("Greetings " + user.getUsername() + "! Would you like to register yourself as a customer? Y/N ");
        String input = sc.next();
        if(input.equals("Y")) {
            user = new Customer(user.getUsername(), user.getPassword());
            customerMenu();
        } else {
            System.out.println("You are being logged out.");
            user = null;
            dashboard();
        }
    }

    public static void customerMenu() {
        StringBuilder sb = new StringBuilder();
        System.out.println("Greetings valued customer! Here is your list of options: ");
        sb.append("1: Apply for a new bank account \n2: View my account balance \n3: Make a deposit or withdrawal \n");
        sb.append("4: Transfer money to another account \n5: Accept money from another account \n6: Exit");
        System.out.println(sb.toString());
        try {
            int option = sc.nextInt();
            System.out.println("You have chosen option " + option);
        } catch (InputMismatchException e) {
            System.out.println("Sorry that is not a valid option.");
            customerMenu();
        }
    }

    public static void employeeMenu() {

    }

    public static void systemMenu() {

    }
}
