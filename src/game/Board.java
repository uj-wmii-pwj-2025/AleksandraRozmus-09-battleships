package game;

import java.nio.file.*;
import java.util.*;

public class Board {
    private final int SIZE = 10;
    private final char[][] myBoard = new char[SIZE][SIZE];
    private final char[][] enemyBoard = new char[SIZE][SIZE];

    public Board(String mapFile) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(mapFile));

        for (int i = 0; i < SIZE; i++) {
            String line = lines.get(i).trim();
            myBoard[i] = line.toCharArray();
        }

        for (int i = 0; i < SIZE; i++)
            Arrays.fill(enemyBoard[i], '?');
    }

    public char[][] getMyBoard() {
        return myBoard;
    }

    public char[][] getEnemyBoard() {
        return enemyBoard;
    }

    public int getSize() {
        return SIZE;
    }

    public int[] processCoord(String coord) {
        coord = coord.toUpperCase();
        int row = coord.charAt(0) - 'A';

        try {
            int col = Integer.parseInt(coord.substring(1)) - 1;
            return new int[]{row, col};
        } catch (NumberFormatException e) {
            return new int[]{-1, -1};
        }
    }

    public ShotResult processShot(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE)
            return ShotResult.PUDLO;

        if (myBoard[x][y] == '.' || myBoard[x][y] == '~') {
            myBoard[x][y] = '~';
            return ShotResult.PUDLO;
        } else if (myBoard[x][y] == '#' || myBoard[x][y] == '@') {
            myBoard[x][y] = '@';

            if (allSunk())
                return ShotResult.O_ZATOPIONY;
            
            if (isShipSunk(x, y))
                return ShotResult.ZATOPIONY;

            return ShotResult.TRAFIONY;
        }
        return ShotResult.PUDLO;
    }

    public void markEnemyBoard(String coord, ShotResult result) {
        int[] xy = processCoord(coord);
        int row = xy[0], col = xy[1];

        if (result == ShotResult.PUDLO)
            enemyBoard[row][col] = '.';
        else {
            enemyBoard[row][col] = '#';

            if (result == ShotResult.ZATOPIONY || result == ShotResult.O_ZATOPIONY)
                markNeighbors(row, col);
        }
    }

    private void markNeighbors(int row, int col) {
        boolean[][] visited = new boolean[SIZE][SIZE];
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{row, col});
        visited[row][col] = true;

        int[] dirX = {1, -1, 0, 0};
        int[] dirY = {0, 0, 1, -1};

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            revealEmptyFields(curr[0], curr[1]);

            for (int i = 0; i < 4; i++) {
                int nx = curr[0] + dirX[i];
                int ny = curr[1] + dirY[i];

                if (nx >= 0 && ny >= 0 && nx < SIZE && ny < SIZE && !visited[nx][ny] && enemyBoard[nx][ny] == '#') {
                    visited[nx][ny] = true;
                    queue.add(new int[]{nx, ny});
                }
            }
        }
    }

    private void revealEmptyFields(int r, int c) {
        for (int dirX = -1; dirX <= 1; dirX++) {
            for (int dirY = -1; dirY <= 1; dirY++) {
                int nx = r + dirX;
                int ny = c + dirY;

                if (nx >= 0 && ny >= 0 && nx < SIZE && ny < SIZE && enemyBoard[nx][ny] == '?')
                    enemyBoard[nx][ny] = '.';
            }
        }
    }

    private boolean isShipSunk(int x, int y) {
        boolean[][] visited = new boolean[SIZE][SIZE];
        return !hasUnhitPart(x, y, visited);
    }

    private boolean hasUnhitPart(int x, int y, boolean[][] v) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE || v[x][y])
            return false;

        if (myBoard[x][y] != '#' && myBoard[x][y] != '@')
            return false;

        v[x][y] = true;

        if (myBoard[x][y] == '#')
            return true;

        return hasUnhitPart(x + 1, y, v) || hasUnhitPart(x - 1, y, v) || hasUnhitPart(x, y + 1, v) || hasUnhitPart(x, y - 1, v);
    }

    private boolean allSunk() {
        for (char[] row : myBoard) {
            for (char c : row) {
                if (c == '#')
                    return false;
            }
        }
        return true;
    }

    public void revealBoardEndGame(boolean won) {
        if (won) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (enemyBoard[i][j] == '?')
                        enemyBoard[i][j] = '.';
                }
            }
        }
    }
}