package game;

import logging.InitLogger;

public class GameStart {
    public static void main(String[] args) throws Exception {
        InitLogger logger = new InitLogger();
        String mode = null;
        String host = null;
        String mapFile = null;
        int port = -1;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-mode" -> mode = args[++i];
                case "-port" -> port = Integer.parseInt(args[++i]);
                case "-map" -> mapFile = args[++i];
                case "-host" -> host = args[++i];
            }
        }

        if (!logger.checkParameters(mode, host, port, mapFile))
            return;

        BattleshipGame game = new BattleshipGame(mode, host, port, mapFile);
        game.start();
    }
}