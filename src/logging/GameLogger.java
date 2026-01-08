package logging;

import game.ShotResult;

public class GameLogger {
    public void logTcpSend(String message) {
        System.out.println("[TCP] " + message.trim());
    }

    public void logTcpReceive(String message) {
        if (message != null)
            System.out.println("[TCP] " + message.trim());
    }

    public void printSeparator(int round) {
        System.out.println("\n==================================");
        System.out.println(" ROUND: " + round);
        System.out.println("==================================");
    }

    public void printBoards(char[][] myBoard, char[][] enemyBoard) {
        System.out.println();
        System.out.println("      MY BOARD                     ENEMY BOARD");
        System.out.println("   1 2 3 4 5 6 7 8 9 10          1 2 3 4 5 6 7 8 9 10");

        for (int i = 0; i < 10; i++) {
            char rowChar = (char) ('A' + i);
            StringBuilder myRow = new StringBuilder();
            StringBuilder enemyRow = new StringBuilder();

            for (int j = 0; j < 10; j++) {
                myRow.append(myBoard[i][j]).append(" ");
                enemyRow.append(enemyBoard[i][j]).append(" ");
            }

            System.out.printf("%c  %s       %c  %s%n", rowChar, myRow.toString(), rowChar, enemyRow.toString());
        }
        System.out.println("__________________________________");
    }

    public void printPrompt(String message) {
        System.out.println(message);
        System.out.print("> ");
    }

    public void logEnemyShot(String coord, ShotResult result) {
        System.out.println(">>> Enemy shot at " + coord + " : " + result.toString());
    }

    public void logMyShotResult(String coord, ShotResult result) {
        System.out.println(">>> Your shot at " + coord + ": " + result.toString());
    }

    public void logWaitingForEnemy() {
        System.out.println("Waiting for opponent move...");
    }

    public void logWaitingForStart() {
        System.out.println("Waiting for game start...");
    }

    public void printEndGame(boolean won, char[][] myBoard, char[][] enemyBoard) {

        System.out.println("\n==================================");

        if (won)
            System.out.println("Wygrana!");
        else
            System.out.println("Przegrana");

        System.out.println("\nEnemy board:");
        printSingleBoard(enemyBoard);

        System.out.println();
        System.out.println("\nYour board:");
        printSingleBoard(myBoard);
    }

    private void printSingleBoard(char[][] board) {
        System.out.println("1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            System.out.print((char)('A' + i) + "  ");

            for (int j = 0; j < 10; j++)
                System.out.print(board[i][j] + " ");

            System.out.println();
        }
    }
}