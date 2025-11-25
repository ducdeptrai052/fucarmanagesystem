package fucarrentingsystem;

import fucarrentingsystem.entity.Account;
import fucarrentingsystem.entity.Customer;

public class SessionContext {
    private static Account currentAccount;
    private static Customer currentCustomer;

    public static Account getCurrentAccount() {
        return currentAccount;
    }

    public static void setCurrentAccount(Account acc) {
        currentAccount = acc;
    }

    public static Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public static void setCurrentCustomer(Customer c) {
        currentCustomer = c;
    }

    public static void clear() {
        currentAccount = null;
        currentCustomer = null;
    }
}
