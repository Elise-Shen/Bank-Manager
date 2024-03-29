How to run this program:
As a customer:(for instructions for BankManager, skip to line 56)
1. Run Main.java in ATM folder
2. The terminal prompts for choice of Customer or BankManager(for instructions for BankManager, skip to line 56)
2. The terminal should prompt for a UserID and Password, an example User has been set up with UserID: 1 Password: abc123
3. After successful login, the user should prompted with the choice of User Action, Transaction or Exit.
    User Actions include:   - View Balance Summary
                            - View Previous Transactions
                            - Net Total
                            - Change Password
                            - View Account Creation Date
                            - Request a New Account
                            - View Account Summary
                            - Change/Set Primary Account
                            - Exit to Main Menu

    Transactions include:   - Deposit
                            - Withdraw
                            - Pay Bills
                            - Transfer Money between Accounts
                            - Exit to Main Menu

    Exit: Logs user out of ATM, will return to step 2 above.
4. After action is completed, the user is brought back to the previous menu for further transactions.


Menu items:
    View Balance Summary: Prompt the user to choose an Account which the Account ID and Balance will be printed to screen.
    View Previous Transactions: Prompt the user to choose an Account which related information of the last transaction will be printed to screen.
                                Prompt the user to choose whether to undo the most recent transaction.
    Net Total: This will print out the net total balance of the user, as a sum of all balances between all of the user's accounts.
    Change Password: Allows the user to change password
    View Account Creation Date: Prompt the user to choose an Account which the Account creation date will be printed to screen.
    Request New Account: Prompts use to choose an account type(chequing/saving/credit/line of credit) to request bank manager for creation
    View Account Summary: Prints to screen the Account Number, AccountID, and Balance.
    Set Primary Account: Prompts user to choose a new Primary Account (default destination for deposits)


Transactions:
    Deposit: Reads from deposits.txt with format "Cash, A, B, C, D" where A, B, C, D are the number of $5, $10, $20, $50 bills respectively or "Cheque, 50"
        Ex. Cash 1, 3, 4, 5 means a deposit of 1x $5bill, 3x $10bill, 4x $20bill, 5x $50bill
        Ex. Cheque, 50 means a deposit of a cheque of $50

    Withdraw: Prompts user for the amount to withdraw, will print out amount of each bill if it is a valid transaction.
        Ex. Withdraw 50 would print "num of $50:1,num of $20: 0,num of $10:0,num of 5:0"

    Pay Bills: Prompts user for Account to pay from, and for amount paid.
        This will pay the specified amount from Account entered if it is a valid transaction.
        Ex. Pay: $50

    Transfer Money: Prompts user for Account to transfer from, recipient AccountID, and amount.
        This will transfer the amount specified to recipient's account and will print a success message on successful completion.
        Ex. Transfer from: 1 , Transfer to: 2, Amount: 50



As Bank Manager: (for customer instructions, see above)
1. Run Main.java in ATM folder
2. The terminal prompts for choice of Customer or BankManager (for customer instructions, see above)
2. The terminal should prompt for a password, an example BankManager has been set up with Password: abc123
3. After successful login, the bank manager should be prompted with the choice of View Account Creation Requests, View Undo Transaction Requests, Restock ATM or Exit.
4. Bank manager is assumed to view all kinds of requests every day.
5. Bank manager should check alert.txt before doing any actions. If there is an unhandled alert, restocking the ATM should take place right away.

Menu items:
    View Account Creation Requests: Allows the bank manager to approve or reject requests from customer to create a new account.
    View Undo Transaction Requests: Allows the bank manager to undo transactions that may have been a mistake or fraudulent.
    Restock this ATM: Allows the bank manager to restock the ATM with bills when running low. See alerts.txt for alerts about low stock.

Note: serialized files path may need to be changed