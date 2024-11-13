package ui;

import client.ServerFacade;
import dataaccess.DataAccessException;

import java.util.Scanner;

public class PreLoginUI {
    ServerFacade server;
    Scanner scanner = new Scanner(System.in);

    public PreLoginUI(ServerFacade serverFacade) {
        server = serverFacade;
    }

    public void run() {
        boolean loggedIn = false;
        String username = "[Not Logged In]";
        help();
        while(!loggedIn) {
            System.out.print(username+" >>> ");
            String line = scanner.nextLine();
            switch (line) {
                case "login":
                    login();
                    break;
                case "help":
                    help();
                    break;
                case "quit":
                    quit();
                    System.exit(0);
                case null, default:
                    System.out.println("Invalid command, please try again. (Type help to display available commands.)\n");
                    break;
            }
        }
    }

    public void login() {
        String username, password;
        System.out.println("\nEnter your username: \n");
        String line = scanner.nextLine();
        username = line.strip();
        System.out.println("\nEnter your password: \n");
        line = scanner.nextLine();
        password = line.strip();

        try {
            server.login(username,password);
        } catch (DataAccessException e) {
            System.out.println("\nInvalid username or password. \n Returning to main menu.\n");
        }
    }

    public void help() {
        System.out.println("\nHere are your available commands: ");
        System.out.println("login - log in to play chess");
        System.out.println("register - Registers a new user");
        System.out.println("help - Displays the available commands");
        System.out.println("quit - Quits the program\n");
    }

    public void quit() {
        System.out.println("Quitting program...");
    }

}
