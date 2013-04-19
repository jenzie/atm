/**
 * @author: Jenny Zhen
 * @name: BankGUI.java
 * @date: 04.24.12
 */

/**
 * $Id: BankGUI.java,v 1.16 2012-05-16 01:51:06 jxz6853 Exp $
 * $Revision: 1.16 $
 * $Log: BankGUI.java,v $
 * Revision 1.16  2012-05-16 01:51:06  jxz6853
 * Changed name of function to update accounts.
 *
 * Revision 1.15  2012-05-16 01:40:29  jxz6853
 * Removed debug statements.
 *
 * Revision 1.14  2012-05-16 01:33:41  jxz6853
 * Completed.
 *
 * Revision 1.13  2012-05-15 23:59:16  jxz6853
 * Finished readme.txt/feedback.txt. Need to finish commenting.
 *
 * Revision 1.12  2012-05-15 00:58:33  jxz6853
 * Finish viewing all accts in bank gui.
 *
 * Revision 1.11  2012-05-13 20:00:03  jxz6853
 * Added window adapter to print final bank data when closed using the x.
 *
 * Revision 1.10  2012-05-09 14:19:02  jxz6853
 * Fixed all withdraw/deposit/logout.
 *
 * Revision 1.9  2012-05-09 07:26:37  jxz6853
 * Handle errors when putting in bad data and fix closeBank().
 *
 * Revision 1.8  2012-05-09 07:23:21  jxz6853
 * Fix/add cancel button. Swap that with clear.
 *
 * Revision 1.7  2012-05-09 06:32:10  jxz6853
 * GUI works for new deposit/withdraw design.
 *
 * Revision 1.6  2012-05-09 05:29:04  jxz6853
 * Fixing MVC.
 * 
 * Revision 1.5  2012-05-09 04:06:49  jxz6853
 * Fixed border.
 *
 * Revision 1.4  2012-05-09 03:24:02  jxz6853
 * Handle errors when putting in bad data and fix closeBank(). Also zeros in file gets removed.
 * 
 * Revision 1.3  2012-05-09 00:37:18  jxz6853
 * Fix numPad.
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * BankGUI()
 * Can talk to BankModel
 * Can't talk to Bank, AtmGUI, AtmModel
 */
public class BankGUI {
	private BankModel bankModel; //talks to for transactions 
	private JFrame frame; //the window
	private JLabel message; //displays messages
	private int origHeight; //original height of window
	private int newHeight; //new height of window
	private JPanel sepCenter; //contains numpad and acct table
	private JPanel acctsPanel; //contains acct table
	private JTextField box; //displays user input
	private JButton[] numPad; //buttons representing numbers
	
	/**
	 * Constructor: Creates instance of bank model.
	 * @param bankModel - talks to for transactions
	 */
	public BankGUI(BankModel bankModel) {
		this.bankModel = bankModel;
		createBankGUI();
	}
	
