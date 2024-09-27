import java.io.*;
import java.util.*;

public class BankManagement {

    static class Account implements Serializable {
        String name;
        String dob;
        String phone;
        String accno;
        int deposit;
        List<String> transactions;
        Account lptr, rptr;

        public Account(String name, String dob, String phone, String accno, int deposit) {
            this.name = name;
            this.dob = dob;
            this.phone = phone;
            this.accno = accno;
            this.deposit = deposit;
            this.transactions = new ArrayList<>();
            this.transactions.add("Account created with initial deposit: " + deposit);
            this.lptr = this.rptr = null;
        }

        void addTransaction(String transaction) {
            transactions.add(transaction);
        }
    }

    static Account start = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        loadAccounts();  // Load accounts from the file at the start

        String password = "RNSIT";
        String pass;

        do {
            System.out.print("Enter the password for access: ");
            pass = scanner.nextLine();

            if (!pass.equals(password)) {
                System.out.println("\nPASSWORD DOESN'T MATCH, PLEASE TRY AGAIN\n");
            }
        } while (!pass.equals(password));

        System.out.println("\n\nPassword Match!\nLOADING MAIN MENU...");

        while (true) {
            System.out.println("\n\n\n\t\t\t\u2588\u2588\u2588\u2588\u2588\u2588\u2588 WELCOME TO THE MAIN MENU \u2588\u2588\u2588\u2588\u2588\u2588\u2588");
            System.out.println("\n\n\t\t1. Create new account\n\t\t2. Display details\n\t\t3. Deposit to existing account\n\t\t4. Withdraw from an existing account\n\t\t5. Edit info from an existing account\n\t\t6. Remove existing account\n\t\t7. Search account\n\t\t8. Sort accounts\n\t\t9. Exit\n\n\n\n\n\t\t Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    createAccounts(scanner);
                    storeAccounts();
                    break;
                case 2:
                    displayAccounts();
                    break;
                case 3:
                    deposit(scanner);
                    storeAccounts();
                    break;
                case 4:
                    withdraw(scanner);
                    storeAccounts();
                    break;
                case 5:
                    edit(scanner);
                    storeAccounts();
                    break;
                case 6:
                    delete(scanner);
                    storeAccounts();
                    break;
                case 7:
                    searchAccount(scanner);
                    break;
                case 8:
                    sortAccounts(scanner);
                    break;
                case 9:
                    exitProgram(scanner);
                    break;
                default:
                    System.out.println("INVALID INPUT, PLEASE TRY AGAIN");
                    break;
            }
        }
    }

    private static void createAccounts(Scanner scanner) {
        System.out.print("Enter the number of ACCOUNTS you want to create: ");
        int n = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        for (int i = 0; i < n; i++) {
            System.out.println("\n*DETAILS OF PERSON " + (i + 1) + "*");
            insertAccount(scanner);
            System.out.println("***");
        }
    }

    private static void insertAccount(Scanner scanner) {
        String name = prompt(scanner, "Enter the NAME: ");
        String dob = prompt(scanner, "Enter the DATE OF BIRTH (dd/mm/yyyy): ", "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/\\d{4}$");
        String phone = prompt(scanner, "Enter the PHONE NUMBER: ", "^\\d{10}$");
        String accno = prompt(scanner, "Enter the ACCOUNT NUMBER: ");
        int deposit = promptInt(scanner, "Enter the DEPOSIT AMOUNT: ");

        Account newAccount = new Account(name, dob, phone, accno, deposit);

        if (start == null) {
            start = newAccount;
        } else {
            Account temp = start;
            while (temp.rptr != null) {
                temp = temp.rptr;
            }
            temp.rptr = newAccount;
            newAccount.lptr = temp;
        }
    }

    private static void displayAccounts() {
        if (start == null) {
            System.out.println("The linked list is empty.");
            return;
        }
        System.out.println("\nTHE BANK INFORMATION IS:");
        System.out.println("**");
        Account temp = start;
        int i = 1;
        while (temp != null) {
            System.out.println("BANK INFO " + i + ".");
            System.out.println("NAME: " + temp.name);
            System.out.println("DATE OF BIRTH: " + temp.dob);
            System.out.println("PHONE NUMBER: " + temp.phone);
            System.out.println("ACCOUNT NUMBER: " + temp.accno);
            System.out.println("DEPOSITED AMOUNT: " + temp.deposit);
            System.out.println("TRANSACTIONS: " + String.join(", ", temp.transactions));
            System.out.println("**");
            temp = temp.rptr;
            i++;
        }
    }

    private static void deposit(Scanner scanner) {
        String accno = prompt(scanner, "Enter the ACCOUNT NUMBER you want to DEPOSIT to: ");
        Account account = findAccountByNumber(accno);
        if (account == null) {
            System.out.println("ACCOUNT " + accno + " DOESN'T EXIST IN OUR DATABASE, PLEASE TRY AGAIN.");
            return;
        }
        System.out.println("YOUR ACCOUNT NUMBER: " + account.accno + " \t BALANCE: " + account.deposit);
        int depositAmount = promptInt(scanner, "Enter the amount you want to DEPOSIT: ");
        account.deposit += depositAmount;
        account.addTransaction("Deposited: " + depositAmount);
        System.out.println("AMOUNT ADDED SUCCESSFULLY!!!");
    }

    private static void withdraw(Scanner scanner) {
        String accno = prompt(scanner, "Enter the ACCOUNT NUMBER you want to WITHDRAW from: ");
        Account account = findAccountByNumber(accno);
        if (account == null) {
            System.out.println("ACCOUNT " + accno + " DOESN'T EXIST IN OUR DATABASE, PLEASE TRY AGAIN.");
            return;
        }
        System.out.println("YOUR ACCOUNT NUMBER: " + account.accno + " \t BALANCE: " + account.deposit);
        int withdrawAmount = promptInt(scanner, "Enter the amount you want to WITHDRAW: ");
        if (account.deposit < withdrawAmount) {
            System.out.println("INSUFFICIENT FUNDS, PLEASE TRY AGAIN!!!!!");
        } else {
            account.deposit -= withdrawAmount;
            account.addTransaction("Withdrawn: " + withdrawAmount);
            System.out.println("AMOUNT WITHDRAWN SUCCESSFULLY");
        }
    }

    private static void edit(Scanner scanner) {
        String accno = prompt(scanner, "Enter the ACCOUNT NUMBER you want to EDIT: ");
        Account account = findAccountByNumber(accno);
        if (account == null) {
            System.out.println("ACCOUNT " + accno + " DOESN'T EXIST IN OUR DATABASE, PLEASE TRY AGAIN.");
            return;
        }
        System.out.println("1. NAME = " + account.name);
        System.out.println("2. DATE OF BIRTH = " + account.dob);
        System.out.println("3. PHONE NUMBER = " + account.phone);
        System.out.println("4. ACCOUNT NUMBER = " + account.accno);
        System.out.println("5. DEPOSITED AMOUNT = " + account.deposit);
        System.out.print("Enter the number you want to edit: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                account.name = prompt(scanner, "Enter the NEW NAME: ");
                break;
            case 2:
                account.dob = prompt(scanner, "Enter the NEW DATE OF BIRTH (dd/mm/yyyy): ", "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/\\d{4}$");
                break;
            case 3:
                account.phone = prompt(scanner, "Enter the NEW PHONE NUMBER: ", "^\\d{10}$");
                break;
            case 4:
                account.accno = prompt(scanner, "Enter the NEW ACCOUNT NUMBER: ");
                break;
            case 5:
                account.deposit = promptInt(scanner, "Enter the NEW DEPOSIT AMOUNT: ");
                break;
            default:
                System.out.println("INVALID INPUT, PLEASE TRY AGAIN.");
                break;
        }
        System.out.println("INFORMATION EDITED SUCCESSFULLY.");
    }

    private static void delete(Scanner scanner) {
        String accno = prompt(scanner, "Enter the ACCOUNT NUMBER you want to REMOVE: ");
        Account account = findAccountByNumber(accno);
        if (account == null) {
            System.out.println("ACCOUNT " + accno + " DOESN'T EXIST IN OUR DATABASE, PLEASE TRY AGAIN.");
            return;
        }
        if (account == start) {
            start = account.rptr;
            if (start != null) {
                start.lptr = null;
            }
        } else {
            account.lptr.rptr = account.rptr;
            if (account.rptr != null) {
                account.rptr.lptr = account.lptr;
            }
        }
        System.out.println("ACCOUNT REMOVED SUCCESSFULLY.");
    }

    private static void searchAccount(Scanner scanner) {
        String accno = prompt(scanner, "Enter the ACCOUNT NUMBER you want to SEARCH: ");
        Account account = findAccountByNumber(accno);
        if (account == null) {
            System.out.println("ACCOUNT " + accno + " DOESN'T EXIST IN OUR DATABASE, PLEASE TRY AGAIN.");
            return;
        }
        System.out.println("ACCOUNT FOUND:");
        System.out.println("NAME: " + account.name);
        System.out.println("DATE OF BIRTH: " + account.dob);
        System.out.println("PHONE NUMBER: " + account.phone);
        System.out.println("ACCOUNT NUMBER: " + account.accno);
        System.out.println("DEPOSITED AMOUNT: " + account.deposit);
        System.out.println("TRANSACTIONS: " + String.join(", ", account.transactions));
    }

    private static void sortAccounts(Scanner scanner) {
        if (start == null) {
            System.out.println("No accounts to sort.");
            return;
        }
        System.out.println("Sort by:");
        System.out.println("1. Name");
        System.out.println("2. Account Number");
        System.out.println("3. Deposit Amount");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        List<Account> accounts = new ArrayList<>();
        Account temp = start;
        while (temp != null) {
            accounts.add(temp);
            temp = temp.rptr;
        }

        switch (choice) {
            case 1:
                accounts.sort(Comparator.comparing(a -> a.name));
                break;
            case 2:
                accounts.sort(Comparator.comparing(a -> a.accno));
                break;
            case 3:
                accounts.sort(Comparator.comparingInt(a -> a.deposit));
                break;
            default:
                System.out.println("INVALID INPUT, PLEASE TRY AGAIN.");
                return;
        }

        start = null;
        for (Account acc : accounts) {
            acc.lptr = acc.rptr = null;
            if (start == null) {
                start = acc;
            } else {
                Account temp2 = start;
                while (temp2.rptr != null) {
                    temp2 = temp2.rptr;
                }
                temp2.rptr = acc;
                acc.lptr = temp2;
            }
        }
        System.out.println("ACCOUNTS SORTED SUCCESSFULLY.");
    }

    private static void exitProgram(Scanner scanner) {
        System.out.println("Exiting program...");
        scanner.close();
        System.exit(0);
    }

    private static String prompt(Scanner scanner, String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    private static String prompt(Scanner scanner, String message, String regex) {
        String input;
        while (true) {
            System.out.print(message);
            input = scanner.nextLine();
            if (input.matches(regex)) {
                break;
            } else {
                System.out.println("Invalid input, please try again.");
            }
        }
        return input;
    }

    private static int promptInt(Scanner scanner, String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input, please enter a number.");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        return input;
    }

    private static Account findAccountByNumber(String accno) {
        Account temp = start;
        while (temp != null) {
            if (temp.accno.equals(accno)) {
                return temp;
            }
            temp = temp.rptr;
        }
        return null;
    }

    private static void storeAccounts() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("accounts.dat"))) {
            out.writeObject(start);
        } catch (IOException e) {
            System.out.println("Error storing accounts: " + e.getMessage());
        }
    }

    private static void loadAccounts() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("accounts.dat"))) {
            start = (Account) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }
}
