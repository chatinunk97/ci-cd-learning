package org.example;

public class Main {
	public static void main(String[] args) {
		Car myCar = new Car();
//		Scanner scanner = new Scanner(System.in);
		System.out.println("####### Java container is online #######");
		System.out.println("Test Class ---> " + myCar.getEngineName());

		// Start a new thread to print numbers from 1 to 100 every second
		Thread numberThread = new Thread(() -> {
			try {
				for (int i = 30; i >= 1; i--) {
					System.out.println("Connected, closing in: " + i);
					Thread.sleep(1000); // Sleep for 1 second before printing the next number
				}
			} catch (InterruptedException e) {

			}
		});

		numberThread.start(); // Start the number thread

		// Wait for user input to exit
//		while (true) {
//			System.out.print("This is working? Input \"Exit\" to exit: ");
//			String userInput = scanner.nextLine();
//			if ("exit".equalsIgnoreCase(userInput)) {
//				break;
//			}
//		}

		// Wait for the number thread to finish before closing the scanner
		try {
			numberThread.join(); // Ensures the main thread waits for the number thread to complete
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
