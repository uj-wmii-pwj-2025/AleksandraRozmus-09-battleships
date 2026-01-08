package game;

import network.Client;
import network.Server;
import logging.GameLogger;
import network.Network;
import java.util.Scanner;

public class BattleshipGame {
    private final String mode;
    private final String host;
    private final int port;

    public final Board board;
    public final GameLogger logger;

    private final Scanner scanner;

    public int roundCounter = 1;
    public String lastShotCoord;

    public BattleshipGame(String mode, String host, int port, String mapFile) throws Exception {
        this.mode = mode;
        this.host = host;
        this.port = port;
        this.board = new Board(mapFile);
        this.logger = new GameLogger();
        this.scanner = new Scanner(System.in);
    }

    public void start() throws Exception {
        if (mode.equals("server"))
            new Server(port, this).run();
        else
            new Client(host, port, this).run();
    }

    public void play(Network network, boolean isServer) throws Exception {
        String lastSent = null;

        if (!isServer) {
            printRoundState();
            String myMove = askForMove();
            lastSent = "start;" + myMove;
            network.send(lastSent);
        }

        while (true) {
            if (isServer && roundCounter == 1) {
                logger.printSeparator(roundCounter);
                logger.printBoards(board.getMyBoard(), board.getEnemyBoard());
                logger.logWaitingForStart();
            } else {
                logger.logWaitingForEnemy();
            }

            String incoming = network.receiveWithRetry(lastSent);
            String[] info = incoming.split(";");

            if (!isServer || roundCounter > 1) {
                if (handleMyShotResult(info[0]))
                    break;
            }

            ShotResult enemyResult;
            if (info.length > 1)
                enemyResult = processEnemyShot(info[1]);
            else
                enemyResult = ShotResult.O_ZATOPIONY;

            if (enemyResult == ShotResult.O_ZATOPIONY) {
                network.send(enemyResult.toString());
                handleEndGame(false);
                break;
            }

            if (!isServer) {
                roundCounter++;
                printRoundState();
            } else {
                if (roundCounter > 1) {
                    logger.printSeparator(roundCounter);
                    logger.printBoards(board.getMyBoard(), board.getEnemyBoard());
                }
            }

            String myMove = askForMove();
            lastSent = enemyResult.toString() + ";" + myMove;
            network.send(lastSent);

            if (isServer)
                roundCounter++;
        }
    }

    public String askForMove() {
        while (true) {
            logger.printPrompt("Enter shot coordinates:");
            String input = scanner.nextLine().trim().toUpperCase();

            int[] coords = board.processCoord(input);
            if (coords[0] >= 0 && coords[0] < board.getSize() && coords[1] >= 0 && coords[1] < board.getSize()) {
                lastShotCoord = input;
                return input;
            }

            System.out.println("Invalid coordinates");
        }
    }

    public ShotResult processEnemyShot(String coords) {
        int[] xy = board.processCoord(coords);
        ShotResult result = board.processShot(xy[0], xy[1]);
        logger.logEnemyShot(coords, result);
        return result;
    }

    public boolean handleMyShotResult(String result) {
        ShotResult res = ShotResult.fromString(result);

        logger.logMyShotResult(lastShotCoord, res);
        board.markEnemyBoard(lastShotCoord, res);

        if (res == ShotResult.O_ZATOPIONY) {
            handleEndGame(true);
            return true;
        }
        return false;
    }

    public void printRoundState() {
        logger.printSeparator(roundCounter);
        logger.printBoards(board.getMyBoard(), board.getEnemyBoard());
    }

    public void handleEndGame(boolean won) {
        board.revealBoardEndGame(won);
        logger.printEndGame(won, board.getMyBoard(), board.getEnemyBoard());
    }
}