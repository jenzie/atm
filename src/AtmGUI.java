/**
 * @author: Jenny Zhen
 * @name: AtmGUI.java
 * @date: 04.24.12
 */

/**
 * $Id: AtmGUI.java,v 1.12 2012-05-16 01:40:29 jxz6853 Exp $
 * $Revision: 1.12 $
 * $Log: AtmGUI.java,v $
 * Revision 1.12  2012-05-16 01:40:29  jxz6853
 * Removed debug statements.
 *
 * Revision 1.11  2012-05-16 01:33:41  jxz6853
 * Completed.
 *
 * Revision 1.10  2012-05-15 23:59:16  jxz6853
 * Finished readme.txt/feedback.txt. Need to finish commenting.
 *
 * Revision 1.9  2012-05-13 20:00:03  jxz6853
 * Added window adapter to print final bank data when closed using the x.
 *
 * Revision 1.8  2012-05-13 19:01:59  jxz6853
 * Fixed multiple log in.
 *
 * Revision 1.7  2012-05-09 17:50:17  jxz6853
 * Removed original deposit/withdraw code.
 *
 * Revision 1.6  2012-05-09 17:49:04  jxz6853
 * Add indexing for the user associated to the windows after verifying pin.
 *
 * Revision 1.5  2012-05-09 14:19:02  jxz6853
 * Fixed all withdraw/deposit/logout.
 *
 * Revision 1.4  2012-05-09 07:26:37  jxz6853
 * Handle errors when putting in bad data and fix closeBank().
 *
 * Revision 1.3  2012-05-09 07:23:21  jxz6853
 * Fix/add cancel button. Swap that with clear.
 *
 * Revision 1.2  2012-05-09 06:32:10  jxz6853
 * GUI works for new deposit/withdraw design.
 *
 * Revision 1.1  2012-05-09 05:29:04  jxz6853
 * Fixing MVC.
 *
 */

import java.util.Random;
import javax.swing.*;   // JButton, JFrame
import java.awt.*;  // BorderLayout, GridLayout, Container
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The GUI for the ATM.
 * Only talks to the ATM model.
 */
@SuppressWarnings("serial")
public class AtmGUI extends JFrame{
	private int token = -1; //token representing account
	private AtmModel atmModel; //talks to for transactions
	private JPanel optionPanel; //list of options
	private String lastPin; //last pin entered
	private JFrame frame; //the window
	private JLabel message; //the message box
	private JLabel trans; //transaction message
	private JTextField box; //enter in numbers
	private JButton[] numPad; //numbers to press
	private JButton button; //enter/check balance
	private boolean loggedIn; //user is logged in
	private boolean transaction; //in transaction state
	private char transactionType; //type of transaction
	
	/**
	 * Constructor: Creates instance of ATM GUI.
	 * @param atmModel - talks to for transactions
	 */
	public AtmGUI(AtmModel atmModel) {
		this.atmModel = atmModel;
		this.loggedIn = false;
		this.transaction = false;
		createAtmGUI();
	}
	
	/**
	 * Creates the ATM GUI.
	 */
	private void createAtmGUI() {
		Random random = new Random(); //random number generator
		int randomInt = random.nextInt(101); //# from 0 to 100
		
		//create new window
		frame = new JFrame("Jenny Zhen's ATM #" + randomInt);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setFSize(280,280);
		
		//horizontal gap 5, vertical gap 0
	    frame.setLayout(new BorderLayout());
	    JPanel sepNorth = new JPanel();
	    sepNorth.setLayout(new BorderLayout());
	    sepNorth.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
	    
	    JPanel sepCenter = new JPanel();
	    sepCenter.setLayout(new BorderLayout());
	    sepCenter.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
	    
	    titleBar(sepNorth); //for the window title
	    boxPanel(sepNorth); //for the box to display input
	    enter(sepCenter); //confirms input
	    exit(sepCenter); //closes window
	    numPad(sepCenter); //bunch of numbers
	    
	    frame.add(sepNorth, BorderLayout.NORTH);
	    frame.add(sepCenter, BorderLayout.SOUTH);
	    
	    //Display the window.
	    frame.pack();
	    frame.setVisible(true);
	}
	
	/**
	 * Creates the title message.
	 * @param panel - panel to add title bar to
	 */
	private void titleBar(JPanel panel) {
		//creates new panel
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new GridLayout(1,1));
		
