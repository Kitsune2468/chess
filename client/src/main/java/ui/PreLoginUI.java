package ui;

import client.ServerFacade;
import static ui.EscapeSequences.*;
import java.util.Scanner;

public class PreLoginUI {
    ServerFacade server;
    Scanner scanner = new Scanner(System.in);
    PostLoginUI postLoginUI;
    boolean quit = false;
    BoardPrinter printer = new BoardPrinter();

    public PreLoginUI(ServerFacade serverFacade) {
        server = serverFacade;

    }

    public void run() {
        quit = false;
        String username = "[Not Logged In]";
        help();
        while(!quit) {
            printer.ResetConsole();
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
            postLoginUI = new PostLoginUI(server,this,username);
            postLoginUI.run();
        } catch (Exception e) {
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
            postLoginUI = new PostLoginUI(server,this,username);
            postLoginUI.run();
        } catch (Exception e) {
            System.out.println("\nInvalid username or password. \n Returning to main menu.\n");
        }
    }

    private static final String SET_MENU_OPTION = SET_TEXT_COLOR_GREEN;
    public void help() {
        System.out.println("\nHere are your available commands: ");
        System.out.println(SET_MENU_OPTION+"login"+RESET_TEXT_COLOR+" - log in to play chess");
        System.out.println(SET_MENU_OPTION+"register"+RESET_TEXT_COLOR+" - Registers a new user");
        System.out.println(SET_MENU_OPTION+"help"+RESET_TEXT_COLOR+" - Displays the available commands");
        System.out.println(SET_MENU_OPTION+"quit"+RESET_TEXT_COLOR+" - Quits the program");
    }

    public void quit() {
        System.out.println("Quitting program...");
        quit = true;
    }

}
