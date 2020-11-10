package com.revature.service;

import com.revature.exceptions.AccountCreationException;
import com.revature.model.Account;
import com.revature.model.Employee;
import com.revature.model.User;
import com.revature.model.Customer;
import com.revature.repository.AccountDAO;
import com.revature.repository.CustomerDAO;
import com.revature.repository.EmployeeDAO;
import com.revature.repository.UserDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BankService {

    private static final Scanner sc = new Scanner(System.in);

    private static User user;

    private static Customer customer;

    private static Employee employee;

    private static final UserDAO userDAO = new UserDAO();

    private static final CustomerDAO customerDAO = new CustomerDAO();

    private static final AccountDAO accountDAO = new AccountDAO();

    private static final EmployeeDAO employeeDAO = new EmployeeDAO();

    private static void resetService() {
        user = null;
        customer = null;
        employee = null;
        dashboard();
    }

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

    private static void loginEmployee() {
        System.out.print("Enter your employee username: ");
        String username = sc.next();
        System.out.print("Enter your password: ");
        String password = sc.next();
        employee = employeeDAO.findEmployeeByUsernameAndPassword(username, password);
    }

    public static void dashboard() {
        System.out.print("Enter 1 if you are a registered user, 2 to register, or 3 if you are an employee: ");
        int val;
        try {
            val = sc.nextInt();
            if(val == 1) {
                loginUser();
            } else if(val == 2) {
                createUser();
            } else if(val == 3) {
                loginEmployee();
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
            System.out.println("That is not a valid option. Taking you back to login.");
            resetService();
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
        System.out.println("1: Approve/deny accounts \n2: View accounts by customer \n3: View log of transactions \n4: Logout");
        int option = 0;
        try {
            option = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Incorrect input please type 1, 2, or 3.");
            employeeMenu();
        }
        switch(option) {
            case 1:
                viewAccountsForVerification();
                break;
            case 2:
                viewCustomerAccountInfo();
                break;
            case 3:
                // view log of transactions
                break;
            case 4:
                resetService();
                break;
            default:
                employeeMenu();
                break;
        }
        employeeMenu();
    }

    private static void viewCustomerAccountInfo() {
        System.out.print("Enter the customer username for the account you wish to view: ");
        String username = sc.next();
        Account acc = accountDAO.getAccountByCustomerUsername(username);
        if(acc != null) {
            System.out.println("Balance: $" + acc.getBalance() + " Verification: " + acc.isVerified());
        } else {
            System.out.println("No information found for given username");
        }
    }

    private static void viewAccountsForVerification() {
        List<Account> accounts = accountDAO.getAllAccounts();
        for(Account acc : accounts) {
            System.out.println("Account ID: " + acc.getAccountId() + " Balance: $" + acc.getBalance() +
                    " Verification Status: " + (acc.isVerified() ? "Verified" : "Unverified"));
        }
        List<Integer> accIds = accounts.stream().map(acc -> acc.getAccountId()).collect(Collectors.toList());
        System.out.print("Enter the account ID for the account which you would like to approve/deny: ");
        try {
            int id = sc.nextInt();
            if(accIds.contains(id)) {
                System.out.print("Enter the verification status (true/false): ");
                boolean verified = sc.nextBoolean();
                accountDAO.updateVerification(id, verified);
            }
        } catch (InputMismatchException e) {
            e.printStackTrace();
        } finally {
            employeeMenu();
        }
    }

    private static void customerMenuSelection(int option) {
        Account account = accountDAO.getAccountById(customer.getAccountId());
        if(!account.isVerified()) {
            System.out.println("Your account has not yet been verified. You will not be able to view the account until an employee verifies it. " +
                    "Taking you back to the dashboard.");
            resetService();
        }
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
                System.out.print("Enter the username of the customer you wish to transfer money to: ");
                String username = sc.next();
                User recipient = userDAO.findUserByUsername(username);
                Account receiver = accountDAO.getAccountById(recipient.getUserId());
                System.out.print("Enter the amount you wish to transfer: ");
                double transfer = sc.nextDouble();
                if(transfer <= account.getBalance() && transfer > 0) {
                    //Post a pending transaction
                } else {
                    System.out.println("That was an invalid amount.");
                }
                break;
            case 4:
                //accept transfer
                System.out.println("Option 4");
                break;
            case 5:
                System.out.println("You have chosen to exit");
                resetService();
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
