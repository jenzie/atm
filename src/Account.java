/**
 * @author: Jenny Zhen
 * @name: Account.java
 * @date: 04.24.12
 */

/**
 * $Id: Account.java,v 1.9 2012-05-16 01:33:41 jxz6853 Exp $
 * $Revision: 1.9 $
 * $Log: Account.java,v $
 * Revision 1.9  2012-05-16 01:33:41  jxz6853
 * Completed.
 *
 * Revision 1.8  2012-05-14 06:12:31  jxz6853
 * Batch mode done.
 *
 * Revision 1.7  2012-05-09 14:19:02  jxz6853
 * Fixed all withdraw/deposit/logout.
 *
 * Revision 1.6  2012-05-09 07:26:38  jxz6853
 * Handle errors when putting in bad data and fix closeBank().
 *
 * Revision 1.5  2012-05-09 00:27:13  jxz6853
 * Testing commit.
 *
 * Revision 1.4  2012-05-09 00:26:03  jxz6853
 * Testing commit.
 * 
 * Revision 1.3  2012-05-08 08:46:28  jxz6853
 * Fixed account withdraw/deposit.
 * 
 * Revision 1.2  2012-05-08 07:59:35  jxz6853
 * Fixed account restrictions.
 * 
 * Revision 1.1  2012-05-08 06:16:16  jxz6853
 * Template.
 * 
 */

/**
 * Interface for an account.
 */
public interface Account {
	
	//Gets the account type. 
	public String getType();
	
	//Gets the user id associated with this account.
	public String getID();
	
	//Gets the user pin associated with this account.
	public String getPin();
	
	//Checks to see if the pin associated with the account is correct.
	public boolean verifyPin(String input);
	
	//Gets the current balance in the account.
	public double getBalance();
	
	//Checks to see if the amount can be deposited into the account.
	public boolean verifyDeposit(double amount);
	
	//Deposits the amount into the account after checking that the amount 
	// could be deposited.
	public boolean deposit(double amount);
	
	//Checks to see if the amount can be withdrawn from the account.
	public boolean verifyWithdraw(double amount);
	
	// Withdraws the amount from the account after checking that the amount 
	// could be withdrawn.
	public boolean withdraw(double amount);
	
	//Checks to see if the account balance has reached below the minimum. If 
	// it is not, the interest can be applied. Otherwise, apply penalty.
	public void belowMinimum();
	
	//Applies a penalty to the account for going below the minimum balance.
	public void applyPenalty();
	
	//Applies interest to the account.
	public double applyInterest();
}
