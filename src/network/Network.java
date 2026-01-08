package network;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import logging.GameLogger;

public class Network {
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final GameLogger logger;

    public final int TIMEOUT = 1000;

    public Network(Socket socket, GameLogger logger) throws Exception {
        this.socket = socket;
        this.logger = logger;
        socket.setSoTimeout(TIMEOUT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }

    public void send(String msg) throws Exception {
        out.write(msg.endsWith("\n") ? msg : msg + "\n");
        out.flush();

        logger.logTcpSend(msg);
    }

    public String receive() throws Exception {
        try {
            String msg = in.readLine();
            if (msg != null)
                logger.logTcpReceive(msg);
            return msg;
        } catch (SocketTimeoutException e) {
            return null;
        }
    }

    public String receiveWithRetry(String lastSent) throws Exception {
        int retries = 0;

        while (retries < 3) {
            String msg = receive();
            if (msg != null && !msg.isEmpty())
                return msg;

            retries++;
            if (retries < 3 && lastSent != null)
                send(lastSent);
        }

        throw new Exception("Błąd komunikacji");
    }

    public void close() throws Exception {
        socket.close();
    }
}