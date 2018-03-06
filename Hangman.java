import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Random;
import java.io.IOException;

public class Hangman {
  static Scanner userInput = new Scanner(System.in);

  public static void main(String[] args) {
    int chosenMenu = displayMenu();
    switch(chosenMenu){
      case 1:
        game();
        break;
      case 2:
        return;
    }
  }
  private static int displayMenu(){
    System.out.println("Welcome in Hangman game!");
    System.out.println("Choose a menupoint:");
    System.out.println("1. Game");
    System.out.println("2. Quit");
    int chosenMenuPoint = userInput.nextInt();
    return chosenMenuPoint;
  }
  private static void game(){
    String pickedWord = pickWord("dictionary.txt");
  }
  private static void displayLeaderboards(){
  }
  private static String pickWord(String fileName) {
    try {
      Stream<String> words = Files.lines(Paths.get(fileName));
      String[] result = words.toArray(String[]::new);
      int idx = new Random().nextInt(result.length);
      String pickedWord = (result[idx]);
      return pickedWord;
    } catch(IOException e) {
      return "";
    }

  }
}
