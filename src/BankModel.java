import java.text.DecimalFormat;

/**
 * @author: Jenny Zhen
 * @name: BankModel.java
 * @date: 04.24.12
 */

/**
 * $Id: BankModel.java,v 1.9 2012-05-16 01:51:06 jxz6853 Exp $
 * $Revision: 1.9 $
 * $Log: BankModel.java,v $
 * Revision 1.9  2012-05-16 01:51:06  jxz6853
 * Changed name of function to update accounts.
 *
 * Revision 1.8  2012-05-16 01:33:42  jxz6853
 * Completed.
 *
 * Revision 1.7  2012-05-15 23:59:17  jxz6853
 * Finished readme.txt/feedback.txt. Need to finish commenting.
 *
 * Revision 1.6  2012-05-15 00:58:33  jxz6853
 * Finish viewing all accts in bank gui.
 *
 * Revision 1.5  2012-05-13 19:01:59  jxz6853
 * Fixed multiple log in.
 *
 * Revision 1.4  2012-05-09 14:19:03  jxz6853
 * Fixed all withdraw/deposit/logout.
 *
 * Revision 1.3  2012-05-09 07:26:38  jxz6853
 * Handle errors when putting in bad data and fix closeBank().
 *
 * Revision 1.2  2012-05-09 06:32:11  jxz6853
 * GUI works for new deposit/withdraw design.
 *
 * Revision 1.1  2012-05-09 05:29:05  jxz6853
 * Fixing MVC.
 *
 */

/**
 * BankModel()
 * Can talk to Bank
 * Can't talk to BankGUI, AtmGUI, AtmModel
 *
 */
public class BankModel {
	private Bank bank; //only one who talks to bank
	private String bankFile; //file containing bank info
	
	/**
	 * Constructor: Creates instance of bank model.
	 * @param bank - talks to for transactions
	 * @param bankFile - file representing bank info
	 */
	public BankModel(Bank bank, String bankFile) {
		this.bank = bank;
		this.bankFile = bankFile;
	}
	
	/**
	 * Updates the account information when asked to be displayed to users.
	 */
	public void updateAccounts() {
		bank.updateAccounts(bankFile);
	}
	
	/**
	 * Performs the command requested.
	 * @param command - command value
	 */
	public void buttonCommands(int command) {
		if(command == 0) {
			AtmModel atmModel = new AtmModel(this);
			new AtmGUI(atmModel);
		} else if(command == 1)
			updateAccounts();
		else if(command == 2)
			bank.updateAccounts(bankFile);
		else
			System.err.println("Invalid option.");
	}
	
	/**
	 * Gets the list of all accounts and returns only necessary info.
	 * @return list of accounts
	 */
	public String[][] getAccounts() {
		updateAccounts();
		DecimalFormat deci = new DecimalFormat("0.00");
		String[][] accts = new String[bank.allAccts().size()][3];
		for(int i = 0; i < bank.allAccts().size(); i++) {
			accts[i][0] = bank.allAccts().get(i).getID();
			accts[i][1] = bank.allAccts().get(i).getType();
			accts[i][2] = deci.format(bank.allAccts().get(i).getBalance());
		}
		return accts;
	}
	
	/**
	 * Closes the bank after updating bank information into file.
	 */
	public void exit() {
		bank.closeBank(bankFile);
	}
	
	/**
	 * Checks to see if the user id exists.
	 * @param userID - id to check
	 * @return true if it exists
	 */
	public int verifyID(String userID) {
		return bank.verifyID(userID);
	}
	
	/**
	 * Checks to see if the pin associated with the account token is correct.
	 * @param token - represents account to check
	 * @param userPin - pin to check
	 * @return true if the pin is correct
	*/
	public boolean verifyPin(int token, String userPin) {
		return bank.verifyPin(token, userPin);
	}
	
	/**
	 * Attempts to deposit amount into account.
	 * @param token - represents account to deposit into
	 * @param amount - amount of money to deposit
	 * @return true if deposit was successful
	 */
	public boolean bankDeposit(int token, double amount) {
		return bank.deposit(token, amount);
	}
	
	/**
	 * Attempts to withdraw amount from account.
	 * @param token - represents account to withdraw from
	 * @param amount - amount of money to withdraw
	 * @return true if withdraw was successful
	 */
	public boolean bankWithdraw(int token, double amount) {
		return bank.withdraw(token, amount);
	}
	
	/**
	 * Gets the balance in the account.
	 * @param token - represents account to check
	 * @return the balance in the account
	 */
	public String getBalance(int token) {
		return bank.getBalance(token);
	}
}
