import java.util.Scanner;
import java.io.Reader;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Random;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileReader;

public class Hangman {
    static Scanner userInput = new Scanner(System.in);

    public static void main(String[] args) {
        int chosenMenu = displayMenu();
        switch (chosenMenu) {
        case 1:
            game();
            break;
        case 2:
            return;
        }

        renderField();
    }

    private static int displayMenu() {
        System.out.println("Welcome in Hangman game!");
        System.out.println("Choose a menupoint:");
        System.out.println("1. Game");
        System.out.println("2. Quit");
        int chosenMenuPoint = userInput.nextInt();
        return chosenMenuPoint;
    }

    private static void game() {
    }

    private static void displayLeaderboards() {
    }

    private static void renderField() {
        String levels = new String("");
        try (BufferedReader br = new BufferedReader(new FileReader("hangman_draws.txt"))) {
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                lives += sCurrentLine + "\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] lives = levels.split("#");
    }
}
