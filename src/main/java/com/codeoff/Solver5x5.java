package com.codeoff;

import java.util.Arrays;

/**
 * Backtracking solver for the 5x5 grid puzzle with the following constraints:
 *
 * - Fixed Seed:
 *   Center cell (row 3, column 3) must be 13.
 *
 * - C1 (Orthogonal):
 *   No two orthogonally adjacent cells (up, down, left, right)
 *   contain consecutive integers (difference != 1).
 *
 * - C2 (Diagonal):
 *   No two diagonally adjacent cells (sharing a corner)
 *   contain values that differ by exactly 2.
 *
 * - C3 (Prime/Even Sum):
 *   The sum of the values in the 10 "prime-numbered" cells
 *   (given by fixed coordinates) must be even.
 *
 * The grid is represented as a 1D array of length 25, in row-major order:
 * index = row * 5 + col, where row, col are 0-based.
 */
public class Solver5x5 {

    private static final int SIZE = 5;
    private static final int CELL_COUNT = SIZE * SIZE;

    // Fixed center index and value (row 3, col 3 -> 0-based index 12)
    private static final int CENTER_INDEX = 2 * SIZE + 2;
    private static final int CENTER_VALUE = 13;

    // Prime-numbered cell coordinates (1-based as per problem statement)
    // Hint from assignment:
    // Grid(1,2), Grid(1,4), Grid(2,1), Grid(2,3), Grid(3,2),
    // Grid(3,4), Grid(4,1), Grid(4,3), Grid(5,2), Grid(5,4)
    private static final int[] PRIME_CELL_INDICES = toZeroBasedIndices(new int[][]{
            {1, 2}, {1, 4},
            {2, 1}, {2, 3},
            {3, 2}, {3, 4},
            {4, 1}, {4, 3},
            {5, 2}, {5, 4}
    });

    // Adjacency lists (by index 0..24)
    private final int[][] orthogonalNeighbors;
    private final int[][] diagonalNeighbors;

    // Order in which cells are filled during backtracking
    private final Integer[] fillOrder;

    public Solver5x5() {
        this.orthogonalNeighbors = buildOrthogonalNeighbors();
        this.diagonalNeighbors = buildDiagonalNeighbors();
        this.fillOrder = buildFillOrder();
    }

    /**
     * Finds one valid assignment for the 5x5 grid.
     *
     * @return array of length 25 with values 1..25 exactly once, or null if unsatisfiable
     */
    public int[] solve() {
        int[] grid = new int[CELL_COUNT];
        boolean[] used = new boolean[26]; // values 1..25; index 0 unused

        // Fix center value
        grid[CENTER_INDEX] = CENTER_VALUE;
        used[CENTER_VALUE] = true;

        if (backtrack(grid, used, 1 /* start from second in fillOrder */)) {
            return grid;
        }
        return null;
    }

    /**
     * Simple validator for a completed 5x5 grid.
     * This is not used during solving (except optionally from Main),
     * but is useful to verify correctness independently.
     */
    public boolean isValidSolution(int[] grid) {
        if (grid == null || grid.length != CELL_COUNT) {
            return false;
        }

        // Check all values 1..25 exactly once
        boolean[] seen = new boolean[26];
        for (int value : grid) {
            if (value < 1 || value > 25) {
                return false;
            }
            if (seen[value]) {
                return false;
            }
            seen[value] = true;
        }

        // Center constraint
        if (grid[CENTER_INDEX] != CENTER_VALUE) {
            return false;
        }

        // C1 and C2 for all cells
        for (int idx = 0; idx < CELL_COUNT; idx++) {
            int v = grid[idx];

            // C1: orthogonal neighbors must not be consecutive
            for (int neighbor : orthogonalNeighbors[idx]) {
                int nv = grid[neighbor];
                if (Math.abs(nv - v) == 1) {
                    return false;
                }
            }

            // C2: diagonal neighbors must not differ by exactly 2
            for (int neighbor : diagonalNeighbors[idx]) {
                int nv = grid[neighbor];
                if (Math.abs(nv - v) == 2) {
                    return false;
                }
            }
        }

        // C3: prime-cell sum even
        int primeSum = 0;
        for (int idx : PRIME_CELL_INDICES) {
            primeSum += grid[idx];
        }
        return primeSum % 2 == 0;
    }

    // ----------------------
    // Backtracking core
    // ----------------------

