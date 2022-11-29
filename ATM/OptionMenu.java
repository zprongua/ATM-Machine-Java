import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class OptionMenu {
	Scanner menuInput = new Scanner(System.in);
	DecimalFormat moneyFormat = new DecimalFormat("'$'###,##0.00");
	HashMap<Integer, Account> data = new HashMap<>();

	public void getLogin() {
		boolean end = false;
		int customerNumber = 0;
		int pinNumber = 0;
		while (!end) {
			try {
				System.out.print("\nEnter your customer number: ");
				customerNumber = menuInput.nextInt();
				System.out.print("\nEnter your PIN number: ");
				pinNumber = menuInput.nextInt();
				for (Map.Entry<Integer, Account> integerAccountEntry : data.entrySet()) {
					Account acc = (Account) ((Map.Entry<?, ?>) integerAccountEntry).getValue();
					if (data.containsKey(customerNumber) && pinNumber == acc.getPinNumber()) {
						getAccountType(acc);
						end = true;
						break;
					}
				}
				if (!end) {
					System.out.println("\nWrong Customer Number or Pin Number");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Character(s). Only Numbers.");
			}
		}
	}

	public void getAccountType(Account acc) {
		boolean end = false;
		while (!end) {
			try {
				System.out.print("\n\nSelect the account you want to access: " +
									"\n Type 1 - Checkings Account" +
									"\n Type 2 - Savings Account" +
									"\n Type 3 - Show Account Balances" +
									"\n Type 4 - Change PIN" +
									"\n Type 5 - Exit" +
									"\n\nChoice: ");

				int selection = menuInput.nextInt();

				switch (selection) {
				case 1:
					getChecking(acc);
					break;
				case 2:
					getSaving(acc);
					break;
				case 3:
					System.out.println("\nChecking Account Balance: " + moneyFormat.format(acc.getCheckingBalance()));
					System.out.println("Savings Account Balance: " + moneyFormat.format(acc.getSavingBalance()));
					break;
				case 4:
					changePIN(acc);
					break;
				case 5:
					end = true;
					break;
				default:
					System.out.println("\nInvalid Choice.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
	}

	public void getChecking(Account acc) {
		boolean end = false;
		while (!end) {
			try {
				System.out.print("\nChecking Account: " +
									"\n Type 1 - View Balance" +
									"\n Type 2 - Withdraw Funds" +
									"\n Type 3 - Deposit Funds" +
									"\n Type 4 - Transfer Funds" +
									"\n Type 5 - View Transaction History" +
									"\n Type 6 - Back" +
									"\n\nChoice: ");

				int selection = menuInput.nextInt();

				switch (selection) {
				case 1:
					System.out.println("\nChecking Account Balance: " + moneyFormat.format(acc.getCheckingBalance()));
					break;
				case 2:
					acc.getCheckingWithdrawInput();
					break;
				case 3:
					acc.getCheckingDepositInput();
					break;
				case 4:
					acc.getTransferInput("Checking");
					break;
				case 5:
					String str = String.format("%s%s.log", acc.getCustomerNumber(), "checking");
					System.out.printf("Transaction History\n\n%s%n", getTransactionHistory(str));
					break;
				case 6:
					end = true;
					break;
				default:
					System.out.println("\nInvalid Choice.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
	}

	public void getSaving(Account acc) {
		boolean end = false;
		while (!end) {
			try {
				System.out.print("\nSavings Account: " +
									"\n Type 1 - View Balance" +
									"\n Type 2 - Withdraw Funds" +
									"\n Type 3 - Deposit Funds" +
									"\n Type 4 - Transfer Funds" +
									"\n Type 5 - View Transaction History" +
									"\n Type 6 - Back" +
									"\n\nChoice: ");
				int selection = menuInput.nextInt();
				switch (selection) {
				case 1:
					System.out.printf("\nSavings Account Balance: %s", moneyFormat.format(acc.getSavingBalance()));
					break;
				case 2:
					acc.getSavingWithdrawInput();
					break;
				case 3:
					acc.getSavingDepositInput();
					break;
				case 4:
					acc.getTransferInput("Savings");
					break;
				case 5:
					String str = String.format("%s%s.log", acc.getCustomerNumber(), "savings");
					System.out.printf("Transaction History\n\n%s%n", getTransactionHistory(str));
					break;
				case 6:
					end = true;
					break;
				default:
					System.out.println("\nInvalid Choice.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
	}

	private void changePIN(Account acc) {
	}

	public void createAccount() {
		int cst_no = 0;
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nEnter your customer number ");
				cst_no = menuInput.nextInt();
				for (Map.Entry<Integer, Account> ignored : data.entrySet()) {
					if (!data.containsKey(cst_no)) {
						end = true;
						break;
					}
				}
				if (!end) {
					System.out.println("\nThis customer number is already registered");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
		System.out.println("\nEnter PIN to be registered");
		int pin = menuInput.nextInt();
		data.put(cst_no, new Account(cst_no, pin));
		System.out.println("\nYour new account has been successfuly registered!");
		System.out.println("\nRedirecting to login.............");
		getLogin();
	}

	public void mainMenu() {
		readFromFile();
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\n Type 1 - Login");
				System.out.println(" Type 2 - Create Account");
				System.out.print("\nChoice: ");
				int choice = menuInput.nextInt();
				switch (choice) {
				case 1:
					getLogin();
					end = true;
					break;
				case 2:
					createAccount();
					end = true;
					break;
				default:
					System.out.println("\nInvalid Choice.");
				}
			} catch (InputMismatchException e) {
				System.out.println("\nInvalid Choice.");
				menuInput.next();
			}
		}
		writeToFile();
		System.out.println("\nThank You for using this ATM.\n");
		menuInput.close();
		System.exit(0);
	}

	public void writeToFile() {
		try (BufferedWriter bf = new BufferedWriter(new FileWriter("users.txt"))) {

			for (Map.Entry<Integer, Account> entry : data.entrySet()) {
				int pin = data.get(entry.getKey()).getPinNumber();
				double checking = data.get(entry.getKey()).getCheckingBalance();
				double saving = data.get(entry.getKey()).getSavingBalance();
				bf.write(entry.getKey() + ":" + entry.getKey() + ":" + pin + ":" + checking + ":" + saving);
				bf.newLine();
			}
			bf.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readFromFile() {
		try (
			BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(":");
				int key = Integer.parseInt(parts[0]);
				int user = Integer.parseInt(parts[1]);
				int pin = Integer.parseInt(parts[2]);
				double checking = Double.parseDouble(parts[3]);
				double saving = Double.parseDouble(parts[4]);
				data.put(key, new Account(user, pin, checking, saving));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getTransactionHistory(String fileName) {
		StringBuilder sb = new StringBuilder();

		try (
			BufferedReader br = new BufferedReader(new FileReader(fileName));) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("INFO")) sb.append(line.substring(5)).append("\n");
				else sb.append(line.substring(0, 23)).append("\t");
			}
		} catch (IOException e) {
			System.out.println("bad thing");
		}
		return sb.toString();
	}
}
