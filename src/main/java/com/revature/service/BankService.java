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

    private static Customer customer;

    private static Employee employee;

    private static System system;

    private static final UserDAO userDAO = new UserDAO();

    private static final CustomerDAO customerDAO = new CustomerDAO();

    private static final AccountDAO accountDAO = new AccountDAO();

    private static void createUser() {
        System.out.print("Please enter your username: ");
        String username = sc.next().trim();
        System.out.print("Enter your password: ");
        String password = sc.next().trim();
        user = userDAO.createUser(username, password);
    }

    private static void loginUser() {
        System.out.print("Please enter your username: ");
        String username = sc.next().trim();
        System.out.print("Enter your password: ");
        String password = sc.next().trim();
        user = userDAO.findUserByUsernameAndPassword(username, password);
    }

    public static void dashboard() {
        System.out.print("Enter 1 if you are a registered user or press 2 to register: ");
        int val;
        try {
            val = sc.nextInt();
            if(val == 1) {
                loginUser();
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
        if(customer != null) {
            customerMenu();
        } else if(employee != null) {
            employeeMenu();
        } else if(user != null){
            userMenu();
        } else {
            dashboard();
        }
    }

    private static void userMenu() {
        System.out.print("Greetings " + user.getUsername() + "! Press 1 to apply for a new bank account or 2 if you already have an account: ");
        int input = sc.nextInt();
        if(input == 1) {
            int accountId = applyForAccount();
            customer = customerDAO.createCustomer(user, accountId);
            customerMenu();
        } else if(input == 2){
            customer = customerDAO.findCustomerByUserId(user);
            customerMenu();
        } else {
            System.out.println("That is not a valid option");
            userMenu();
        }
    }

    private static void customerMenu() {
        StringBuilder sb = new StringBuilder();
        System.out.println("Here is your list of options: ");
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
        Account account = accountDAO.getAccountById(customer.getAccountId());
        switch(option) {
            case 1:
                account = accountDAO.getAccountById(customer.getAccountId());
                System.out.println("Your current balance is $" + account.getBalance());
                break;
            case 2:
                System.out.print("Press W to withdraw or D to deposit (W/D): ");
                String input = sc.next();
                double amount;
                if(input.equals("W")) {
                    System.out.printf("Enter the amount you would like to withdraw from $%s: ",
                            BigDecimal.valueOf(account.getBalance()).setScale(2, RoundingMode.HALF_UP));
                    amount = BigDecimal.valueOf(sc.nextDouble()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    if(account.getBalance() - amount >= 0 && amount > 0) {
                        account = accountDAO.updateBalance(account.getBalance() - amount, account.getAccountId());
                    } else {
                        System.out.printf("Sorry that is an invalid amount to withdraw given your funds of $%s",
                                BigDecimal.valueOf(account.getBalance()).setScale(2, RoundingMode.HALF_UP));
                    }
                } else if(input.equals("D")) {
                    System.out.print("Enter the amount you would like to deposit: ");
                    amount = BigDecimal.valueOf(sc.nextDouble()).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    if(amount > 0) {
                        account = accountDAO.updateBalance(account.getBalance() + amount, account.getAccountId());
                    } else {
                        System.out.println("You cannot deposit anything less than or equal to $0.00");
                    }
                } else {
                    System.out.println("That was not a valid option. Taking you back to the customer menu.");
                }
                break;
            case 3:
                //transfer money to different account
                System.out.println("Option 3");
                break;
            case 4:
                //accept transfer
                System.out.println("Option 4");
                break;
            case 5:
                System.out.println("You have chosen to exit");
                customer = null;
                account = null;
                user = null;
                break;
            default:
                System.out.println("Sorry that was not a valid option");
                break;
        }
        customerMenu();
    }

    private static int applyForAccount() throws AccountCreationException {
        System.out.println("Please enter a dollar amount for your account's beginning balance. " +
                "Must be greater than 100.00 and less than 10 million. Ex: 5761.28");
        Account account = null;
        try {
            BigDecimal bd = BigDecimal.valueOf(sc.nextDouble()).setScale(2, RoundingMode.HALF_UP);
            double beginningBalance = bd.doubleValue();
            System.out.println(beginningBalance);
            account = accountDAO.createAccount(beginningBalance);
        } catch (InputMismatchException e) {
            e.printStackTrace();
        }
        assert account != null;
        return account.getAccountId();
    }

}
