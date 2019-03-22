package Actions;
import ATM.*;
import Accounts.*;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.List;
import java.util.Locale;

public class WithdrawMoney extends Transactions {

    /**
     * The current account ID.
     */
    private int currentAccountID;

    private MonetaryAmount amountWithdrawn;
    /**
     * The CashStorage of the ATM that performs this action.
     */
    private CashStorage cashStorage;

    public WithdrawMoney(int userID, BankManager bm, CashStorage cs) {
        super(userID, bm);
        this.cashStorage = cs;
    }

    /**
     * Withdraw money from the chosen account of current user, if applicable.
     */
    @Override
    public void execute() {
        BankManager bankManager = getBankManager();
        User currentUser = bankManager.getUser(getUserID());
        boolean validInput = false;
        List<Account> currentUserAccounts = currentUser.getAccountList();
        while (!validInput) {
            for (Account a : currentUserAccounts) {
                if (a != null) {
                    System.out.println(a.getAccountID() + " - " + a);
                }
            }
            Keypad keyPad = new Keypad();
            int accountChoice = keyPad.getIntInput("\nPlease type in the ID of the account that you want " +
                    "to withdraw money from.");
            Account myAccount = currentUser.getAccount(accountChoice);
            if (myAccount == null || myAccount.getAccountType() == 3 || myAccount.getAccountType() == 4) {
                System.out.println("Invalid input. You can only withdraw money from Asset Accounts.");
            } else {
                validInput = true;
                currentAccountID = accountChoice;
                Account currentAccount = currentUser.getAccount(accountChoice);
                boolean validInput1 = false;
                while (!validInput1) {
                    MonetaryAmount balance = currentAccount.getCurrencyBalance();
                    int cashWithdrawn = keyPad.getIntInput("Please enter the amount of money that you " +
                            "want to withdraw.");
                    amountWithdrawn = createMoney(cashWithdrawn);
                    if (currentAccount.getAccountType() == 2 &&
                            (balance.subtract(amountWithdrawn)).isGreaterThanOrEqualTo(createMoney(0))) {
                        boolean withdrawn = cashStorage.withdrawal(cashWithdrawn);
                        if (withdrawn) {
                            currentAccount.decreaseCurrencyBalance(amountWithdrawn);
                            validInput1 = true;
                        }
                    } else if (currentAccount.getAccountType() == 1) {
                        if (balance.isGreaterThanOrEqualTo(createMoney(0)) &&
                                balance.subtract(amountWithdrawn).isGreaterThanOrEqualTo(createMoney(-100))) {
                            boolean withdrawn = cashStorage.withdrawal(cashWithdrawn);
                            if (withdrawn) {
                                currentAccount.decreaseCurrencyBalance(amountWithdrawn);
                                validInput1 = true;
                            }
                        } else {
                            System.out.println("Insufficient balance. Please enter again!");
                        }
                    }

                }
            }
        }
    }

    @Override
    public int getCurrentAccountID() {
        return currentAccountID;
    }

    public MonetaryAmount getAmountWithdrawn(){
        return amountWithdrawn;
    }

    @Override
    public String toString() {
        return "Withdrawal $" + amountWithdrawn + "from account " + currentAccountID;
    }
}


