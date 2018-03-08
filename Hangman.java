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
        terminalCustomize.clearScreen();
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
            System.out.println("Your letter:\n(You can guess the whole word if you start with an ! sign.)");
            String guessedChar = userInput.next().toUpperCase();
            if (guessedChar.contains("!")) {
                checkCharInput(guessedChar, game);
            } else {
                if (Character.isLetter(guessedChar.charAt(guessedChar.length() - 1))) {
                    if (!(new String(game.wrongChars).contains(guessedChar.substring(guessedChar.length() - 1)))
                            && !(new String(game.correctChars)
                                    .contains(guessedChar.substring(guessedChar.length() - 1)))) {
                        checkCharInput(guessedChar, game);
                    } else {
                        try {
                            System.out.println("Character already used, please input another!");
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } else {
                    try {
                        System.out.println("Please input a letter from A to Z!");
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
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
        if (input.contains("!")) {
            guessWord(input, game);
        } else {
            if (game.pickedWord.contains(input.substring(input.length() - 1))) {
                for (int i = 0; i < game.pickedWord.length(); i++) {
                    if (game.pickedWord.charAt(i) == input.charAt(input.length() - 1)) {
                        game.correctChars[i] = input.charAt(input.length() - 1);
                        checkWin(game);
                    }
                }
            } else {
                for (int i = 0; i < game.wrongChars.length; i++) {
                    if (game.wrongChars[i] == '\u0000') {
                        game.wrongChars[i] = input.charAt(input.length() - 1);
                        game.tries--;
                        if (game.tries == 0) {
                            renderField(game);
                            try{
                                Thread.sleep(2000);
                            }catch(InterruptedException e){
                                e.printStackTrace();
                            }
                            main(new String[] {});
                        }
                        break;
                    }
                }
            }
        }
    }

    private static void guessWord(String input, Hangman game) {
        if (input.substring(input.indexOf('!')).equals(game.pickedWord)) {
            terminalCustomize.clearScreen();
            renderWin();
            main(new String[] {});
        } else {
            game.tries = 0;
            renderField(game);
            try{
                Thread.sleep(2000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
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
        lives[tries] = lives[tries].replace("$".charAt(0), (char) 27);
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
                    Thread.sleep(350);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                terminalCustomize.clearScreen();
            }
        }
    }
}