		//creates new label
		JLabel label = new JLabel("Welcome to Bank of the World!");
		label.setFont(new Font(label.getFont().getFontName(), 
				label.getFont().getStyle(), 15));
        
		//adds label to panel
		titlePanel.add(label);
		panel.add(titlePanel, BorderLayout.NORTH);
	}
	
	/**
	 * Creates new text field and the message with the command options.
	 * @param panel - panel to add text field and message to
	 */
	private void boxPanel(JPanel panel) {
		JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new BorderLayout());
		
		message = new JLabel("Enter your account ID.", JLabel.CENTER);
		boxPanel.add(message, BorderLayout.NORTH);
		
		boxPanel.add(commandOptions(), BorderLayout.CENTER);
		
		box = new JTextField();
		box.setEditable(false);
		box.setBackground(Color.white);
		box.setHorizontalAlignment(JTextField.CENTER);
		boxPanel.add(box, BorderLayout.SOUTH);
		
		panel.add(boxPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Creates new labels displaying the command options that are visible 
	 * once the user has logged in.
	 * @return the option panel
	 */
	private JPanel commandOptions() {
		optionPanel = new JPanel();
		optionPanel.setLayout(new GridLayout(5, 1));
		
		//create the new labels
		JLabel Opt0 = new JLabel("[0] Check balance.");
		JLabel Opt1 = new JLabel("[1] Make a deposit.");
		JLabel Opt2 = new JLabel("[2] Make a withdrawal.");
		JLabel Opt3 = new JLabel("[3] Log out.");
		trans = new JLabel("", JLabel.CENTER);
		
		//adds the label to the panel
		optionPanel.add(Opt0);
		optionPanel.add(Opt1);
		optionPanel.add(Opt2);
		optionPanel.add(Opt3);
		optionPanel.add(trans);
		
		optionPanel.setVisible(false);
		
		return optionPanel;
	}
	
	/**
	 * Creates a number pad representing buttons to press.
	 * @param panel - panel to add number pad to
	 */
	private void numPad(JPanel panel) {
		JPanel numPanel = new JPanel();
		numPanel.setLayout(new GridLayout(4, 3, 5, 5));
		
		//create new array of buttons
		numPad = new JButton[12];
		for(int i = 9; i > -1; i--) {
			numPad[i] = new JButton(i + "");
			numPad[i].setBackground(Color.white);
			numPad[i].setFocusPainted(false);
			numPanel.add(numPad[i]);
		}
		
		//button to clear input
		numPad[10] = new JButton("Clear");
		numPad[10].setBackground(Color.white);
		numPad[10].setFocusPainted(false);
		numPanel.add(numPad[10]);
		
		//button to cancel transaction
		numPad[11] = new JButton("Cancel");
		numPad[11].setBackground(Color.white);
		numPad[11].setFocusPainted(false);
		numPanel.add(numPad[11]);
		
		//create action listeners used when buttons are pressed
		for(int i = 0; i < numPad.length - 2; i++) {
			numPad[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					Object button = event.getSource();
					String buttonVal = "";
					
					clearTransMessage();
					//if logged in, set transaction to true and set the type
					if(!transaction && loggedIn) {
						for(int i = 0; i < numPad.length; i++) {
							if(button.equals(numPad[i])) {
								box.setText("");
								buttonVal = i + "";
								transactionType = atmModel.getTType(buttonVal);
								
								transaction = true;
								
								if(transactionType == 'd') {
									trans.setText("Enter deposit amount.");
								} else if(transactionType == 'w') {
									trans.setText("Enter withdraw amount.");
								} else if(transactionType == 'c') {
									trans.setText("Current balance is $" + 
										atmModel.getBalance(token) + ".");
									transaction = false;
									box.setText("");
								} else if(transactionType == 'l') {
									atmModel.exit();
									logout();
								}
								return;
							}
						}
					//otherwise, the user is logging in/performing transaction
					} else if(!loggedIn || transaction) {
						for(int i = 0; i < numPad.length; i++) {
							if(button.equals(numPad[i])) {
								lastPin += numPad[i].getText();
								
								if(loggedIn && transaction) {
									if(transactionType == 'd')
										trans.setText("Depositing...");
									else if(transactionType == 'w')
										trans.setText("Withdrawing...");
								}
								
								if(i == 10 && box.getText().contains("."))
									return;
								if(!box.getText().equals(""))
									buttonVal += box.getText();
								if(message.getText().contains("pin"))
									buttonVal += "*";
								else
									buttonVal += numPad[i].getText();
							}
						}
						box.setText(buttonVal);
					}
				}
			});
		}
		
		//clear the input
		clearTransMessage();
		numPad[10].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent clear) {
				if(box.getText().length() == 0)
					return;
				else {
					lastPin = "";
					box.setText("");
				}
			}
		});
		
		//cancel the transaction
		numPad[11].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent cancel) {
				transaction = false;
				transactionType = ' ';
				box.setText("");
				clearTransMessage();
				if(loggedIn)
					trans.setText("Transaction canceled.");
			}
		});
		
		panel.add(numPanel, BorderLayout.NORTH);
	}
	
	/**
	 * Creates the enter button.
	 * @param panel - panel to add button to
	 */
	private void enter(JPanel panel) {
		JPanel enterPanel = new JPanel();
		enterPanel.setLayout(new BorderLayout());
		enterPanel.setBorder(BorderFactory.createEmptyBorder(3,0,3,0));
		
		//create button
		button = new JButton("Okay");
		enterPanel.add(button, BorderLayout.SOUTH);
		button.setBackground(Color.white);
		button.setFocusPainted(false);
		
		//if button was pressed, perform based on conditions
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				clearTransMessage();
				
				int correctID;
				boolean correctPin;
				if(box.getText().equals(""))
					return;
				
				//user is logging in
				if(message.getText().equals("Enter your account ID.") || 
						message.getText().equals("Incorrect account ID.")) {
					correctID = atmModel.verifyID(box.getText());
					if(correctID != -1) {
						token = correctID;
						message.setText("Enter your account pin.");
					}
					else
						message.setText("Incorrect account ID.");
					lastPin = "";
					box.setText(null);
				} //user is entering in the pin
				else if(message.getText().equals("Enter your account pin.") || 
						message.getText().equals("Incorrect account pin.")) {
					correctPin = atmModel.verifyPin(token, lastPin);
					if(correctPin) {
						loggedIn = true;
						message.setText("Welcome back!");
						optionPanel.setVisible(true);
						setFSize(280,360);
					} else {
						message.setText("Incorrect account pin.");
						lastPin = "";
					} box.setText(null);
				} //user is making a transaction
				else {
					boolean made = atmModel.makeTransaction(token, 
							box.getText(), transactionType);
					if(made) {
						if(transactionType == 'd')
							trans.setText("New balance is $" + 
								atmModel.getBalance(token));
						else if(transactionType == 'w')
							trans.setText("New balance is $" + 
								atmModel.getBalance(token));
					} else if(!made) {
						if(!transaction)
							return;
						trans.setText("Could not make a transaction.");
					}
					box.setText("");
					transaction = false;
				}
			}
		});
		panel.add(enterPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Creates the button to close the ATM window.
	 * @param panel - panel to add button to
	 */
	private void exit(JPanel panel) {
		JPanel exitPanel = new JPanel();
		exitPanel.setLayout(new GridLayout());
		exitPanel.setBorder(BorderFactory.createEmptyBorder(3,0,3,0));
		
		//creates the button
		JButton exit = new JButton("Close");
		exit.setBackground(Color.white);
		exit.setFocusPainted(false);
		
		//closes the window when pressed
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				clearTransMessage();
				
				atmModel.exit();
				token = -1;
				lastPin = "";
				frame.setVisible(false);
			}
		});
		exitPanel.add(exit);
		panel.add(exit, BorderLayout.SOUTH);
	}
	
	/**
	 * Logs the user out when called. Removes all previous information and 
	 * returns window to log in state.
	 */
	private void logout() {
		setFSize(280,280);
		frame.setSize(280, 280);
		loggedIn = false;
		transaction = false;
		token = -1;
		lastPin = "";
		trans.setText("");
		box.setText("");
		message.setText("Enter your account ID.");
		optionPanel.setVisible(false);
	}
	
	/**
	 * Clears the transaction message.
	 */
	private void clearTransMessage() {
		trans.setText("");
	}
	
	/**
	 * Sets the window size as needed.
	 * @param width - width of new size
	 * @param height - height of new size
	 */
	private void setFSize(int width, int height) {
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
	}
}