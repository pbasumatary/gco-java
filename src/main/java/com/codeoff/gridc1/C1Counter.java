package com.codeoff.gridc1;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Counts all 5x5 grids labeled 1..25 exactly once,
 * such that no orthogonally adjacent cells contain consecutive numbers.
 *
 * Constraint:
 *   For any two cells that share an edge:
 *       |v1 - v2| != 1
 *
 * Representation:
 *   5x5 grid flattened to 1D array of length 25:
 *     index = row * 5 + col
 *
 * Counting:
 *   Backtracking with:
 *     - adjacency precomputed
 *     - cell fill order sorted by orthogonal degree
 *     - BigInteger counter.
 *
 * WARNING: Full enumeration is computationally enormous.
 */
public class C1Counter {

    private static final int SIZE = 5;
    private static final int CELL_COUNT = SIZE * SIZE;

    private final int[][] orthNeighbors;
    private final Integer[] fillOrder;

    // State for counting
    private BigInteger solutions = BigInteger.ZERO;
    private long nodesVisited = 0L;

    // Optional limits so you can stop early
    private final Long maxNodes;
    private final BigInteger maxSolutions;

    public C1Counter(Long maxNodes, BigInteger maxSolutions) {
        this.orthNeighbors = buildOrthNeighbors();
        this.fillOrder = buildFillOrder();
        this.maxNodes = maxNodes;
        this.maxSolutions = maxSolutions;
    }

    /**
     * Start counting all valid grids.
     */
    public BigInteger countAll() {
        int[] grid = new int[CELL_COUNT];
        boolean[] used = new boolean[26]; // values 1..25

        backtrack(grid, used, 0);
        return solutions;
    }

    public long getNodesVisited() {
        return nodesVisited;
    }

    // ------------- Core backtracking -------------

    private void backtrack(int[] grid, boolean[] used, int pos) {
        // Optional global limits
        if (maxNodes != null && nodesVisited >= maxNodes) {
            return;
        }
        if (maxSolutions != null && solutions.compareTo(maxSolutions) >= 0) {
            return;
        }

        nodesVisited++;

        if (pos == fillOrder.length) {
            // Complete valid grid under C1
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

            if (maxNodes != null && nodesVisited >= maxNodes) {
                return;
            }
            if (maxSolutions != null && solutions.compareTo(maxSolutions) >= 0) {
                return;
            }
        }
    }

    private boolean respectsC1(int[] grid, int idx, int value) {
        for (int nb : orthNeighbors[idx]) {
            int nv = grid[nb];
            if (nv != 0 && Math.abs(nv - value) == 1) {
                return false;
            }
        }
        return true;
    }

    // ------------- Helpers: adjacency & fill order -------------

    private int[][] buildOrthNeighbors() {
        int[][] result = new int[CELL_COUNT][];

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int idx = r * SIZE + c;
                result[idx] = computeOrthNeighbors(r, c);
            }
        }
        return result;
    }

    private int[] computeOrthNeighbors(int r, int c) {
        int[][] deltas = { {-1,0}, {1,0}, {0,-1}, {0,1} };
        int[] tmp = new int[4];
        int count = 0;

        for (int[] d : deltas) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE) {
                tmp[count++] = nr * SIZE + nc;
            }
        }

        return Arrays.copyOf(tmp, count);
    }

    /**
     * Simple heuristic: fill more-constrained cells first (higher orth degree).
     */
    private Integer[] buildFillOrder() {
        Integer[] order = new Integer[CELL_COUNT];
        for (int i = 0; i < CELL_COUNT; i++) {
            order[i] = i;
        }

        Arrays.sort(order, 0, CELL_COUNT, (a, b) -> {
            int da = orthNeighbors[a].length;
            int db = orthNeighbors[b].length;
            return Integer.compare(db, da); // descending degree
        });

        return order;
    }
}
