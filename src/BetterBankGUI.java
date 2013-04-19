/**
 * @author: Jenny Zhen
 * @name: BankGUI.java
 * @date: 04.24.12
 */

/**
 * $Id: BetterBankGUI.java,v 1.6 2012-05-16 01:33:41 jxz6853 Exp $
 * $Revision: 1.6 $
 * $Log: BetterBankGUI.java,v $
 * Revision 1.6  2012-05-16 01:33:41  jxz6853
 * Completed.
 *
 * Revision 1.5  2012-05-13 19:01:59  jxz6853
 * Fixed multiple log in.
 *
 * Revision 1.4  2012-05-09 14:19:02  jxz6853
 * Fixed all withdraw/deposit/logout.
 *
 * Revision 1.3  2012-05-09 07:26:37  jxz6853
 * Handle errors when putting in bad data and fix closeBank().
 *
 * Revision 1.2  2012-05-09 06:31:16  jxz6853
 * Fixing classes implementing account.java.
 *
 * Revision 1.1  2012-05-09 05:29:04  jxz6853
 * Fixing MVC.
 * 
 */

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.swing.*;   // JButton, JFrame
import java.awt.*;  // BorderLayout, GridLayout, Container
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

public class BetterBankGUI extends JFrame{
	private int token = -1;
	private String lastID;
	private String lastPin;
	private Bank bank;
	private static BetterBankGUI GUI;
	private String bankFile;
	private JFrame frame;
	private JLabel message;
	private JTextField box;
	private JButton[] numPad;
	private JButton deposit;
	private JButton withdraw;
	private JButton button; //enter/check balance
	
	public BetterBankGUI(Bank bank, String bankFile) {
		this.bank = bank;
		this.bankFile = bankFile;
		createGUI();
		if(!frame.isVisible())
			bank.closeBank(bankFile);
	}
	
	private void createGUI() {
		Random random = new Random();
		int randomInt = random.nextInt(101);
		
		frame = new JFrame("ATM #" + randomInt);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		//horizontal gap 5, vertical gap 10
	    frame.setLayout(new BorderLayout(5, 15));
	    JPanel sepNorth = new JPanel();
	    sepNorth.setLayout(new GridLayout(2, 1));
	    JPanel sepSouth = new JPanel();
	    sepSouth.setLayout(new BorderLayout(3, 1));
	    sepSouth.setBorder(BorderFactory.createEmptyBorder(0,10,5,10));
	    
	    titleBar(sepNorth);
	    idPanel(sepNorth);
	    numPad(frame);
	    dep_with(sepSouth);
	    enter(sepSouth);
	    exit(sepSouth);
	    
	    frame.add(sepNorth, BorderLayout.NORTH);
	    frame.add(sepSouth, BorderLayout.SOUTH);
	    
	    //Display the window.
	    frame.pack();
	    frame.setVisible(true);
	}
	
	private void titleBar(JPanel panel) {
		JPanel titlePanel = new JPanel();
		
		JLabel label = new JLabel("Welcome to Bank of the World!");
		label.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
		label.setFont(new Font(label.getFont().getFontName(), 
				label.getFont().getStyle(), 15));
        
		titlePanel.add(label);
		panel.add(titlePanel);
	}
	
	private void idPanel(JPanel panel) {
		JPanel idPanel = new JPanel();
		idPanel.setLayout(new GridLayout(2, 1, 5, 5));
		idPanel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		message = new JLabel("Enter your account ID.", JLabel.CENTER);
		idPanel.add(message);
		
		box = new JTextField();
		box.setEditable(false);
		box.setBackground(Color.white);
		box.setHorizontalAlignment(JTextField.CENTER);
		idPanel.add(box);
		
		panel.add(idPanel);
	}
	
