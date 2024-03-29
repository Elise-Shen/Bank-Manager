package ATM;

import Accounts.Account;
import Accounts.Chequing;
import Accounts.Saving;
import Actions.Transactions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.Serializable;
import java.util.*;
import java.time.*;

public class User implements Serializable {
    private static final long serialVersionUID = 1163641386372217670L;

    private int userID;
    // transRecord: Map Date to an ArrayList
    private List<Transactions> transRecord = new ArrayList<>();
    private static ObservableList<Transactions> observableTransRecord = FXCollections.observableArrayList();

    private List<Account> accounts = new ArrayList<>();
    private static ObservableList<Account> observableAccounts = FXCollections.observableArrayList();
    private Account primaryAccount;
    private String password;
    private BankManager bankManager;

    private List<String> previousPayees = new ArrayList<>();
    private static ObservableList<String> prevPayees = FXCollections.observableArrayList();

    private boolean isEmployee;


    private int accountRequestCount;
    private int productRequestCount;

    public User (int userID, String password, BankManager bm, boolean isEmployee){
        this.userID = userID;
        this.password = password;
        this.bankManager = bm;
        Account account1 = new Chequing("CAD");
        Account account2 = new Saving("CAD");
        account2.setOwnerID(this.userID);
        account1.setOwnerID(this.userID);
        setPrimaryAccount(account1);
        accounts.add(account1);
        accounts.add(account2);
        this.bankManager.addAllAccountsList(account1);
        this.bankManager.addAllAccountsList(account2);
        this.isEmployee = false;
        previousPayees.add("Water");
        this.isEmployee = isEmployee;
    }
// getter and setter

    public ObservableList<Account> getObservableAccounts(){
        return observableAccounts;
    }

    public void readAllAccounts(){
        observableAccounts.addAll(accounts);
    }


    public int getUserID(){
        return this.userID;
    }

    public void setPassword(String newPassword){
        this.password = newPassword;
        System.out.println("The password is reset (in user.java)");
    }

    public String getPassword(){
        return this.password;
    }

    public void setPrimaryAccount(Account a){
        primaryAccount=a;
    }

    public Account getPrimaryAccount() {
        return primaryAccount;
    }

    /**
     * Return the Array list of account.
     */
    public List<Account> getAccountList(){ return accounts;}

    /**
     * Given account ID, return the corresponding account.
     */
    public Account getAccount(int accountID){
        Account result = null;
        for(Account a: accounts){
            if (a != null && a.getAccountID() == accountID) {
                result = a;
                break;
            }
        }
        return result;

    }

    public BankManager getBankManager(){
        return this.bankManager;
    }

    /**
     * Adds the transaction to the linked HashMap
     * @param t current transaction
     */
    public void addTransactions(Transactions t){

        transRecord.add(t);

    }
    public List<Transactions> getTransactionsList(){
        return transRecord;
    }

    public void readTransactions(){
        observableTransRecord.addAll(transRecord);
    }

    public ObservableList<Transactions> getTransactionsObservableList(){
        return observableTransRecord;
    }

    /**
     * Add new account to account list.
     * @param a the account to be added
     */
    public void addAccount(Account a){
        accounts.add(a);
    }

    // recent transaction record


    public void incrementCount(){
        accountRequestCount += 1;
    }

    public void resetCount(){
        accountRequestCount = 0;
    }

    public int getCount(){
        return accountRequestCount;
    }

    public void addPayee(String payee){
        previousPayees.add(payee);
    }

    public static void readPayees(List<String> list){
        for(String a : list){
            if(!a.equals("")){
                prevPayees.add(a);
            }
        }
    }

    public static ObservableList<String> getPrevPayees(){
        return prevPayees;
    }

    public List<String> getPreviousPayees(){
        return previousPayees;
    }

    public void product_incrementCount(){
        productRequestCount += 1;
    }

    public void product_resetCount(){
        productRequestCount = 0;
    }

    public int product_getCount(){
        return productRequestCount;
    }

    public String toString(){
        return "User ID: "+userID;
    }

    public boolean isEmployee() {return this.isEmployee;}
}

