/**
 * @author: Jenny Zhen
 * @name: Bank.java
 * @date: 04.24.12
 */

/**
 * $Id: Bank.java,v 1.18 2012-05-16 01:33:41 jxz6853 Exp $
 * $Revision: 1.18 $
 * $Log: Bank.java,v $
 * Revision 1.18  2012-05-16 01:33:41  jxz6853
 * Completed.
 *
 * Revision 1.17  2012-05-15 23:59:17  jxz6853
 * Finished readme.txt/feedback.txt. Need to finish commenting.
 *
 * Revision 1.16  2012-05-15 00:58:33  jxz6853
 * Finish viewing all accts in bank gui.
 *
 * Revision 1.15  2012-05-14 06:53:39  jxz6853
 * Need to fix viewing all accts from bank gui.
 *
 * Revision 1.14  2012-05-14 06:45:55  jxz6853
 * Fixed batch mode to include initial and final bank data.
 *
 * Revision 1.13  2012-05-14 06:12:31  jxz6853
 * Batch mode done.
 *
 * Revision 1.12  2012-05-13 20:00:04  jxz6853
 * Added window adapter to print final bank data when closed using the x.
 *
 * Revision 1.11  2012-05-13 19:01:59  jxz6853
 * Fixed multiple log in.
 *
 * Revision 1.10  2012-05-09 14:19:02  jxz6853
 * Fixed all withdraw/deposit/logout.
 *
 * Revision 1.9  2012-05-09 07:26:38  jxz6853
 * Handle errors when putting in bad data and fix closeBank().
 *
 * Revision 1.8  2012-05-09 05:29:04  jxz6853
 * Fixing MVC.
 *
 * Revision 1.7  2012-05-09 03:24:02  jxz6853
 * Handle errors when putting in bad data and fix closeBank(). Also zeros in file gets removed.
 * 
 * Revision 1.6  2012-05-08 11:58:29  jxz6853
 * Add event listeners.
 * 
 * Revision 1.5  2012-05-08 09:42:12  jxz6853
 * GUI template.
 * 
 * Revision 1.4  2012-05-08 08:46:28  jxz6853
 * Fixed account withdraw/deposit.
 * 
 * Revision 1.3  2012-05-08 07:59:35  jxz6853
 * Fixed account restrictions.
 * 
 * Revision 1.2  2012-05-08 07:19:27  jxz6853
 * Fixed name for saving account.
 * 
 * Revision 1.1  2012-05-08 06:16:16  jxz6853
 * Template.
 * 
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * Bank()
 * Can't talk to BankGUI, AtmGUI, BankModel, AtmModel
 */
public class Bank {
	private static final int MINCD = 500; //minimum balance for CD account
	private Account user; //current user of the account
	private ArrayList<Account> accounts; //list of accounts
	private Scanner input; //reads file input from bank
	private Scanner batch; //reads file input from batch
	private PrintWriter out; //write output to bankFile
	private Bank bank; //bank object
	
	/**
	 * Default constructor with one argument. An empty/nonexistent bank file 
	 * is legal and would start up with no accounts in bank.
	 * @param bankFile - contains all bank account information in the format: 
	 * 						userType userID userPin userBalance
	 */
	public Bank(String bankFile) {
		accounts = new ArrayList<Account>();
		try {
			input = new Scanner(new File(bankFile));
		} catch (FileNotFoundException e) {
			return;
		}
		
		getAccounts();
	}
	
	/**
	 * Constructor for batch file. An empty/nonexistent bank file 
	 * is legal and would start up with no accounts in bank.
	 * @param bankFile - contains all bank account information in the format: 
	 * 						userType userID userPin userBalance
	 * @param batchFile - contains commands to be processed; will not ask for 
	 * 						any input from the user, and all output will be 
	 * 						written to standard output 
	 */
	public Bank(String bankFile, String batchFile) {
		bank = new Bank(bankFile);
		
		try {
			input = new Scanner(new File(bankFile));
			batch = new Scanner(new File(batchFile));
		} catch (FileNotFoundException e) {
			System.err.println("File not found!");
			System.exit(0);
		}
		
		batchMode();
		bank.updateAccounts(bankFile);
	}
	
