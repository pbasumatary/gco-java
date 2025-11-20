package com.codeoff;

import java.math.BigInteger;
import java.util.Arrays;

public class SolverC1Counter {

    private static final int SIZE = 5;
    private static final int CELL_COUNT = SIZE * SIZE;

    private final int[][] orthNeighbors;
    private final Integer[] fillOrder;

    // Counting state
    private BigInteger solutions = BigInteger.ZERO;
    private long nodesVisited = 0;

    // Safety limits to avoid infinite runtimes in the web server
    private static final long MAX_NODES = 2_000_000L; // safe cap
    private static final BigInteger MAX_SOLUTIONS = new BigInteger("1000000");

    public SolverC1Counter() {
        orthNeighbors = buildOrthNeighbors();
        fillOrder = buildFillOrder();
    }

    // --------------------------------------------------------
    // Public entry point called from WebServer
    // --------------------------------------------------------
    public String countSummary() {

        int[] grid = new int[CELL_COUNT];
        boolean[] used = new boolean[26]; // values 1..25

        backtrack(grid, used, 0);

        return """
                C1-only enumeration (partial, capped for safety)
                Nodes visited: """ + nodesVisited + """
                Solutions found: """ + solutions + """
                
                NOTE:
                Full enumeration of all valid C1-only 5×5 grids is computationally huge.
                This solver intentionally runs a partial search with safe caps.
                """;
    }

    // --------------------------------------------------------
    // Backtracking
    // --------------------------------------------------------
    private void backtrack(int[] grid, boolean[] used, int pos) {

        // Stop if we hit caps
        if (nodesVisited >= MAX_NODES) return;
        if (solutions.compareTo(MAX_SOLUTIONS) >= 0) return;

        nodesVisited++;

        // Full grid?
        if (pos == fillOrder.length) {
            solutions = solutions.add(BigInteger.ONE);
            return;
        }

        int idx = fillOrder[pos];

        for (int v = 1; v <= 25; v++) {
            if (used[v]) continue;

            if (!respectsC1(grid, idx, v)) continue;

            grid[idx] = v;
            used[v] = true;

            backtrack(grid, used, pos + 1);

            grid[idx] = 0;
            used[v] = false;

            if (nodesVisited >= MAX_NODES) return;
            if (solutions.compareTo(MAX_SOLUTIONS) >= 0) return;
        }
    }

    private boolean respectsC1(int[] grid, int idx, int v) {
        for (int nb : orthNeighbors[idx]) {
            int nv = grid[nb];
            if (nv != 0 && Math.abs(nv - v) == 1)
                return false;
        }
        return true;
    }

    // --------------------------------------------------------
    // Build neighbors and fill order
    // --------------------------------------------------------
    private int[][] buildOrthNeighbors() {
        int[][] n = new int[CELL_COUNT][];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int idx = r * SIZE + c;
                int[] t = new int[4];
                int k = 0;

                if (r > 0) t[k++] = (r - 1) * SIZE + c;
                if (r < SIZE - 1) t[k++] = (r + 1) * SIZE + c;
                if (c > 0) t[k++] = r * SIZE + (c - 1);
                if (c < SIZE - 1) t[k++] = r * SIZE + (c + 1);

                n[idx] = Arrays.copyOf(t, k);
            }
        }
        return n;
    }

    private Integer[] buildFillOrder() {
        Integer[] order = new Integer[CELL_COUNT];
        for (int i = 0; i < CELL_COUNT; i++) order[i] = i;

        // Sort by descending number of constraints (heuristic)
        Arrays.sort(order, (a, b) ->
                Integer.compare(orthNeighbors[b].length, orthNeighbors[a].length));

        return order;
    }
}
