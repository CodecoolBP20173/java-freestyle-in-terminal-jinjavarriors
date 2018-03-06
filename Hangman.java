import java.util.Scanner;

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
  }
  private static void displayLeaderboards(){
  }
}