    private boolean backtrack(int[] grid, boolean[] used, int pos) {
        if (pos == fillOrder.length) {
            // All cells assigned; check prime-cell parity (C3)
            int primeSum = 0;
            for (int idx : PRIME_CELL_INDICES) {
                primeSum += grid[idx];
            }
            return (primeSum % 2 == 0);
        }

        int idx = fillOrder[pos];

        for (int value = 1; value <= 25; value++) {
            if (used[value]) {
                continue;
            }

            if (!respectsLocalConstraints(grid, idx, value)) {
                continue;
            }

            grid[idx] = value;
            used[value] = true;

            if (backtrack(grid, used, pos + 1)) {
                return true;
            }

            // undo
            grid[idx] = 0;
            used[value] = false;
        }

        return false;
    }

    /**
     * Check local constraints for placing 'value' at position 'idx':
     *  - It's only checked against already-filled neighbors.
     *  - C1 (orthogonal no consecutive) and C2 (diagonal no diff 2).
     */
    private boolean respectsLocalConstraints(int[] grid, int idx, int value) {
        // C1: orthogonal neighbors cannot be consecutive
        for (int neighbor : orthogonalNeighbors[idx]) {
            int nv = grid[neighbor];
            if (nv != 0 && Math.abs(nv - value) == 1) {
                return false;
            }
        }

        // C2: diagonal neighbors cannot differ by exactly 2
        for (int neighbor : diagonalNeighbors[idx]) {
            int nv = grid[neighbor];
            if (nv != 0 && Math.abs(nv - value) == 2) {
                return false;
            }
        }

        return true;
    }

    // ----------------------
    // Helper: adjacency and fill order
    // ----------------------

    private int[][] buildOrthogonalNeighbors() {
        int[][] neighbors = new int[CELL_COUNT][];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int idx = r * SIZE + c;
                neighbors[idx] = computeOrthogonalNeighbors(r, c);
            }
        }
        return neighbors;
    }

    private int[][] buildDiagonalNeighbors() {
        int[][] neighbors = new int[CELL_COUNT][];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int idx = r * SIZE + c;
                neighbors[idx] = computeDiagonalNeighbors(r, c);
            }
        }
        return neighbors;
    }

    private int[] computeOrthogonalNeighbors(int r, int c) {
        int[][] deltas = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        return collectNeighbors(r, c, deltas);
    }

    private int[] computeDiagonalNeighbors(int r, int c) {
        int[][] deltas = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        return collectNeighbors(r, c, deltas);
    }

    private int[] collectNeighbors(int r, int c, int[][] deltas) {
        int[] temp = new int[4];
        int count = 0;

        for (int[] d : deltas) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE) {
                temp[count++] = nr * SIZE + nc;
            }
        }

        return Arrays.copyOf(temp, count);
    }

    /**
     * Build an order of cell indices for backtracking:
     *  - First: the fixed center cell
     *  - Then: remaining cells sorted by (orthogonal + diagonal) degree descending,
     *          so heavily constrained cells are filled early.
     */
    private Integer[] buildFillOrder() {
        Integer[] order = new Integer[CELL_COUNT];
        boolean[] used = new boolean[CELL_COUNT];

        // Center first
        order[0] = CENTER_INDEX;
        used[CENTER_INDEX] = true;

        // Collect remaining indices
        int pos = 1;
        for (int i = 0; i < CELL_COUNT; i++) {
            if (!used[i]) {
                order[pos++] = i;
            }
        }

        // Sort the tail (from index 1 onwards) by descending degree
        Arrays.sort(order, 1, CELL_COUNT, (a, b) -> {
            int degA = orthogonalNeighbors[a].length + diagonalNeighbors[a].length;
            int degB = orthogonalNeighbors[b].length + diagonalNeighbors[b].length;
            return Integer.compare(degB, degA); // descending
        });

        return order;
    }

    // ----------------------
    // Helper: prime-cell indices
    // ----------------------

    private static int[] toZeroBasedIndices(int[][] oneBasedCoords) {
        int[] indices = new int[oneBasedCoords.length];
        for (int i = 0; i < oneBasedCoords.length; i++) {
            int r = oneBasedCoords[i][0] - 1; // convert to 0-based
            int c = oneBasedCoords[i][1] - 1;
            indices[i] = r * SIZE + c;
        }
        return indices;
    }
}