	private void numPad(JFrame frame) {
		JPanel numPanel = new JPanel();
		numPanel.setLayout(new GridLayout(4, 3, 5, 5));
		numPanel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		numPad = new JButton[12];
		for(int i = 9; i > -1; i--) {
			numPad[i] = new JButton(i + "");
			numPad[i].setBackground(Color.white);
			numPad[i].setFocusPainted(false);
			numPanel.add(numPad[i]);
		}
		
		BufferedImage back = null;
		try {
			back = ImageIO.read(new File("back.png"));
		} catch (IOException e) {
			System.err.println("IOException: " +
					"Unable to read image for back arrow.");
		}
		numPad[10] = new JButton(".");
		numPad[10].setBackground(Color.white);
		numPad[10].setFocusPainted(false);
		numPanel.add(numPad[10]);
		
		numPad[11] = new JButton(new ImageIcon(back));
		numPad[11].setBackground(Color.white);
		numPad[11].setFocusPainted(false);
		numPanel.add(numPad[11]);
		
		for(int i = 0; i < numPad.length - 1; i++) {
			numPad[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					Object button = event.getSource();
					String buttonVal = "";
					
					for(int i = 0; i < numPad.length; i++) {
						if(button.equals(numPad[i])) {
							lastPin += numPad[i].getText();
							
							if(i == 10 && box.getText().contains("."))
								return;
							if(box.getText() != null)
								buttonVal += box.getText();
							if(message.getText().contains("pin"))
								buttonVal += "*";
							else
								buttonVal += numPad[i].getText();
						}
					}
					box.setText(buttonVal);
				}
			});
		}
		
		numPad[11].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent back) {
				String remLast = "";
				if(box.getText().length() == 0)
					return;
				else {
					remLast = box.getText().substring(0, box.getText().length() - 1);
					box.setText(remLast);
				}
			}
		});
		frame.add(numPanel, BorderLayout.CENTER);
	}
	
	private void dep_with(JPanel panel) {
		JPanel dwPanel = new JPanel();
		dwPanel.setLayout(new GridLayout(1, 2, 10, 10));
		
		deposit = new JButton("Deposit");
		deposit.setBackground(Color.white);
		deposit.setFocusPainted(false);
		deposit.setEnabled(false);
		
		withdraw = new JButton("Withdraw");
		withdraw.setBackground(Color.white);
		withdraw.setFocusPainted(false);
		withdraw.setEnabled(false);
		
		dwPanel.add(deposit);
		dwPanel.add(withdraw);
		
		deposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent deposit) {
				
				if(box.getText() == null)
					return;
				
				double amount = Double.parseDouble(box.getText());
				DecimalFormat deci = new DecimalFormat("0.00");
				String dep = deci.format(amount);
				
				if(bank.deposit(token, amount))
					message.setText("New balance is $" + bank.getBalance(token) + ".");
				else if(!bank.deposit(token, amount))
					message.setText("Unable to deposit $" + dep + ".");
				box.setText(null);
			}
		});
		
		withdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent withdraw) {
				
				if(box.getText() == null)
					return;
				
				double amount = Double.parseDouble(box.getText());
				DecimalFormat deci = new DecimalFormat("0.00");
				String with = deci.format(amount);
				
				if(bank.withdraw(token, amount))
					message.setText("New balance is $" + bank.getBalance(token) + ".");
				else if(!bank.withdraw(token, amount))
					message.setText("Unable to withdraw $" + with + ".");
				box.setText(null);
			}
		});
		
		panel.add(dwPanel, BorderLayout.NORTH);
	}
	
	private void enter(JPanel panel) {
		JPanel enterPanel = new JPanel();
		enterPanel.setLayout(new BorderLayout());
		enterPanel.setBorder(BorderFactory.createEmptyBorder(3,0,3,0));
		
		button = new JButton("Enter");
		enterPanel.add(button, BorderLayout.SOUTH);
		button.setBackground(Color.white);
		button.setFocusPainted(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int correctID;
				boolean correctPin;
				if(box.getText() == null)
					return;
				if(message.getText().equals("Enter your account ID.") || 
						message.getText().equals("Incorrect account ID.")) {
					correctID = bank.verifyID(box.getText());
					if(correctID != -1) {
						message.setText("Enter your account pin.");
						token = correctID;
					} else
						message.setText("Incorrect account ID.");
					lastID = box.getText();
					lastPin = "";
					box.setText(null);
				} else if(message.getText().equals("Enter your account pin.") || 
						message.getText().equals("Incorrect account pin.")) {
					correctPin = bank.verifyPin(token, lastPin);
					if(correctPin) {
						message.setText("Welcome back!");
						deposit.setEnabled(true);
						withdraw.setEnabled(true);
						button.setText("Check balance");
					} else {
						message.setText("Incorrect account pin.");
						lastPin = "";
					} box.setText(null);
				} else
					message.setText("Current balance is $" + bank.getBalance(token) + ".");
			}
		});
		panel.add(enterPanel, BorderLayout.CENTER);
	}
	
	private void exit(JPanel panel) {
		JPanel exitPanel = new JPanel();
		exitPanel.setLayout(new GridLayout());
		
		JButton exit = new JButton("Exit");
		exit.setBackground(Color.white);
		exit.setFocusPainted(false);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				bank.closeBank(bankFile);
				frame.setVisible(false);
			}
		});
		exitPanel.add(exit);
		panel.add(exit, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		Bank newBank;
		if(args.length == 2)
			newBank = new Bank(args[0], args[1]);
		else
			newBank = new Bank(args[0]);
		
		GUI = new BetterBankGUI(newBank, args[0]);
	}
}