	/**
	 * Adds all the accounts into the list.
	 * @param filename - bank file containing all account information
	 */
	private void getAccounts() {
		Account acct;
		String acctType;
		String acctID;
		String acctPin;
		double acctBalance;
		
		while(input.hasNext()) {
			String[] line = input.nextLine().split(" ");
			acctType = line[0];
			acctID = line[1];
			acctPin = line[2];
			acctBalance = new Double(line[3]);
			acct = createAcct(acctType, acctID, acctPin, acctBalance);
			accounts.add(acct);
		}
	}
	
	/**
	 * Creates an instance of an account based on its type.
	 * @param type - cd, checking, or saving
	 * @param id - id of the account (unique 4 or more digit integer)
	 * @param pin - pin of the account (4 digit integer)
	 * @param balance - double representing amount in account
	 * @return an Account instance
	 */
	private Account createAcct(String type, String id, 
								String pin, double balance) {
		int cdMin = 500;
		if(type.equalsIgnoreCase("cd")) {
			if(balance < cdMin)
				System.err.println(
					"InsufficientBalance: Not enough to open CD account.");
			return new CDAcct(id, pin, balance);
		} else if(type.equalsIgnoreCase("checking"))
			return new CheckingAcct(id, pin, balance);
		else
			return new SavingAcct(id, pin, balance);
	}
	
	/**
	 * Verifies the user entered account ID.
	 * @param userID - account id to search for
	 * @return token representing account
	 */
	public int verifyID(String userID) {
		for(int i = 0; i < accounts.size(); i++) {
			if(accounts.get(i).getID().equals(userID)) {
				user = accounts.get(i);
				return i;
			}
		} return -1;
	}
	
	/**
	 * Verifies the user entered account Pin.
	 * @param token - represents the account
	 * @param userPin - account pin to check
	 * @return true if pin is correct
	 */
	public boolean verifyPin(int token, String userPin) {
		//retrieve last user attempted
		user = accounts.get(token);
		if(user.verifyPin(userPin)) {
			return true;
		} else //remove last user attempted
			user = null;
		return false;
	}
	
	/**
	 * Verifies the amount user wants to deposit.
	 * @param token - represents the account
	 * @param amount - amount to deposit
	 * @return true if the account can deposit the money
	 */
	public boolean verifyDeposit(int token, double amount) {
		user = accounts.get(token);
		return user.verifyDeposit(amount);
	}
	
	/**
	 * Deposits the money into the user's account.
	 * @param token - represents the account
	 * @param amount - amount to deposit
	 * @return true if deposit was successful
	 */
	public boolean deposit(int token, double amount) {
		user = accounts.get(token);
		return user.deposit(amount);
	}
	
	/**
	 * Verifies the amount user wants to withdraw.
	 * @param token - represents the account
	 * @param amount - amount to withdraw
	 * @return true if the account can withdraw the money
	 */
	public boolean verifyWithdraw(int token, double amount) {
		user = accounts.get(token);
		return user.verifyWithdraw(amount);
	}
	
	/**
	 * Withdraws the amount from the user's account.
	 * @param token - represents the account
	 * @param amount - amount to withdraw
	 * @return true if withdraw was successful
	 */
	public boolean withdraw(int token, double amount) {
		user = accounts.get(token);
		return user.withdraw(amount);
	}
	
	/**
	 * Gets the user's current balance.
	 * @param token - represents the account
	 * @return string representing balance in account.
	 */
	public String getBalance(int token) {
		user = accounts.get(token);
		DecimalFormat deci = new DecimalFormat("0.00");
		return deci.format(user.getBalance());
	}
	
