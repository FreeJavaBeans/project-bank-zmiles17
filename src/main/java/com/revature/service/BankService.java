package com.revature.service;

import com.revature.exceptions.AccountCreationException;
import com.revature.model.Account;
import com.revature.model.Employee;
import com.revature.model.User;
import com.revature.model.Customer;
import com.revature.repository.AccountDAO;
import com.revature.repository.CustomerDAO;
import com.revature.repository.UserDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BankService {

    private static final Scanner sc = new Scanner(System.in);

    private static User user;

    private static final UserDAO userDAO = new UserDAO();

    private static final CustomerDAO customerDAO = new CustomerDAO();

    private static final AccountDAO accountDAO = new AccountDAO();

    private static void createUser() {
        System.out.print("Please enter your username: ");
        String username = sc.next().trim();
        System.out.print("Enter your password: ");
        String password = sc.next().trim();
        user = userDAO.createUser(username, password);
        System.out.println(user.getUsername());
    }

    private static void findUser() {

    }

    public static void dashboard() {
        System.out.print("Enter 1 if you are a registered user or press 2 to register: ");
        int val;
        try {
            val = sc.nextInt();
            if(val == 1) {
                findUser();
            } else if(val == 2) {
                createUser();
            } else {
                System.out.println("That was not a valid option.");
            }
        } catch (InputMismatchException e) {
            sc.next();
            System.out.println("That was an invalid input please try again.");
        }
        chooseMenu();
    }

    private static void chooseMenu() {
        if(user instanceof Customer) {
            customerMenu();
        } else if(user instanceof Employee) {
            employeeMenu();
        } else if(user != null){
            userMenu();
        } else {
            dashboard();
        }
    }

    private static void userMenu() {
        System.out.print("Greetings " + user.getUsername() + "! Press Y to apply for a bank account or N to exit. Y/N ");
        String input = sc.next();
        if(input.equals("Y")) {
            int accountId = applyForAccount();
            user = customerDAO.createCustomer(user, accountId);
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
        sb.append("1: View my account balance \n2: Make a deposit or withdrawal \n");
        sb.append("3: Transfer money to another account \n4: Accept money from another account \n5: Exit");
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
                System.out.println("Option 1");
                break;
            case 2:
                System.out.println("Option 2");
                break;
            case 3:
                System.out.println("Option 3");
                break;
            case 4:
                System.out.println("Option 4");
                break;
            case 5:
                System.out.println("You have chosen to exit");
                break;
            default:
                System.out.println("Sorry that was not a valid option");
                break;
        }
    }

    private static int applyForAccount() throws AccountCreationException {
        System.out.println("Please enter a dollar amount for your account's beginning balance. Must be greater than 100.00 and less than 10 million. Ex: 5761.28");
        Account account = null;
        try {
            BigDecimal bd = new BigDecimal(sc.nextDouble()).setScale(2, RoundingMode.HALF_UP);
            double beginningBalance = bd.doubleValue();
            System.out.println(beginningBalance);
            account = accountDAO.createAccount(beginningBalance);
        } catch (InputMismatchException e) {
            e.printStackTrace();
        }
        return account.getAccountId();
    }

}
