import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;

public class OptionMenu {
	Scanner menuInput = new Scanner(System.in);
	DecimalFormat moneyFormat = new DecimalFormat("'$'###,##0.00");
	HashMap<Integer, Account> data = new HashMap<>();
	Properties properties = new Properties();

	public void getLogin() throws IOException {
		boolean end = false;
		int customerNumber = 0;
		int pinNumber = 0;
		while (!end) {
			try {
				System.out.print("\nEnter your customer number: ");
				customerNumber = menuInput.nextInt();
				System.out.print("\nEnter your PIN number: ");
				pinNumber = menuInput.nextInt();
				Iterator it = data.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					Account acc = (Account) pair.getValue();
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
				System.out.println("\nSelect the account you want to access: ");
				System.out.println(" Type 1 - Checkings Account");
				System.out.println(" Type 2 - Savings Account");
				System.out.println(" Type 3 - Show Account Balances");
				System.out.println(" Type 4 - Exit");
				System.out.print("\nChoice: ");

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
				System.out.println("\nChecking Account: ");
				System.out.println(" Type 1 - View Balance");
				System.out.println(" Type 2 - Withdraw Funds");
				System.out.println(" Type 3 - Deposit Funds");
				System.out.println(" Type 4 - Transfer Funds");
				System.out.println(" Type 5 - Exit");
				System.out.print("\nChoice: ");

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
				System.out.println("\nSavings Account: ");
				System.out.println(" Type 1 - View Balance");
				System.out.println(" Type 2 - Withdraw Funds");
				System.out.println(" Type 3 - Deposit Funds");
				System.out.println(" Type 4 - Transfer Funds");
				System.out.println(" Type 5 - Exit");
				System.out.print("Choice: ");
				int selection = menuInput.nextInt();
				switch (selection) {
				case 1:
					System.out.println("\nSavings Account Balance: " + moneyFormat.format(acc.getSavingBalance()));
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

	public void createAccount() throws IOException {
		int cst_no = 0;
		boolean end = false;
		while (!end) {
			try {
				System.out.println("\nEnter your customer number ");
				cst_no = menuInput.nextInt();
				Iterator it = data.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					if (!data.containsKey(cst_no)) {
						end = true;
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

	public void mainMenu() throws IOException {
//		data.put(952141, new Account(952141, 191904, 1000, 5000));
//		data.put(123, new Account(123, 123, 20000, 50000));
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
		try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
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
}