	/**
	 * Writes all account information to bankFile when bank closes in either 
	 * GUI or batch mode.
	 * @param bankFile - file to write bank data to
	 */
	public void updateAccounts(String bankFile) {
		String acct;
		
		try {
			out = new PrintWriter(new FileWriter(bankFile));
		} catch (IOException e1) {
			System.err.println("" +
				"IOException: Could not create print writer for bankFile.");
		}
		
		out.flush();
		for(int i = 0; i < accounts.size(); i++) {
			acct = accounts.get(i).getType() + " " +
					accounts.get(i).getID() + " " +
					accounts.get(i).getPin() + " " +
					accounts.get(i).getBalance() + "\n";
			out.write(acct);
		}
		out.flush();
		out.close();
		
	}
	
	/**
	 * Closes the bank after printing out the bank's final state.
	 * @param bankFile - file to write bank data to
	 */
	public void closeBank(String bankFile) {
		System.out.println(this.getReport(true));
		System.exit(0);
	}
	
	/**
	 * Gets the final report for the user after GUI was closed or if batch 
	 * commands were completed. If for batch mode, initial report is returned.
	 * @param finReport - true if report is for final state
	 * @return string representing the initial/final report for the user
	 */
	private String getReport(boolean finReport) {
		String report = "";
		DecimalFormat deci = new DecimalFormat("0.00");
		if(finReport)
			report += "==========   Final Bank Data ==========\n\n";
		else
			report += "=========   Initial Bank Data =========\n\n";
		report += "Account Type    Account    Balance\n";
		report += "------------    -------    -----------\n";
		
		for(int i = 0; i < accounts.size(); i++) {
			report += String.format("%-16s", accounts.get(i).getType());
			report += String.format("%-11s", accounts.get(i).getID());
			report += String.format("$%11s", 
						deci.format(accounts.get(i).getBalance()) + "\n");
		}
		report += "\n=======================================";
		return report;
	}
	
	/**
	 * Attempts to create an instance of an account using the given information 
	 * in batch mode.
	 * @param type - type of account to create
	 * @param id - user id of account
	 * @param pin - pin of account
	 * @param balance - starting balance
	 * @return string representing account creation if success or fail
	 */
	private String batchCreate(String type, String id, 
						String pin, String balance) {
		double bal = Double.parseDouble(balance);
		DecimalFormat deci = new DecimalFormat("0.00");
		String fbalance = deci.format(bal);
		
		if(type.equals("x"))
			bank.accounts.add(createAcct("checking", id, pin, bal));
		else if(type.equals("s"))
			bank.accounts.add(createAcct("saving", id, pin, bal));
		else if(type.equals("c")) {
			if(bal < MINCD)
				return id + "\to\t" + type + "\t" + 
					"Open: Failed\n";
			bank.accounts.add(createAcct("cd", id, pin, bal));
		}
		return id + "\to\t" + type + "\t" + 
			"Open: Success\t$ " + fbalance + "\n";
	}
	
	/**
	 * Attempts to deposit money into account in batch mode.
	 * @param id - represents account
	 * @param amount - money to deposit
	 * @return string representing successful or failed deposit
	 */
	private String batchDeposit(String id, String amount) {
		DecimalFormat deci = new DecimalFormat("0.00");
		double deposit = Double.parseDouble(amount);
		Account currAcct = null;
		
		for(int i = 0; i < bank.accounts.size(); i++) {
			if(bank.accounts.get(i).getID().matches(id))
				currAcct = bank.accounts.get(i);
		}
		
		if(currAcct == null)
			return id + "\td\t\t$\t" + deci.format(deposit) + "\tFailed\n";
		
		currAcct.deposit(deposit);
		return id + "\td\t\t$\t" + deci.format(deposit) + "\t$ " + 
			deci.format(currAcct.getBalance()) + "\n";
	}
	
