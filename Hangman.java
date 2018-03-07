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
    int tries;
    static Scanner userInput = new Scanner(System.in);

    public static void main(String[] args) {
        int chosenMenu = displayMenu();
        switch (chosenMenu) {
        case 1:
            game();
            break;
        case 2:
            System.exit(0);
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
        game.tries = 7;
        //System.out.println(game.pickedWord);
        while (true) {
            String guessedChar = userInput.next();
            if (!(new String(game.wrongChars).contains(guessedChar)) && !(new String(game.correctChars).contains(guessedChar))) {
                checkCharInput(guessedChar, game);
                System.out.println(Arrays.toString(game.correctChars));
                System.out.println(Arrays.toString(game.wrongChars));
            } else {
                System.out.println("Character already used, please input another!");
            }
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

    private static void checkCharInput(String input, Hangman game) {
        if (input.length() > 1) {
            guessWord(input, game.pickedWord);
        } else {
            if (game.pickedWord.contains(input)) {
                for (int i = 0; i < game.pickedWord.length(); i++) {
                    if (game.pickedWord.charAt(i) == input.charAt(0)) {
                        game.correctChars[i] = input.charAt(0);
                        checkWin(game.correctChars, game.pickedWord);
                    }
                }
            } else {
                for (int i = 0; i < game.wrongChars.length; i++) {
                    if (game.wrongChars[i] == '\u0000') {
                        game.wrongChars[i] = input.charAt(0);
                        game.tries--;
                        System.out.println("Tries left: " + game.tries);
                        if (game.tries == 0) {
                            System.out.println("GAME OVER");
                            main(new String[]{});
                        }
                        break;
                    }
                }
            }
        }
    }

    private static void guessWord(String input, String pickedWord) {
        if (input.equals(pickedWord)) {
            System.out.println("YES word!!!");
            main(new String[]{});
        } else {
            System.out.println("NO word!!!");
            main(new String[]{});
        }
    }

    private static void checkWin(char[] correctChars, String pickedWord) {
        String guessedWord = Stream.of(correctChars).map(e -> new String(e)).collect(Collectors.joining());

        if (guessedWord.equals(pickedWord)) {
            System.out.println("YES!!!");
            main(new String[]{});
        }
    }
}
