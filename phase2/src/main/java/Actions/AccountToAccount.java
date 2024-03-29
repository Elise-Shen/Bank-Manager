package Actions;

import ATM.*;
import Accounts.*;

import javax.money.MonetaryAmount;
import java.util.*;


/**
 * Can only transfer to own accounts, or other user's chequing accounts.
 */
public class AccountToAccount extends Transactions {
    // Transfer money from one account to another
    // ask user to input 2 account numbers
    private int currentAccountID;
    private int recipientAccountID;
    private MonetaryAmount amountTransferred;
    private Account currentAccount;
    private Account recipientAccount;

    public AccountToAccount(int currentId, BankManager bankManager) {
        super(currentId, bankManager);
    }

    @Override
    public void execute() {
        BankManager bankManager = getBankManager();
        User currentUser = bankManager.getUser(getUserID());
        List<Account> currentUserAccounts = bankManager.getAccountArrayList(currentUser);//want to return a list of all accounts
        List<Account> allAccounts = bankManager.getAllAccounts();


        for (Account a : currentUserAccounts) {
            if (a != null && !(a instanceof Credit)) {
                System.out.println(a.getAccountID() + " - " + a);
            }
        }

        Keypad keyPad = new Keypad();
        int accountID_from = keyPad.getIntInput("\nType in the accountID money transfer out from");
        Account account_from = currentUser.getAccount(accountID_from);
        currentAccountID = accountID_from;


        for (Account a : allAccounts) {
            System.out.println(a.getAccountID() + " - User " + a.getOwnerID() + "'s " + a);
        }

        int accountID_to = keyPad.getIntInput("\nType in the accountID money transfer out to");
        recipientAccountID = accountID_to;
        Account account_to = bankManager.getOneAccount(accountID_to);


        int amount = keyPad.getIntInput("\nType in the amount of money to transfer");
        amountTransferred = createMoney(amount);
        if (account_from.decreaseCurrencyBalance(createMoney(amount))) {
            // increase, decrease amount
            account_to.increaseCurrencyBalance(createMoney(amount));
            // update recent transaction
            System.out.println("A transaction of amount $" + amount + " is completed");
            System.out.println("from " + accountID_from + account_from + " to " + accountID_to + account_to);
        } else {
            System.out.println("Transaction failed.");
        }

    }

    public boolean executeTransfer(Account to, Account from, MonetaryAmount amount){
        amountTransferred = amount;
        currentAccount = from;
        recipientAccount = to;
        currentAccountID = from.getAccountID();
        recipientAccountID = to.getAccountID();
        MonetaryAmount amountConverted =  HelperMethods.exchangeCurrency(amountTransferred, recipientAccount.getPrimaryCurrency());
        if(currentAccount.decreaseCurrencyBalance(amountTransferred)){
            recipientAccount.increaseCurrencyBalance(amountConverted);
            return true;
        }
        return false;

    }

    @Override
    public int getCurrentAccountID() {
        return currentAccountID;
    }

    @Override
    public int getRecipientAccountID() {
        return recipientAccountID;
    }

    public MonetaryAmount getAmountTransferred() {
        return amountTransferred;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public Account getRecipientAccount(){
        return recipientAccount;
    }

    @Override
    public String toString() {
        return "Transfer of " + amountTransferred + " from User " + currentAccount.getOwnerID() +"'s " +
                currentAccount + " account  to User " +recipientAccount.getOwnerID()+"'s " + recipientAccount;
    }
}

