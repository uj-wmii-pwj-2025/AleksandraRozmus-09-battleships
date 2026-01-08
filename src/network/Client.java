package network;

import game.BattleshipGame;

import java.net.Socket;

public class Client {
    private final String host;
    private final int port;
    private final BattleshipGame game;

    public Client(String host, int port, BattleshipGame game) {
        this.host = host;
        this.port = port;
        this.game = game;
    }

    public void run() throws Exception {
        System.out.println("Connecting to server...");
        Network network = new Network(new Socket(host, port), game.logger);
        System.out.println("Connected");

        game.play(network, false);
    }
}