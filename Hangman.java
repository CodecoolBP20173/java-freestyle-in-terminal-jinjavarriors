import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Random;
import java.util.Collection;
import java.util.Collections;
import java.util.InputMismatchException;
import java.lang.*;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileReader;
import com.codecool.termlib.Attribute;
import com.codecool.termlib.Terminal;
import com.codecool.termlib.Color;

public class Hangman {
    char[] wrongChars = new char[7];
    char[] correctChars;
    String pickedWord;
    int tries;
    static Terminal terminalCustomize = new Terminal();
    static Scanner userInput = new Scanner(System.in);

    public static void main(String[] args) {
        int chosenMenu = displayMenu();
        Hangman game = new Hangman();
        switch (chosenMenu) {
        case 1:
            game(game);
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
        while (true) {
            try {
                int chosenMenuPoint = userInput.nextInt();
                if (!(chosenMenuPoint == 1 || chosenMenuPoint == 2)) {
                    System.out.println("Please choose a valid option!");
                    continue;
                } else {
                    return chosenMenuPoint;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please choose a valid option!");
                userInput.next();
            }
        }
    }

    private static void game(Hangman game) {
        game.pickedWord = pickWord("dictionary.txt").toUpperCase();
        game.correctChars = new char[game.pickedWord.length()];
        for (int i = 0; i < game.correctChars.length; i++) {
            game.correctChars[i] = '_';
        }
        game.tries = 7;
        while (true) {
            renderField(game);
            System.out.println("Your letter: ");
            String guessedChar = userInput.next().toUpperCase();
            if (Character.isLetter(guessedChar.charAt(0))) {
                if (!(new String(game.wrongChars).contains(guessedChar))
                        && !(new String(game.correctChars).contains(guessedChar))) {
                    checkCharInput(guessedChar, game);
                } else {
                    System.out.println("Character already used, please input another!");
                }
            } else {
                System.out.println("Please input a letter from A to Z!");
            }
        }
    }

    private static String pickWord(String fileName) {
        try {
            Stream<String> words = Files.lines(Paths.get(fileName));
            String[] result = words.toArray(String[]::new);
            int idx = new Random().nextInt(result.length);
            String pickedWord = (result[idx]);
            words.close();
            return pickedWord;
        } catch (IOException e) {
            return "";
        }
    }

    private static void checkCharInput(String input, Hangman game) {
        input.toUpperCase();
        if (input.length() > 1) {
            guessWord(input, game);
        } else {
            if (game.pickedWord.contains(input)) {
                for (int i = 0; i < game.pickedWord.length(); i++) {
                    if (game.pickedWord.charAt(i) == input.charAt(0)) {
                        game.correctChars[i] = input.charAt(0);
                        checkWin(game);
                    }
                }
            } else {
                for (int i = 0; i < game.wrongChars.length; i++) {
                    if (game.wrongChars[i] == '\u0000') {
                        game.wrongChars[i] = input.charAt(0);
                        game.tries--;
                        if (game.tries == 0) {
                            renderField(game);
                            System.out.println("GAME OVER");
                            main(new String[] {});
                        }
                        break;
                    }
                }
            }
        }
    }

    private static void guessWord(String input, Hangman game) {
        if (input.equals(game.pickedWord)) {
            renderField(game);
            main(new String[] {});
        } else {
            game.tries = 0;
            renderField(game);
            main(new String[] {});
        }
    }

    private static void checkWin(Hangman game) {
        String guessedWord = Stream.of(game.correctChars).map(e -> new String(e)).collect(Collectors.joining());
        if (guessedWord.equals(game.pickedWord)) {
            terminalCustomize.clearScreen();
            renderWin();
            main(new String[] {});
        }
    }

    private static void renderField(Hangman game) {
        terminalCustomize.clearScreen();
        String levels = new String("");
        int tries = game.tries;
        char[] wrongChars = game.wrongChars;
        String letters = new String("");

        for (int i = 0; i < game.correctChars.length; i++) {
            if (i == game.correctChars.length - 1) {
                letters += game.correctChars[i];
            } else {
                letters += game.correctChars[i] + " ";
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader("hangman_draws.txt"))) {
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                levels += sCurrentLine + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] lives = levels.split("#");
        String triedChars = new String();

        for (int i = 0; i < wrongChars.length; i++) {
            if (wrongChars[i] != '\u0000') {
                if (i == wrongChars.length - 1) {
                    triedChars += wrongChars[i];
                } else {
                    triedChars += wrongChars[i] + ", ";
                }
            }
        }

        lives[tries] = lives[tries].replace("<guessed>", triedChars);
        lives[tries] = lives[tries].replace("<lives>", Integer.toString(tries));
        lives[tries] = lives[tries].replace("$".charAt(0), (char)27);
        lives[tries] = lives[tries].replace("<letters>", letters);
        System.out.println(lives[tries]);
    }
    private static void renderWin(){
        String lines = new String("");
        try (BufferedReader br = new BufferedReader(new FileReader("winman.txt"))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                lines += sCurrentLine + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] frames = lines.split("#");
        for(int i=0;i<4;i++){
            for(String frame:frames){
                System.out.println(frame);
                try{
                    Thread.sleep(500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                terminalCustomize.clearScreen();
            }
        }
    }
}
