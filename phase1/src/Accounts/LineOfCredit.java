package Accounts;
import java.util.Date;

public class LineOfCredit extends DebtAccount {

    int accountType;

    public LineOfCredit() {
        accountType = 4;
    }

    @Override
    public String toString() {
        return "Line of Credit";
    }

    @Override
    public int getAccountType() {
        return accountType;
    }
}
