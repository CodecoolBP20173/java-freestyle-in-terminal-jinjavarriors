import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Random;
import java.io.IOException;
import java.lang.*;

public class Hangman {
    char[] wrongChars = new char[7];
    char[] correctChars;
    String pickedWord;
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
        Hangman game = new Hangman();
        game.pickedWord = pickWord("dictionary.txt");
        game.correctChars = new char[game.pickedWord.length()];
        System.out.println(game.pickedWord);
        while(true){
            // String guessedChar = userInput.next();
            check_char_input(guessedChar, game.pickedWord, game.correctChars, game.wrongChars);
            // System.out.println(Arrays.toString(game.correctChars));
            // System.out.println(Arrays.toString(game.wrongChars));
        }
    }

    private static void displayLeaderboards() {
    }

    private static String pickWord(String fileName) {
        try {
            Stream<String> words = Files.lines(Paths.get(fileName));
            String[] result = words.toArray(String[]::new);
            int idx = new Random().nextInt(result.length);
            String pickedWord = (result[idx]);
            return pickedWord;
        } catch (IOException e) {
            return "";
        }

    }

    private static void check_char_input(String input, String pickedWord, char[] correctChars, char[] wrongChars) {
        if (input.length() > 1) {
            if (input == pickedWord) {
                System.out.println("YES!!!");
            } else {
                System.out.println("NO!!!");
            }
        } else {
            if (pickedWord.contains(input)) {
                for (int i = 0; i < pickedWord.length(); i++) {
                    if (pickedWord.charAt(i) == input.charAt(0)) {
                        correctChars[i] = input.charAt(0);
                    }
                }
            } else {
                for (int i = 0; i < wrongChars.length; i++) {
                    if (wrongChars[i] == '\u0000') {
                        wrongChars[i] = input.charAt(0);
                        break;
                    }
                }
            }
        }
    }

}
