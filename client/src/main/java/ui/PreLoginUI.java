package ui;

import client.ServerFacade;
import dataaccess.DataAccessException;

import java.util.Scanner;

public class PreLoginUI {
    ServerFacade server;
    Scanner scanner = new Scanner(System.in);
    PostLoginUI postLoginUI;
    boolean quit = false;

    public PreLoginUI(ServerFacade serverFacade) {
        server = serverFacade;
        postLoginUI = new PostLoginUI(server,this);
    }

    public void run() {
        quit = false;
        String username = "[Not Logged In]";
        help();
        while(!quit) {
            System.out.print(username+" >>> ");
            String line = scanner.nextLine();
            switch (line) {
                case "login":
                    login();
                    help();
                    break;

                case "register":
                    register();
                    help();
                    break;

                case "help":
                    help();
                    break;

                case "quit":
                    quit();
                    break;

                case null, default:
                    System.out.println("Invalid command, please try again. (Type help to display available commands.)\n");
                    break;
            }
        }
        System.exit(0);
    }

    public void login() {
        String username, password;
        System.out.println("Login:");
        System.out.print("Enter your username:\n   ");
        String line = scanner.nextLine();
        username = line.strip();
        System.out.print("Enter your password:\n   ");
        line = scanner.nextLine();
        password = line.strip();

        try {
            server.login(username,password);
            postLoginUI.run();
        } catch (DataAccessException e) {
            System.out.println("\nInvalid username or password. \n Returning to main menu.\n");
        }
    }

    public void register() {
        String username, password, email;

        System.out.println("Register:");
        System.out.print("Enter your username:\n   ");
        String line = scanner.nextLine();
        username = line.strip();
        System.out.print("Enter your password:\n   ");
        line = scanner.nextLine();
        password = line.strip();
        System.out.print("Enter your email:\n   ");
        line = scanner.nextLine();
        email = line.strip();

        try {
            server.register(username,password,email);
            postLoginUI.run();
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
        quit = true;
    }

}
