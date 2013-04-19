/**
 * @author: Jenny Zhen
 * @name: AtmModel.java
 * @date: 04.24.12
 */

/**
 * $Id: AtmModel.java,v 1.7 2012-05-16 01:33:41 jxz6853 Exp $
 * $Revision: 1.7 $
 * $Log: AtmModel.java,v $
 * Revision 1.7  2012-05-16 01:33:41  jxz6853
 * Completed.
 *
 * Revision 1.6  2012-05-13 19:01:59  jxz6853
 * Fixed multiple log in.
 *
 * Revision 1.5  2012-05-09 14:19:02  jxz6853
 * Fixed all withdraw/deposit/logout.
 *
 * Revision 1.4  2012-05-09 07:26:38  jxz6853
 * Handle errors when putting in bad data and fix closeBank().
 *
 * Revision 1.3  2012-05-09 07:23:21  jxz6853
 * Fix/add cancel button. Swap that with clear.
 *
 * Revision 1.2  2012-05-09 06:32:11  jxz6853
 * GUI works for new deposit/withdraw design.
 *
 * Revision 1.1  2012-05-09 05:29:04  jxz6853
 * Fixing MVC.
 *
 */

/**
 * AtmModel()
 * Can talk to BankModel
 * Can't talk to: AtmGUI, BankGUI, Bank
 */
public class AtmModel {
	private BankModel bankModel;
	
	/**
	 * Constructor: Creates an instance of a ATM model.
	 * @param bankModel - talks to when performing transactions
	 */
	public AtmModel(BankModel bankModel) {
		this.bankModel = bankModel;
	}
	
	/**
	 * Checks to see if the user id exists.
	 * @param userID - id to check
	 * @return true if it exists
	 */
	public int verifyID(String userID) {
		return bankModel.verifyID(userID);
	}
	
	/**
	 * Checks to see if the pin associated with the account token is correct.
	 * @param token - represents account to check
	 * @param userPin - pin to check
	 * @return true if the pin is correct
	*/
	public boolean verifyPin(int token, String userPin) {
		return bankModel.verifyPin(token, userPin);
	}
	
	/**
	 * Attempts to deposit amount into account.
	 * @param token - represents account to deposit into
	 * @param amount - amount of money to deposit
	 * @return true if deposit was successful
	 */
	public boolean atmDeposit(int token, double amount) {
		return bankModel.bankDeposit(token, amount);
	}
	
	/**
	 * Attempts to withdraw amount from account.
	 * @param token - represents account to withdraw from
	 * @param amount - amount of money to withdraw
	 * @return true if withdraw was successful
	 */
	public boolean atmWithdraw(int token, double amount) {
		return bankModel.bankWithdraw(token, amount);
	}
	
	/**
	 * Gets the balance in the account.
	 * @param token - represents account to check
	 * @return the balance in the account
	 */
	public String getBalance(int token) {
		return bankModel.getBalance(token);
	}
	
	/**
	 * Gets the transaction type.
	 * 		0 - check balance
	 * 		1 - deposit
	 * 		2 - withdraw
	 * 		3 - log out
	 * @param value - represents the transaction number
	 * @return the transaction type
	 */
	public char getTType(String value) {
		if(value.equals("0"))
			return 'c';
		else if(value.equals("1"))
			return 'd';
		else if(value.equals("2"))
			return 'w';
		else if(value.equals("3"))
			return 'l';
		return ' ';
	}
	
	/**
	 * Attempts to perform the transaction by type.
	 * 		d - deposit
	 * 		w - withdraw
	 * 		l - log out
	 * @param token - represent the account
	 * @param amount - amount to modify
	 * @param type - type of transaction
	 * @return true if transaction was successful
	 */
	public boolean makeTransaction(int token, String amount, char type) {
		double money = Double.parseDouble(amount);
		if(type == 'd')
			return atmDeposit(token, money);
		else if(type == 'w')
			return atmWithdraw(token, money);
		else if(type == 'l')
		System.err.println("Invalid transaction type.");
		return false;
			
	}
	
	/**
	 * Exits the ATM.
	 */
	public void exit() {
		bankModel.buttonCommands(2);
	}
	
}