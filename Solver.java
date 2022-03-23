import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;


public class Solver {

   // private int moves = 0;
    private final SearchNode finalNode;
    private Stack<Board> boards;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) { throw new IllegalArgumentException(); }
        MinPQ<SearchNode> minpq = new MinPQ<SearchNode>();
        List<Board> previouses = new ArrayList<>();
        Board previous = null;
        Board dequeuedboard = initial ;
        SearchNode dequeuednode = new SearchNode(initial,0,null);
        Iterable <Board> boards ;

        while((!dequeuedboard.isGoal()) && previouses.size() <1000 ) {
            boards = dequeuedboard.neighbors();

            for (Board board1 : boards) {
                if (!previouses.contains(board1)) {
                     if (!board1.equals(previous)) {
                        minpq.insert(
                                new SearchNode(board1, (dequeuednode.moves + 1), dequeuednode));
                    }
                }
            }
                previouses.add(previous);
                previous = dequeuedboard;
                if(!minpq.isEmpty()) {
                    dequeuednode = minpq.delMin();
                }
                dequeuedboard = dequeuednode.current;
            }
            finalNode = dequeuednode;
        }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return (finalNode.current.isGoal()) ;

    /*    int inversions = 0;

        for (int i = 0; i < initialbd.n * initialbd.n; i++) {
            int currentRow = i / initialbd.n;
            int currentCol = i % initialbd.n;

            for (int j = i; j < initialbd.n * initialbd.n; j++) {
                int row = j / initialbd.n;
                int col = j % initialbd.n;
                if (initialbd.bdtiles[row][col] != 0 && initialbd.bdtiles[row][col] < initialbd.bdtiles[currentRow][currentCol]) {
                    inversions++;
                }
            }
        }
        if (initialbd.n % 2 != 0 && inversions % 2 != 0) return false;
        if (initialbd.n % 2 == 0 && (inversions + initialbd.zeroRow) % 2 == 0) return false;
        return true;
    */
    }
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves(){
       if (boards != null) return boards.size()-1;
         Iterable<Board> y=solution();
       if (y == null) { return -1; }
        return boards.size() - 1;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (boards != null) {
            return boards ;
        }
        boards = new Stack<Board>();
        SearchNode pointer = finalNode ;
       if (!finalNode.current.isGoal()) { return null; }
        while (pointer != null) {
            boards.push(pointer.current);
            pointer = pointer.previous;
        }
        return boards;
    }
    private class SearchNode implements Comparable<SearchNode> {
        private final int priority;
        private final SearchNode previous;
        private final Board current;
        public final int moves;

        public SearchNode(Board current, int moves, SearchNode previous) {
            this.moves = moves;
            this.current = current;
            this.previous = previous;
            this.priority = moves + current.manhattan() ;
        }

        @Override
        public int compareTo(SearchNode that) {
            int cmp = this.priority - that.priority;
            return Integer.compare(cmp, 0);
        }

    }

        // test client (see below)
    public static void main(String[] args){
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);
        //StdOut.println(solver.solution());
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}