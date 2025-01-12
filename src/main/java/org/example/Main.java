package org.example;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Car myCar = new Car();
		Scanner scanner = new Scanner(System.in);
		System.out.println("################### ------------------ Java container is online ------------------ ###################");
		System.out.println("Test Class ---> " + myCar.getEngineName());
		while (true) {
			System.out.print("This is working ? Input \"Exit\" to exit: ");
			String userInput = scanner.nextLine();
			if ("exit".equalsIgnoreCase(userInput)) {
				break;
			}
		}
	}
}