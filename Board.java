import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int[][] bdtiles;
    private final int n;
    // private final List<Board> neighbors = new ArrayList<>();
    private int zeroRow;
    private int zeroCol;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        bdtiles = new int [tiles.length][tiles[0].length];
        n = tiles.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] >= 0 && tiles[i][j] < n*n) bdtiles[i][j] = tiles[i][j];

            }
        }
        findZeroTile();
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder(4 * n * n);     // optimization?
        //      s.append(N + "\n");
        s.append(n);
        s.append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", bdtiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() { return n; }

    // number of tiles out of place
    public int hamming() {
        int hm = 0;
        for (int i = 0; i < n*n; i++) {
            if (bdtiles[i / n][i % n] != 0) {
                if (bdtiles[i / n][i % n] != i + 1) {
                    hm++;
                }
            }
        }
        return hm;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int mh = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (bdtiles[i][j] != 0 && bdtiles[i][j] != (i*n + j+1)) {
                    mh = mh + Math.abs(((bdtiles[i][j]-1) / n) - i) + Math.abs((bdtiles[i][j]-1) % n - j);
                }
            }
        }
        return mh;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n * n - 1; i++) {
            if (bdtiles[i / n][i % n] != 0) {
                if (bdtiles[i / n][i % n] != i + 1) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    // does this board equal y?
    public boolean equals(Object y) {
        if (!(y instanceof Board)) return false;
        Board that = (Board) y;
        if (this.n != that.n) { return false; }

            for (int i = 0; i < n * n - 1; i++) {
                if (this.bdtiles[i / n][i % n] != that.bdtiles[i / n][i % n]) {
                    return false;
                }
            }
            return true;
        }


    // all neighboring boards
    public Iterable<Board> neighbors() {
            findZeroTile();
        List<Board> neighbors = new ArrayList<>();
            if (zeroRow > 0) {
                int [][] copy = new int [n][n];
                for (int i = 0; i < bdtiles.length; i++) {
                    // copy second dimensional array
                    copy[i] = Arrays.copyOf(bdtiles[i], bdtiles[i].length);
                }
                int i = copy[zeroRow][zeroCol];
                copy[zeroRow][zeroCol] = copy[zeroRow-1][zeroCol];
                copy[zeroRow-1][zeroCol] = i;
                neighbors.add(new Board(copy));
            }
            if (zeroCol > 0) {
                int [][] copy1 = new int [n][n];
                for (int i = 0; i < bdtiles.length; i++) {
                    // copy second dimensional array
                    copy1[i] = Arrays.copyOf(bdtiles[i], bdtiles[i].length);
                }
                int i = copy1[zeroRow][zeroCol];
                copy1[zeroRow][zeroCol] = copy1[zeroRow][zeroCol-1];
                copy1[zeroRow][zeroCol-1] = i;
                neighbors.add(new Board(copy1));
            }
            if (zeroRow < n-1) {
                int [][] copy2 = new int [n][n];
                for (int i = 0; i < bdtiles.length; i++) {
                    // copy second dimensional array
                    copy2[i] = Arrays.copyOf(bdtiles[i], bdtiles[i].length);
                }
                int i = copy2[zeroRow][zeroCol];
                copy2[zeroRow][zeroCol] = copy2[zeroRow+1][zeroCol];
                copy2[zeroRow+1][zeroCol] = i;
                neighbors.add(new Board(copy2));
            }
            if (zeroCol+1 < n) {
                int [][] copy3 = new int [n][n];
                for (int i = 0; i < bdtiles.length; i++) {
                    // copy second dimensional array
                    copy3[i] = Arrays.copyOf(bdtiles[i], bdtiles[i].length);
                }
            int i = copy3[zeroRow][zeroCol];
            copy3[zeroRow][zeroCol] = copy3[zeroRow][zeroCol+1];
            copy3[zeroRow][zeroCol+1] = i;
            neighbors.add(new Board(copy3));
            }
        return neighbors;

    }

    private void findZeroTile() {
        outerloop:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (bdtiles[i][j] == 0) {
                    zeroRow = i;       // index starting from 0
                    zeroCol = j;
                    break outerloop;
                }
            }
        }
    }
    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
       /* int [][] bdtw = new int [n][n];
        for (int i = 0; i < bdtiles.length; i++) {
            // copy second dimensional array
            bdtw[i] = Arrays.copyOf(bdtiles[i], bdtiles[i].length);
        }
        int i = bdtw[0][0];
        if (bdtw[0][0] == 0 || bdtw[0][0] == bdtw[0][1]) {
            i = bdtw[1][1];
            bdtw[1][1] = bdtw[1][0];
            bdtw[1][0] = i;
        }
        if (bdtw[1][0] == 0) {
            bdtw[0][0] = bdtw[1][0];
            bdtw[1][0] = i;
        } else {
            bdtw[0][0] = bdtw[0][1];
            bdtw[0][1] = i;
        }
        Board bd1 = new Board(bdtw);
        return bd1;  */
        int dim = n;
        int[][] copy = this.bdtiles;
        if (dim <= 1)
            return new Board(copy);
        // Find zero so that we don't exchange with the blank
        // This looks like a O(dim^2) algorithm, but on average it should finish
        // in O(1).
        int row ;
        int col = 0;
        int value = 0;
        int lastValue = bdtiles[0][0];
        zerosearch: for (row = 0; row < dim; row++) {
            for (col = 0; col < dim; col++) {
                value = bdtiles[row][col];
                // Check col>0 because swap must occur on same row
                if (value != 0 && lastValue != 0 && col > 0)
                    break zerosearch;
                lastValue = value;
            }
        }
        copy[row][col] = lastValue;
        copy[row][col - 1] = value;
        return new Board(copy);

    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int [][] i = new int [4][4];
        for (int l = 0; l < 4; l++) {
            for (int k = 0; k < 4; k++) {
                i[l][k] = (l*4 + k+1);
            }
            i[3][3] = 0;
        }
        Board bd = new Board(i);
        StdOut.println("board equal" + bd.toString());
        StdOut.println("hamming equal" + bd.hamming());
        StdOut.println("manhattan equal" + bd.manhattan());
        StdOut.println("is goal  equal" + bd.isGoal());
        StdOut.println("neighburs" + bd.neighbors());
        StdOut.println("board twin equal" + bd.twin().toString());
    }

}