package Accounts;

import BankProducts.BankProduct;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.List;

public class Saving extends AssetAccount {

    /**
     * Interest Rate
     */
    private static double interestRate = 0.001;

    public List<BankProduct> bankProducts = new ArrayList<>();

    /**
     * Creates a Savings Account.
     */
    public Saving(String currency) {
        super(currency);
    }

    @Override
    public int getAccountType() {
        return 2;
    }

    @Override
    public String toString() {
        return "Savings";
    }

    @Override
    public void addInterest(){
        MonetaryAmount newBalance = getCurrencyBalance().multiply(1 + interestRate);
        setCurrencyBalance(newBalance);
    }


    public static void setInterestRate(double rate){interestRate = rate;}

    public static double getInterestRate(){return interestRate;}

    @Override
    public boolean decreaseCurrencyBalance(MonetaryAmount amount) {
        MonetaryAmount currencyBalance = getCurrencyBalance();
        CurrencyUnit unit = getPrimaryCurrency();
        if (currencyBalance.isLessThan(Money.of(0, unit))) {
            return false;
        } else if (currencyBalance.isGreaterThanOrEqualTo(amount)) {
            MonetaryAmount newBalance = currencyBalance.subtract(amount);
            setCurrencyBalance(newBalance);
        }
        return false;
    }
}


