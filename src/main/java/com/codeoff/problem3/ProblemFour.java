package com.codeoff.problem3;

import java.util.Arrays;

/**
 * Solver for the 6x6 grid with:
 *
 * - Fixed Seed: (row 1, col 1) = 1
 * - C1 (Orthogonal): No orthogonally adjacent cells contain consecutive integers.
 * - C5 (Rook): Numbers {1, 12, 24, 36} are all in distinct rows and distinct columns.
 */
public class ProblemFour {

    private static final int SIZE = 6;
    private static final int CELL_COUNT = SIZE * SIZE;

    private static final int FIXED_INDEX = 0;   // (0,0) in 0-based
    private static final int FIXED_VALUE = 1;

    // Special rook set
    private static final int[] ROOK_VALUES = {1, 12, 24, 36};

    private final int[][] orthogonalNeighbors;
    private final Integer[] fillOrder;

    public ProblemFour() {
        this.orthogonalNeighbors = buildOrthogonalNeighbors();
        this.fillOrder = buildFillOrder();
    }

    /**
     * Finds one valid assignment or returns null.
     */
    public int[] solve() {
        int[] grid = new int[CELL_COUNT];
        boolean[] used = new boolean[37];  // 1..36
        boolean[] rookRows = new boolean[SIZE];
        boolean[] rookCols = new boolean[SIZE];

        // Fixed seed at top-left
        grid[FIXED_INDEX] = FIXED_VALUE;
        used[FIXED_VALUE] = true;

        // Rook constraint: mark row/col occupied by value 1
        rookRows[0] = true;
        rookCols[0] = true;

        if (backtrack(grid, used, rookRows, rookCols, 1)) {
            return grid;
        }
        return null;
    }

    /**
     * Full validator for a completed grid.
     */
    public boolean isValidSolution(int[] grid) {
        if (grid == null || grid.length != CELL_COUNT) {
            return false;
        }

        // Values 1..36 exactly once
        boolean[] seen = new boolean[37];
        for (int v : grid) {
            if (v < 1 || v > 36) return false;
            if (seen[v]) return false;
            seen[v] = true;
        }

        // Fixed seed
        if (grid[FIXED_INDEX] != FIXED_VALUE) {
            return false;
        }

        // C1: no orthogonal consecutive
        for (int idx = 0; idx < CELL_COUNT; idx++) {
            int v = grid[idx];
            for (int nb : orthogonalNeighbors[idx]) {
                int nv = grid[nb];
                if (Math.abs(v - nv) == 1) {
                    return false;
                }
            }
        }

        // C5: rook rows/cols
        int[] rookRowsSeen = new int[ROOK_VALUES.length];
        int[] rookColsSeen = new int[ROOK_VALUES.length];

        for (int i = 0; i < ROOK_VALUES.length; i++) {
            int value = ROOK_VALUES[i];
            int pos = findValue(grid, value);
            if (pos < 0) return false;
            rookRowsSeen[i] = pos / SIZE;
            rookColsSeen[i] = pos % SIZE;
        }

        if (!allDistinct(rookRowsSeen)) return false;
        if (!allDistinct(rookColsSeen)) return false;

        return true;
    }

    private int findValue(int[] grid, int value) {
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == value) return i;
        }
        return -1;
    }

    private boolean allDistinct(int[] arr) {
        boolean[] seen = new boolean[SIZE];
        for (int x : arr) {
            if (seen[x]) return false;
            seen[x] = true;
        }
        return true;
    }

    // ----------------- Backtracking core -----------------

    private boolean backtrack(int[] grid,
                              boolean[] used,
                              boolean[] rookRows,
                              boolean[] rookCols,
                              int pos) {

        if (pos == fillOrder.length) {
            return true; // all cells filled; final constraints already enforced locally
        }

        int idx = fillOrder[pos];
        int row = idx / SIZE;
        int col = idx % SIZE;

        for (int value = 1; value <= 36; value++) {
            if (used[value]) continue;

            if (!respectsLocalConstraints(grid, idx, row, col, value, rookRows, rookCols)) {
                continue;
            }

            grid[idx] = value;
            used[value] = true;

            boolean placedRook = isRookValue(value);
            if (placedRook) {
                rookRows[row] = true;
                rookCols[col] = true;
            }

            if (backtrack(grid, used, rookRows, rookCols, pos + 1)) {
                return true;
            }

            // undo
            if (placedRook) {
                rookRows[row] = false;
                rookCols[col] = false;
            }
            grid[idx] = 0;
            used[value] = false;
        }

        return false;
    }

    private boolean respectsLocalConstraints(int[] grid,
                                             int idx,
                                             int row,
                                             int col,
                                             int value,
                                             boolean[] rookRows,
                                             boolean[] rookCols) {
        // C1: orthogonal neighbors must not be consecutive
        for (int nb : orthogonalNeighbors[idx]) {
            int nv = grid[nb];
            if (nv != 0 && Math.abs(nv - value) == 1) {
                return false;
            }
        }

        // C5: rook constraint
        if (isRookValue(value)) {
            if (rookRows[row] || rookCols[col]) {
                return false;
            }
        }

        return true;
    }

    private boolean isRookValue(int v) {
        for (int rv : ROOK_VALUES) {
            if (rv == v) return true;
        }
        return false;
    }

    // ----------------- Helpers: adjacency & fill order -----------------

    private int[][] buildOrthogonalNeighbors() {
        int[][] neighbors = new int[CELL_COUNT][];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int idx = r * SIZE + c;
                neighbors[idx] = computeOrthNeighbors(r, c);
            }
        }
        return neighbors;
    }

    private int[] computeOrthNeighbors(int r, int c) {
        int[][] deltas = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
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
     * Fill order:
     *  - First: fixed top-left cell (index 0).
     *  - Then: remaining cells sorted by descending orthogonal degree.
     */
    private Integer[] buildFillOrder() {
        Integer[] order = new Integer[CELL_COUNT];
        boolean[] usedIndex = new boolean[CELL_COUNT];

        order[0] = FIXED_INDEX;
        usedIndex[FIXED_INDEX] = true;

        int pos = 1;
        for (int i = 0; i < CELL_COUNT; i++) {
            if (!usedIndex[i]) {
                order[pos++] = i;
            }
        }

        Arrays.sort(order, 1, CELL_COUNT, (a, b) -> {
            int da = orthogonalNeighbors[a].length;
            int db = orthogonalNeighbors[b].length;
            return Integer.compare(db, da); // descending
        });

        return order;
    }
}
