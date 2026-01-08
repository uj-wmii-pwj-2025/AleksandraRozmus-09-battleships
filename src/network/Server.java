package network;

import game.BattleshipGame;

import java.net.ServerSocket;

public class Server {
    private final int port;
    private final BattleshipGame game;

    public Server(int port, BattleshipGame game) {
        this.port = port;
        this.game = game;
    }

    public void run() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Waiting for client...");
            Network network = new Network(serverSocket.accept(), game.logger);
            System.out.println("Client connected");

            game.play(network, true);
        }
    }
}