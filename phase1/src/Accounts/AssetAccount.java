package Accounts;
import java.util.Date;
import java.time.LocalDate;
public class AssetAccount extends Account {

    /**
     * Creates an Asset Account.
     */
    public AssetAccount() {
        super();
    }

    @Override
    public int getAccountType() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }
}