	/**
	 * Attempts to withdraw money from account in batch mode.
	 * @param id - represents account
	 * @param amount - money to withdraw
	 * @return string representing successful or failed withdraw
	 */
	private String batchWithdraw(String id, String amount) {
		DecimalFormat deci = new DecimalFormat("0.00");
		double withdraw = Double.parseDouble(amount);
		Account currAcct = null;
		
		for(int i = 0; i < bank.accounts.size(); i++) {
			if(bank.accounts.get(i).getID().matches(id))
				currAcct = bank.accounts.get(i);
		}
		
		if(currAcct == null)
			return id + "\tw\t\t$\t" + deci.format(withdraw) + "\tFailed\n";
		
		currAcct.withdraw(withdraw);
		return id + "\tw\t\t$\t" + deci.format(withdraw) + "\t$ " + 
			deci.format(currAcct.getBalance()) + "\n";
	}
	
	/**
	 * Closes the account in batch mode.
	 * @param id - represents account
	 * @return string representing successful or failed close
	 */
	private String batchClose(String id) {
		DecimalFormat deci = new DecimalFormat("0.00");
		Account currAcct = null;
		
		for(int i = 0; i < bank.accounts.size(); i++) {
			if(bank.accounts.get(i).getID().matches(id))
				currAcct = bank.accounts.get(i);
		}
		
		if(currAcct == null)
			return id + "\tc\tClosed: Failed\n";
		
		bank.accounts.remove(currAcct);
		return id + "\tc\tClosed: Success $\t"+ 
			deci.format(currAcct.getBalance()) + "\n";
	}
	
	/**
	 * Applies interest to all accounts based on their type in batch mode.
	 * @return string representing amount of interest and new balance
	 */
	private String batchInterest() {
		DecimalFormat deci = new DecimalFormat("0.00");
		String message = "";
		double interest;
		
		for(Account user : bank.accounts) {
			interest = user.applyInterest();
			message += user.getID() + "\t$\t" + 
				deci.format(interest) + "\t$\t" + 
					deci.format(user.getBalance()) + "\n"; 
		} return message;
	}
	
	/**
	 * Runs through a series of commands from the file in batch mode.
	 */
	private void batchMode() {
		String message;
		String closeMessage;
		String interestMessage;
		String command;
		
		message = bank.getReport(false) + "\n";
		closeMessage = "=============================================\n\n";
		interestMessage = "============== Interest Report ==============\n" +
				"Account Adjustment      New Balance\n" +
				"------- -----------     -----------\n";
		while(batch.hasNext()) {
			String[] line = batch.nextLine().split(" ");
			command = line[0];
			if(command.equals("o"))
				message += batchCreate(line[1], line[2], line[3], line[4]);
			else if(command.equals("d"))
				message += batchDeposit(line[1], line[2]);
			else if(command.equals("w"))
				message += batchWithdraw(line[1], line[2]);
			else if(command.equals("c"))
				closeMessage += batchClose(line[1]);
			else if(command.equals("a"))
				interestMessage += batchInterest();
		}
		message += "\n" + interestMessage + closeMessage;
		message += "\n" + bank.getReport(true);
		
		System.out.println(message);
	}
	
	/**
	 * Gets the list of all accounts.
	 * @return list of accounts
	 */
	public ArrayList<Account> allAccts() {
		return this.accounts;
	}
	
	/**
	 * Runs the program.
	 * @param args - string[] representing command line arguments in the 
	 * 					format: bankFile batchFile. First argument is required, 
	 * 					second is optional.
	 */
	public static void main(String[] args) {
		Bank bank;
		if(args.length < 1 || args.length > 2) {
			System.err.println("Usage: java Bank bankFile [batchFile]");
			System.exit(0);
		} else if(args.length == 2)
			new Bank(args[0], args[1]);
		else {
			bank = new Bank(args[0]);
			new BankModel(bank, args[0]);
			new BankGUI(new BankModel(bank, args[0]));
		}
	}
}
