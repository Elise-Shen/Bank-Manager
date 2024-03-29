package ATM;

import Accounts.*;
import Actions.AccountToAccount;
import Actions.DepositMoney;
import Actions.Transactions;
import Actions.WithdrawMoney;
import javafx.collections.ObservableMap;
import org.apache.commons.lang3.StringUtils;

import javax.money.MonetaryAmount;
import java.io.*;
import java.util.InputMismatchException;

import java.util.*;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;




public class BankManager implements Serializable {

    private static final long serialVersionUID = 474692929576172322L;

    private String bankName;
    private Map<List<Integer>, Map<String, Integer>> accountRequests = new HashMap<>();
    private Map<Integer, List<Transactions>> undoTransactionRequest = new HashMap<>();
    private static ObservableList<Transactions> observableUndoTransacRequest = FXCollections.observableArrayList();

    private List<User>  userArrayList = new ArrayList<>();
    private AccountFactory accountFactory = new AccountFactory();
    private List<Account> allAccounts = new ArrayList<>();
    private String password;

    private final int CHEQUING = 1;
    private final int SAVINGS = 2;
    private final int CREDIT = 3;
    private final int LINE_OF_CREDIT = 4;

    private static ObservableList<User> allUsers = FXCollections.observableArrayList();

    public BankManager(String bankName, String password){
        this.bankName = bankName;
        this.password = password;

        // create 3 users for testing purpose
        User user1 = new User(1, "abc123", this, false);
        User user2 = new User(2, "abc123", this, false);
        User user3 = new User(3, "abc123", this, true);
        userArrayList.add(user1);
        userArrayList.add(user2);
        userArrayList.add(user3);
        //createUserExample(userNumExample);
    }

    /**
     * create a new user and add it to user array list
     * @return
     */
    public User createUser(){
        int numUsers = userArrayList.size();
        User newUser = new User(numUsers+1, "abc123", this, false);
        userArrayList.add(newUser);
        return newUser;
    }

    /**
     * add account to account array
     * @param a
     */
    void addAllAccountsList(Account a){
        allAccounts.add(a);
    }

    String getBankName(){
        return this.bankName;
    }

    public User getUser(int userID){
        User result = null;
        for (User u: userArrayList) {
            if (u.getUserID() == userID) {
                result = u;
                break;
            }
        }
        return result;
    }
    public List<Account> getAccountArrayList(User user) {
        // given user obj -> return a list of account in correspond to the user
        return user.getAccountList();
    }

    public List<Account> getAllAccounts(){
        return allAccounts;
    }

    /**
     * find the account with given id
     * @param id
     * @return
     */

    public Account getOneAccount(int id){
        Account temp = null;
        for (Account a: allAccounts){
            if (a.getAccountID() == id){
                temp = a;
                break;
            }
        }
        return temp;
    }

    /**
     * Return the list of all users.
     */
    public List getUserList() {
        return userArrayList;
    }



    String getPassword(){
        return this.password;
    }

    public void setPassword(User user, String newPassword){
        user.setPassword(newPassword);
    }

    /**
     * Add a new creating account request to the accountRequests list.
     */
    public void requestAccount(int userID, int accountType, String currency){
        Map<String, Integer> value = new TreeMap<>();
        value.put(currency, accountType);
        List<Integer> user = new ArrayList<>();
        user.add(userID);
        accountRequests.put(user, value);
    }

    /**
     * Add a new creating joint account request to the accountRequests list.
     */
    public void requestAccount(int user1, int user2, int accountType, String currency) {
        Map<String, Integer> value = new TreeMap<>();
        value.put(currency, accountType);
        List<Integer> user = new ArrayList<>();
        user.add(user1);
        user.add(user2);
        accountRequests.put(user, value);
    }

    /**
     * Create a new account for the user with specified account type and currency type.
     */
    public void createAccount(List<Integer> userIDs, int accountType, String currency) {
        StringJoiner sj = new StringJoiner(", ");
        Account newAccount = accountFactory.getAccount(accountType, currency);
        for (Integer i : userIDs) {
            addAccount(i, newAccount);
            sj.add(i.toString());
        }
        System.out.println("Created a new " + newAccount + " for User " + sj + ".");
    }

    /**
     * Not private because may use this for other reasons in Phase 2
     * @param userID the ID of the user
     * @param newAccount the Account object of account being requested
     */
    public void addAccount(int userID, Account newAccount){
        User user = getUser(userID);
        newAccount.setOwnerID(userID);
        user.addAccount(newAccount);
        addAllAccountsList(newAccount);
    }

    public Map<List<Integer>, Map<String, Integer>> getAccountRequests(){
        return accountRequests;
    }

    public Map<Integer, List<Transactions>> getUndoTransactionRequest() {
        return undoTransactionRequest;
    }

