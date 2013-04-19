/**
 * @author: Jenny Zhen
 * @name: SavingsAcct.java
 * @date: 04.24.12
 */

/**
 * $Id: SavingAcct.java,v 1.10 2012-05-16 01:48:37 jxz6853 Exp $
 * $Revision: 1.10 $
 * $Log: SavingAcct.java,v $
 * Revision 1.10  2012-05-16 01:48:37  jxz6853
 * Added class names in class descriptions.
 *
 * Revision 1.9  2012-05-16 01:33:41  jxz6853
 * Completed.
 *
 * Revision 1.8  2012-05-14 06:12:32  jxz6853
 * Batch mode done.
 *
 * Revision 1.7  2012-05-09 14:19:03  jxz6853
 * Fixed all withdraw/deposit/logout.
 *
 * Revision 1.6  2012-05-09 07:26:38  jxz6853
 * Handle errors when putting in bad data and fix closeBank().
 *
 * Revision 1.5  2012-05-09 00:31:16  jxz6853
 * Fixing classes implementing account.java.
 *
 * Revision 1.4  2012-05-09 05:29:04  jxz6853
 * Fixing MVC.
 * 
 * Revision 1.3  2012-05-08 08:46:28  jxz6853
 * Fixed account withdraw/deposit.
 * 
 * Revision 1.2  2012-05-08 07:59:35  jxz6853
 * Fixed account restrictions.
 * 
 * Revision 1.1  2012-05-08 07:19:27  jxz6853
 * Fixed name for saving account.
 * 
 */

/**
 * SavingAcct() represents a savings account.
 */
public class SavingAcct implements Account {
	private static final int MINBALANCE = 200; //minimum account balance
	private final String type; //type of account
	private final String ID; //user id of account
	private final String pin; //pin of account
	private double balance; //amount of money
	
	/**
	 * Constructor: creates an instance of a savings account.
	 * @param ID - user ID
	 * @param pin - user pin
	 * @param balance - starting balance
	 */
	public SavingAcct(String ID, String pin, double balance) {
		this.type = "Saving";
		this.ID = ID;
		this.pin = pin;
		this.balance = balance;
	}
	
	/**
	 * Gets the account type.
	 * @return String representing the type of account. In this case, savings.
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * Gets the user id associated with this account.
	 * @return the user id
	 */
	public final String getID() {
		return this.ID;
	}
	
	/**
	 * Gets the user pin associated with this account.
	 * @return the user pin
	 */
	public final String getPin() {
		return this.pin;
	}
	
	/**
	 * Checks to see if the pin associated with the account is correct.
	 * @param input - the pin that the user entered
	 * @return true if pin is correct
	 */
	public boolean verifyPin(String input) {
		if(input.equals(this.pin))
			return true;
		return false;
	}

	/**
	 * Gets the current balance in the account.
	 * @return account balance
	 */
	public double getBalance() {
		return this.balance;
	}

	/**
	 * Checks to see if the amount can be deposited into the account.
	 * @param amount - amount of money to deposit
	 * @return true if amount can be deposited
	 */
	public boolean verifyDeposit(double amount) {
		if(amount > 0)
			return true;
		return false;
	}

	/**
	 * Deposits the amount into the account after checking that the amount 
	 * could be deposited.
	 * @param amount - amount of money to deposit
	 * @return true if amount was deposited
	 */
	public boolean deposit(double amount) {
		if(verifyDeposit(amount)) {
			this.balance += amount;
			return true;
		} return false;
	}

	/**
	 * Checks to see if the amount can be withdrawn from the account.
	 * @param amount - amount of money to withdraw
	 * @return true if the money can be withdrawn
	 */
	public boolean verifyWithdraw(double amount) {
		if(balance - amount < 0)
			return false;
		return true;
	}

	/**
	 * Withdraws the amount from the account after checking that the amount 
	 * could be withdrawn.
	 * @param amount - amount of money to withdraw
	 * @return true if th emoney was withdrawn
	 */
	public boolean withdraw(double amount) {
		if(verifyWithdraw(amount)) {
			this.balance -= amount;
			return true;
		} return false;
	}
	
	/**
	 * Checks to see if the account balance has reached below the minimum. If 
	 * it is not, the interest can be applied. Otherwise, apply penalty.
	 */
	public void belowMinimum() {
		if(balance < MINBALANCE)
			applyPenalty();
		else
			applyInterest();
	}
	
	/**
	 * Applies a penalty to the account for going below the minimum balance.
	 */
	public void applyPenalty() {
		if(balance > 10)
			balance -= 10;
		else
			balance -= (balance / 10);
	}

	/**
	 * Applies interest to the account.
	 */
	public double applyInterest() {
		double interest = balance * (0.005 / 12);
		balance += interest;
		return interest;
	}

}