	/**
	 * Creates the bank GUI.
	 */
	private void createBankGUI() {
		Random random = new Random(); //radom number generator
		int randomInt = random.nextInt(101);
		
		//creates new window
		frame = new JFrame("Jenny Zhen's Bank #" + randomInt);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		//mimics conditions when close button is pressed
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent close) {
				bankModel.exit();
				frame.setVisible(false);
			}
		});
		
		//horizontal gap 5, vertical gap 10
	    frame.setLayout(new BorderLayout(5, 0));
	    JPanel sepNorth = new JPanel();
	    sepNorth.setLayout(new BorderLayout(2, 1));
	    sepNorth.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
	    sepCenter = new JPanel();
	    sepCenter.setLayout(new BorderLayout(2, 1));
	    sepCenter.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
	    JPanel sepSouth = new JPanel();
	    sepSouth.setLayout(new BorderLayout(3, 0));
	    sepSouth.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
	    
	    //components of panel
	    titleBar(sepNorth);
	    boxPanel(sepNorth);
	    viewAccts(sepCenter);
	    numPad(sepCenter);
	    exit(sepSouth);
	    
	    //adds panels to window
	    frame.add(sepNorth, BorderLayout.NORTH);
	    frame.add(sepCenter, BorderLayout.CENTER);
	    frame.add(sepSouth, BorderLayout.SOUTH);
	    
	    //Display the window.
	    frame.pack();
	    frame.setVisible(true);
	    
	    //gets the original height of the window
	    origHeight = frame.getHeight();
	}
	
	/**
	 * Creates the title bar
	 * @param panel - panel to add title bar to
	 */
	private void titleBar(JPanel panel) {
		JPanel titlePanel = new JPanel();
		JLabel label = new JLabel("Welcome to Bank of the World!");
		label.setFont(new Font(label.getFont().getFontName(), 
				label.getFont().getStyle(), 15));
        
		titlePanel.add(label);
		panel.add(titlePanel, BorderLayout.NORTH);
	}
	
	/**
	 * Creates labels representing command options and the text field 
	 * representing buttons pressed.
	 * @param panel - panel to add to
	 */
	private void boxPanel(JPanel panel) {
		JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new BorderLayout());
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new GridLayout(4, 1, 5, 0));
		messagePanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		boxPanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		
		//create labels representing list of commands
		message = new JLabel("Choose an option from below.");
		JLabel Opt0 = new JLabel("[0] Open new ATM window.");
		JLabel Opt1 = new JLabel("[1] View all accounts.");
		JLabel Opt2 = new JLabel("[2] Update all accounts.");
		messagePanel.add(message);
		messagePanel.add(Opt0);
		messagePanel.add(Opt1);
		messagePanel.add(Opt2);

		//creates text field representing input
		box = new JTextField();
		box.setEditable(false);
		box.setBackground(Color.white);
		box.setHorizontalAlignment(JTextField.CENTER);
		boxPanel.add(messagePanel, BorderLayout.NORTH);
		
		panel.add(boxPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Displays table of accounts in bank with only the necessary information.
	 * Updates when a transaction is made and when the update command is 
	 * requested.
	 * @param panel - panel to add table to
	 */
	private void viewAccts(JPanel panel) {
		acctsPanel = new JPanel();
		acctsPanel.setLayout(new GridLayout(1, 1));
		acctsPanel.setVisible(false);
		
		//table information
		String[] columns = {"Account ID", "Type", "Balance"};
		String[][] accts = bankModel.getAccounts();
		
		@SuppressWarnings("serial")
		DefaultTableModel acctsModel = new DefaultTableModel(accts, columns){
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false; //disable table editing
			}
		};
		JTable acctsTable = new JTable(acctsModel);
		acctsModel.fireTableDataChanged(); //when changes are made
		acctsTable.revalidate();
		
		//set the size of window to the correct height based on number of rows
		int width = acctsTable.getPreferredSize().width;
	    newHeight = acctsTable.getRowCount() * acctsTable.getRowHeight();
	    acctsTable.setPreferredScrollableViewportSize(
	    		new Dimension(width, newHeight));
		
	    //adds scroll pane to table
		JScrollPane scrollPane = new JScrollPane(acctsTable);
		acctsPanel.add(scrollPane);
		panel.add(acctsPanel, BorderLayout.NORTH);
	}
	
	/**
	 * Creates number pad representing buttons to press.
	 * @param panel - panel to add to
	 */
	private void numPad(JPanel panel) {
		JPanel numPanel = new JPanel();
		numPanel.setLayout(new GridLayout(4, 3, 5, 5));
		
		//create new array of button
		numPad = new JButton[12];
		for(int i = 9; i > -1; i--) {
			numPad[i] = new JButton(i + "");
			numPad[i].setBackground(Color.white);
			numPad[i].setFocusPainted(false);
			numPanel.add(numPad[i]);
		}
		
		//create button with an image
		BufferedImage back = null;
		try {
			back = ImageIO.read(new File("back.png"));
		} catch (IOException e) {
			System.err.println(
					"IOException: Unable to read image for back arrow.");
		}
		
		//button for decimal value
		numPad[10] = new JButton(".");
		numPad[10].setBackground(Color.white);
		numPad[10].setFocusPainted(false);
		numPanel.add(numPad[10]);
		
		//create back/cancel button
		numPad[11] = new JButton(new ImageIcon(back));
		numPad[11].setBackground(Color.white);
		numPad[11].setFocusPainted(false);
		numPanel.add(numPad[11]);
		
		//create action listeners to perform actions when pressed
		for(int i = 0; i < numPad.length - 1; i++) {
			numPad[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					Object button = event.getSource();
					
					for(int i = 0; i < numPad.length; i++) {
						//if requested to display all accounts
						if(button.equals(numPad[i])) {
							if(i == 1 || i == 2) {
								frame.remove(acctsPanel);
								viewAccts(sepCenter);
								bankModel.updateAccounts();
								acctsPanel.setVisible(true);
								int height = origHeight + newHeight + 25;
								setFSize(frame.getWidth(), height);
							} //otherwise, hide table of accounts
							else {
								if(i != 2) {
									acctsPanel.setVisible(false);
									setFSize(frame.getWidth(), origHeight);
									frame.setSize(frame.getWidth(), origHeight);
								}
								bankModel.buttonCommands(i);
							}
							break;
						}
					} //remove inputted text
					box.setText(null);
				}
			});
		}
		panel.add(numPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Creates the button to close the window.
	 * @param panel - panel to add button to
	 */
	private void exit(JPanel panel) {
		JPanel exitPanel = new JPanel();
		exitPanel.setLayout(new GridLayout());
		
		//create new close button
		JButton exit = new JButton("Close");
		exit.setBackground(Color.white);
		exit.setFocusPainted(false);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				bankModel.exit();
				frame.setVisible(false);
			}
		});
		exitPanel.add(exit);
		panel.add(exit, BorderLayout.SOUTH);
	}
	
	/**
	 * Resizes the window based on state.
	 * @param width - width of new window size
	 * @param height - height of new window size
	 */
	private void setFSize(int width, int height) {
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
	}
}
