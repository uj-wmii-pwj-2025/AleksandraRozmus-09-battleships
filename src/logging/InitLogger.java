package logging;

import java.io.File;

public class InitLogger {
    public boolean checkParameters(String mode, String host, int port, String mapFile) {
        if (mode == null || mode.isEmpty()) {
            System.out.println("Please specify mode (-mode client/server)");
            return false;
        }

        if (!mode.equals("client") && !mode.equals("server")) {
            System.out.println("Invalid mode. Possible: client or server");
            return false;
        }

        if (port <= 0 || port > 65535) {
            System.out.println("Invalid port number");
            return false;
        }

        if (mapFile == null || mapFile.isEmpty()) {
            System.out.println("Map file not specified");
            return false;
        }

        File f = new File(mapFile);
        if (!f.exists() || !f.isFile() || !f.canRead()) {
            System.out.println("Cannot read file: " + mapFile);
            return false;
        }

        if (mode.equals("client") && (host == null || host.isEmpty())) {
            System.out.println("Client mode requires specifying a host (-host)");
            return false;
        }

        return true;
    }
}