    public ObservableList<Transactions> getObservableUndoTransacRequest(){
        List<Transactions> requests= undoTransactionRequest.
                values().stream().flatMap(List::stream).collect(Collectors.toList());
        observableUndoTransacRequest.addAll(requests);
        return observableUndoTransacRequest;
    }

    public void addUndoTransactionRequest(int userID, Transactions t){
        if (undoTransactionRequest.containsKey(userID)) {
            undoTransactionRequest.get(userID).add(t);
        } else {
            ArrayList<Transactions> transactions = new ArrayList<>();
            transactions.add(t);
            undoTransactionRequest.put(userID, transactions);
        }
    }

    public void undoTransaction (Transactions t){
        // recentTransaction_to -> recentTransaction_from
        // bills can't undo
        // this 2 variables are updated in every transaction
        MonetaryAmount amountMoved;
        // search user
        Account currentAccount = t.getCurrentAccount();
        // get recent transaction of this user
        if(t instanceof WithdrawMoney){
            amountMoved = ((WithdrawMoney) t).getAmountWithdrawn();
            currentAccount.increaseCurrencyBalance(amountMoved);
            System.out.println("Returned $" + amountMoved +" to the account");
        }else if(t instanceof DepositMoney){
            amountMoved = ((DepositMoney) t).getAmountDeposited();
            currentAccount.decreaseCurrencyBalance(amountMoved);
            System.out.println("Removed $" + amountMoved + " from the account");

        }else if (t instanceof AccountToAccount){
            //int recipientID = (t).getRecipientAccountID();
            Account recipientAccount = ((AccountToAccount) t).getRecipientAccount();
            amountMoved = ((AccountToAccount) t).getAmountTransferred();
            currentAccount.increaseCurrencyBalance(amountMoved);
            recipientAccount.decreaseCurrencyBalance(amountMoved);
            System.out.println("Returned money to original account");
        }
        System.out.println("Transaction is Undone");
    }


    /**
     * Determine whether the user successfully login according to the password entered.
     */
    int checkLogin () {
        User currentUser;
        // check if user exist
        int currentID = 0;
        boolean validInput0 = false;
        boolean validInput1 = false;
        while (!validInput0) {
            try{
            Keypad keyPad = new Keypad();
            currentID = keyPad.getIntInput("\nPlease enter your User ID");
            currentUser = checkUserID(currentID);
            if (currentUser == null) {
                System.out.println("User does not exist. Please try again.");
            } else {
                validInput0 = true;
                while (!validInput1) {
                    // check if password is correct.
                    String pass = keyPad.getStringInput("\nPlease enter your password");
                    if (currentUser.getPassword().equals(pass)) {
                        System.out.println("Successfully Login!");
                        validInput1 = true;
                    } else {
                        System.out.println("Wrong Password. Please try again.");
                    }
                }
            }
        }catch(InputMismatchException ex){System.out.println("Invalid input. Please try again!");}
        }
        return currentID;
    }

    public boolean authenticateUser(String userInput, String password){
        User currentUser;
        if(StringUtils.isNumeric(userInput)){
                currentUser = getUser(Integer.parseInt(userInput));
                if(currentUser == null){
                    return false;
                }
                System.out.println("Authenticated: "+currentUser.getPassword().equals(password));
                return currentUser.getPassword().equals(password);
        }
        return false;
    }

    /**
     * Used for part 2
     * @param t transaction
     */
   //public void addTransaction(Transactions t){
        //listOfTransactions.add(0, t);
    //}

    /**
     * Check if the user with ID userID exists.
     * @param userID the ID to check.
     */
    private User checkUserID(int userID) {
        for (User user : userArrayList) {
            if (user.getUserID() == userID) {
                return user;
            }
        }
        return null;
    }

    public String getAccountName(int type){
        String name= "";
        switch (type){
            case CHEQUING:
                name = "Chequing";
                break;
            case SAVINGS:
                name = "Savings";
                break;
            case CREDIT:
                name = "Credit";
                break;
            case LINE_OF_CREDIT:
                name = "Line of Credit";
                break;
        }
        return name;
    }

    /**
     * Add interest to all savings accounts in this BankManager's account list.
     */
    void addInterests() {
        for (Account a : allAccounts) {
            if (a != null) {
                a.addInterest();
            }
        }
    }


    /**
     * Return the list of account types that has an interest rate tied to them.
     */
    public Map<String, Double> getInterestAccountList() {
        Map<String, Double> map = new HashMap<>();
        map.put("Saving", Saving.getInterestRate());
        map.put("Credit", Credit.getInterestRate());
        map.put("LineOfCredit", LineOfCredit.getInterestRate());
        return map;
    }

    public String toString(){
        return bankName;
    }

    public void readUserList(){
        allUsers.addAll(userArrayList);
    }

    public ObservableList<User> getAllUsers(){
        return allUsers;
    }
}

