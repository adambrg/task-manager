package org.example;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;

public class Main {
    static final String fileName = "tasks.csv";  //soubor, ze ktereho ziskavame informace
    static String[][] tasks; // pole, do ktereho se zapisuji informace ze souboru

    public static void main(String[] args) { //hlavni metoda, ktera zavola "setOptions" a spusti tim program
        setOptions();
    }

    public static void displayOptions() { //metoda, ktera nam vypise vsechny moznosti
        System.out.println(ConsoleColors.BLUE + "Please select an option: ");
        String[] options = {"add", "remove", "list", "exit"};
        for (String option : options) {
            System.out.println(ConsoleColors.RESET + option);
        }
    }

    public static void setOptions() { //metoda, ktera umoznuje uzivateli vybrat vystup
        Scanner scanner = new Scanner(System.in);
        while (true) {
            retrieveData();
            displayOptions();
            String input = scanner.nextLine();
            switch (input) {
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask();
                    break;
                case "list":
                    showList();
                    break;
                case "exit":
                    exit();
                    return;
                default:
                    System.out.print(ConsoleColors.BLUE + "Please enter a valid option: ");
                    break;
            }
        }
    }

    public static void retrieveData() { //metoda, ktera ziskava informace ze souboru a zapisuje je do pole "tasks"
        File file = new File(fileName);
        try (Scanner scanner = new Scanner(file)) {
            int rowCount = 0;
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                rowCount++;
            }
            tasks = new String[rowCount][];
            scanner.close();
            Scanner scanner2 = new Scanner(file);
            int CurrentRow = 0;
            while (scanner2.hasNextLine()) {
                String line = scanner2.nextLine();
                String[] elements = line.split(", ");
                tasks[CurrentRow] = elements;
                CurrentRow++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            e.getStackTrace();
        }
    }

    public static void addTask() {  //metoda, která nam umoznuje zapisovat informace do souboru
        Scanner scanner = new Scanner(System.in);
        System.out.println(ConsoleColors.BLUE + "Please enter a task: ");
        String name = scanner.nextLine();
        System.out.println(ConsoleColors.BLUE + "Enter a date in the format (YYYY-MM-DD)");
        String date = scanner.nextLine();
        System.out.println(ConsoleColors.BLUE + "Enter the current state (true/false): ");
        String state = scanner.nextLine();
        System.out.println(ConsoleColors.GREEN + "TASK ADDED SUCCESSFULLY" + ConsoleColors.RESET);
        try (PrintWriter Writer = new PrintWriter(new FileWriter(fileName, true))) {
            Writer.println();
            Writer.print(name + ", " + date + ", " + state);
        } catch (IOException e) {
            System.err.println("File not found");
        }
    }

    public static void saveTasksToFile() { // metoda, ktera uklada zmeny do souboru pri "removeTask"
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (int i = 0; i < tasks.length; i++) {
                if (i == tasks.length - 1) {
                    writer.print(String.join(", ", tasks[i]));
                } else {
                    writer.println(String.join(", ", tasks[i]));
                }
            }
        } catch (IOException e) {
            System.err.println("Saving failed.");
        }
    }

    public static void removeTask() { // metoda, ktera odstranuje informace ze souboru
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                showList();
                System.err.println(ConsoleColors.RED + "Enter the task number to remove: " + ConsoleColors.RESET);

                String input = scanner.nextLine();
                int index = Integer.parseInt(input) - 1;

                if (index < 0) {
                    System.err.println(ConsoleColors.RED + "Index cannot be negative." + ConsoleColors.RESET);
                    continue;
                }
                tasks = ArrayUtils.remove(tasks, index);
                System.out.println(ConsoleColors.GREEN + "TASK SUCCESSFULLY DELETED!" + ConsoleColors.RESET);

                saveTasksToFile();
                break;

            } catch (NumberFormatException e) {
                System.err.println(ConsoleColors.RED + "Input must be a NUMBER");
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Index is out of bounds");
            }
        }
    }

    public static void showList() { //metoda, ktera vypise list vsech tasku
        System.out.println(ConsoleColors.BLUE + "List of all tasks:" + ConsoleColors.RESET);
        for (int i = 0; i < tasks.length; i++) {
            System.out.println((i + 1) + " - " + Arrays.toString(tasks[i]));
        }
        if (tasks.length == 0) {
            System.out.println(ConsoleColors.RED + "No tasks found");
        }
    }

    public static void exit() {  //metoda k ukonceni programu
        System.out.println(ConsoleColors.RED + "Exiting application..");
        System.exit(0);
    }
}