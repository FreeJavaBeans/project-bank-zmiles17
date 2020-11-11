package com.revature.service;

import com.revature.exceptions.AccountCreationException;
import com.revature.model.*;
import com.revature.repository.*;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class BankService {

    private final static Logger logger = Logger.getLogger(BankService.class);

    private static final Scanner sc = new Scanner(System.in);

    private static User user;

    private static Customer customer;

    private static Employee employee;

    private static final UserDAO userDAO = new UserDAO();

    private static final CustomerDAO customerDAO = new CustomerDAO();

    private static final AccountDAO accountDAO = new AccountDAO();

    private static final EmployeeDAO employeeDAO = new EmployeeDAO();

    private static final TransactionDAO transactionDAO = new TransactionDAO();

    private static void resetService() {
        user = null;
        customer = null;
        employee = null;
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
        resetService();
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
            logger.info(e.getMessage());
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
            dashboard();
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
            logger.info(e.getMessage());
            customerMenu();
        }
    }

    private static void employeeMenu() {
        System.out.println("1: Approve/deny accounts \n2: View accounts by customer \n3: View log of transactions \n4: Logout");
        int option;
        try {
            option = sc.nextInt();
            switch(option) {
                case 1:
                    viewAccountsForVerification();
                    break;
                case 2:
                    viewCustomerAccountInfo();
                    break;
                case 3:
                    viewTransactions();
                    break;
                case 4:
                    dashboard();
                    break;
                default:
                    employeeMenu();
                    break;
            }
        } catch (InputMismatchException e) {
            logger.info(e.getMessage());
        }
        employeeMenu();
    }

    public static void viewTransactions() {
        List<Transaction> transactions = transactionDAO.getAllTransactions();
        if(transactions.size() != 0) {
            for(Transaction t : transactions) {
                System.out.println("Transaction ID: " + t.getTransactionId() + " Amount: $" + t.getAmount() + " Pending: " + t.isPending());
            }
        } else {
            System.out.println("No transactions currently available.");
        }
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
        if(accounts.size() != 0) {
            accounts.sort(Comparator.comparingInt(Account::getAccountId));
            for(Account acc : accounts) {
                System.out.println("Account ID: " + acc.getAccountId() + " Balance: $" + acc.getBalance() +
                        " Verification Status: " + (acc.isVerified() ? "Verified" : "Unverified"));
            }
            List<Integer> accIds = accounts.stream().map(Account::getAccountId).collect(Collectors.toList());
            System.out.print("Enter the account ID for the account which you would like to approve/deny: ");
            try {
                int id = sc.nextInt();
                if(accIds.contains(id)) {
                    System.out.print("Enter the verification status (true/false): ");
                    boolean verified = sc.nextBoolean();
                    accountDAO.updateVerification(id, verified);
                } else {
                    System.out.println("That was not a valid account ID.");
                }
            } catch (InputMismatchException e) {
                logger.info(e.getMessage());
            }
        } else {
            System.out.println("There are not any accounts pending review.");
        }
        employeeMenu();
    }

    private static void customerMenuSelection(int option) {
        Account account = accountDAO.getAccountById(customer.getAccountId());
        if(!account.isVerified()) {
            System.out.println("Your account has not yet been verified. You will not be able to view the account until an employee verifies it. " +
                    "Taking you back to the dashboard.");
            dashboard();
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
                        accountDAO.updateBalance(account.getBalance() - amount, account.getAccountId());
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
                System.out.println(username);
                User recipient = userDAO.findUserByUsername(username);
                Account receiver = accountDAO.getAccountById(recipient.getUserId());
                System.out.print("Enter the amount you wish to transfer: ");
                double transfer = sc.nextDouble();
                if(transfer <= account.getBalance() && transfer > 0) {
                    transactionDAO.createTransaction(account.getAccountId(), receiver.getAccountId(), transfer);
                } else {
                    System.out.println("That was an invalid amount.");
                }
                break;
            case 4:
                listPendingTransactions(account.getAccountId());
                break;
            case 5:
                System.out.println("You have chosen to exit");
                dashboard();
                break;
            default:
                System.out.println("Sorry that was not a valid option");
                break;
        }
        customerMenu();
    }

    private static void listPendingTransactions(int accountId) {
        List<Transaction> pendingTransactions = transactionDAO.getTransactionsToAccountId(accountId);
        if(pendingTransactions != null && pendingTransactions.size() != 0) {
            pendingTransactions.sort(Comparator.comparingInt(Transaction::getTransactionId));
            for(Transaction t : pendingTransactions) {
                System.out.println("ID: " + pendingTransactions.indexOf(t) + " Amount: $" + t.getAmount());
            }
            System.out.print("Enter the ID of the transaction you wish to accept: ");
            try {
                int id = sc.nextInt();
                Transaction transaction = pendingTransactions.get(id);
                Account fromAcc = accountDAO.getAccountById(transaction.getFromAccountId());
                Account toAcc = accountDAO.getAccountById(transaction.getToAccountId());
                accountDAO.updateBalance(fromAcc.getBalance() - transaction.getAmount(), fromAcc.getAccountId());
                accountDAO.updateBalance(toAcc.getBalance() + transaction.getAmount(), toAcc.getAccountId());
                transactionDAO.updateTransactionStatus(transaction.getTransactionId());
                System.out.println("Transaction completed successfully.");
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        } else {
            System.out.println("You do not have any pending money transfers.");
        }
        customerMenu();
    }

    private static int applyForAccount() throws AccountCreationException {
        System.out.println("Please enter a dollar amount for your account's beginning balance. " +
                "Must be greater than or equal to 100.00 and less than 10 million. Ex: 5761.28");
        Account account = null;
        try {
            BigDecimal bd = BigDecimal.valueOf(sc.nextDouble()).setScale(2, RoundingMode.HALF_UP);
            double beginningBalance = bd.doubleValue();
            account = accountDAO.createAccount(beginningBalance);
            System.out.println("Account created successfully with a starting balance of $" +
                    BigDecimal.valueOf(account.getBalance()).setScale(2, RoundingMode.HALF_UP));
        } catch (InputMismatchException e) {
            logger.info(e.getMessage());
        }
        assert account != null;
        return account.getAccountId();
    }

}
