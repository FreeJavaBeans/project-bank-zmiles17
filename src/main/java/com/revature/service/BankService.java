package com.revature.service;

import com.revature.model.Employee;
import com.revature.model.User;
import com.revature.model.Customer;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BankService {

    private static Scanner sc = new Scanner(System.in);

    private static User user;

    private static void createOrFindUser(String option) {
        System.out.print("Please enter your username: ");
        String username = sc.next().trim();
        System.out.print("Enter your password: ");
        String password = sc.next().trim();
//        System.out.println("username: " + username + "\npassword: " + password);

        // This needs to be refactored to persist or retrieve data
        user = new User(username, password);


    }

    public static void dashboard() {
        System.out.print("Enter 1 if you are a registered user or press 2 to register: ");
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

    private static void userMenu() {
        System.out.print("Greetings " + user.getUsername() + "! Press Y to register as a customer or N to logout. Y/N ");
        String input = sc.next();
        if(input.equals("Y")) {
            Customer customer = new Customer(user.getUsername(), user.getPassword());
            user = customer;
            customerMenu();
        } else if(input.equals("N")){
            System.out.println("You are being logged out.");
            user = null;
            dashboard();
        } else {
            System.out.println("That is not a valid option");
            userMenu();
        }
    }

    private static void customerMenu() {
        StringBuilder sb = new StringBuilder();
        System.out.println("Greetings valued customer! Here is your list of options: ");
        sb.append("1: Apply for a new bank account \n2: View my account balance \n3: Make a deposit or withdrawal \n");
        sb.append("4: Transfer money to another account \n5: Accept money from another account \n6: Exit");
        System.out.println(sb.toString());
        try {
            int option = sc.nextInt();
            customerMenuSelection(option);
        } catch (InputMismatchException e) {
            System.out.println("Sorry that is not a valid option.");
            customerMenu();
        }
    }

    private static void employeeMenu() {

    }

    private static void systemMenu() {

    }

    private static void customerMenuSelection(int option) {
        switch(option) {
            case 1:
                System.out.println("Option 1 Chosen");
                break;
            case 2:
                System.out.println("Option 2 chosen");
                break;
            case 3:
                System.out.println("Option 3");
                break;
            case 4:
                System.out.println("Option 4");
                break;
            case 5:
                System.out.println("Option 5");
                break;
            case 6:
                System.out.println("You have chosen to exit");
                break;
            default:
                System.out.println("Sorry that was not a valid option");
                break;
        }
    }


}
