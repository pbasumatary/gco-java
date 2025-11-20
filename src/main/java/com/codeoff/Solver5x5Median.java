package com.codeoff;

import java.util.Arrays;

public class Solver5x5Median {

    private static final int SIZE = 5;
    private static final int CELL_COUNT = SIZE * SIZE;

    // From problem 1
    private static final int CENTER_INDEX = 12;
    private static final int CENTER_VALUE = 13;

    private static final int[] PRIME_CELL_INDICES = toZeroBasedIndices(new int[][]{
            {1,2}, {1,4},
            {2,1}, {2,3},
            {3,2}, {3,4},
            {4,1}, {4,3},
            {5,2}, {5,4}
    });

    private final int[][] orthogonalNeighbors;
    private final int[][] diagonalNeighbors;
    private final Integer[] fillOrder;

    public Solver5x5Median() {
        orthogonalNeighbors = buildOrthogonalNeighbors();
        diagonalNeighbors = buildDiagonalNeighbors();
        fillOrder = buildFillOrder();
    }

    public Result solveAndGetResult() {
        int [] g = solve();
        return new Result(g, g[24]);
    }

    public int[] solve() {
        int[] grid = new int[CELL_COUNT];
        boolean[] used = new boolean[26];

        grid[CENTER_INDEX] = CENTER_VALUE;
        used[CENTER_VALUE] = true;

        if (backtrack(grid, used, 1))
            return grid;

        return null;
    }

    private boolean backtrack(int[] grid, boolean[] used, int pos) {
        if (pos == fillOrder.length) {
            return validateFinal(grid);
        }

        int idx = fillOrder[pos];

        for (int v = 1; v <= 25; v++) {
            if (used[v]) continue;

            if (!localOK(grid, idx, v)) continue;

            grid[idx] = v;
            used[v] = true;

            if (backtrack(grid, used, pos + 1))
                return true;

            grid[idx] = 0;
            used[v] = false;
        }

        return false;
    }

    // Final validation including median C4
    private boolean validateFinal(int[] grid) {

        // C3: prime-cell sum even
        int primeSum = 0;
        for (int i : PRIME_CELL_INDICES)
            primeSum += grid[i];
        if (primeSum % 2 != 0) return false;

        // C4: top-row median must be 14
        int[] row1 = Arrays.copyOfRange(grid, 0, 5);
        Arrays.sort(row1);
        if (row1[2] != 14)
            return false;

        return true;
    }

    private boolean localOK(int[] grid, int idx, int v) {

        // C1 ortho consecutive
        for (int n : orthogonalNeighbors[idx]) {
            int nv = grid[n];
            if (nv != 0 && Math.abs(nv - v) == 1)
                return false;
        }

        // C2 diagonal ±2
        for (int n : diagonalNeighbors[idx]) {
            int nv = grid[n];
            if (nv != 0 && Math.abs(nv - v) == 2)
                return false;
        }

        return true;
    }

    // --- adjacency helpers (same as previous project) ---

    private int[][] buildOrthogonalNeighbors() {
        int[][] result = new int[CELL_COUNT][];
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                result[r*SIZE+c] = collect(r,c,new int[][]{{-1,0},{1,0},{0,-1},{0,1}});
        return result;
    }

    private int[][] buildDiagonalNeighbors() {
        int[][] result = new int[CELL_COUNT][];
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                result[r*SIZE+c] = collect(r,c,new int[][]{{-1,-1},{-1,1},{1,-1},{1,1}});
        return result;
    }

    private int[] collect(int r, int c, int[][] deltas) {
        int[] tmp = new int[4];
        int count = 0;

        for (int[] d : deltas) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (nr>=0 && nr<SIZE && nc>=0 && nc<SIZE)
                tmp[count++] = nr*SIZE + nc;
        }
        return Arrays.copyOf(tmp, count);
    }

    // Fill center first, then descending degree
    private Integer[] buildFillOrder() {
        Integer[] order = new Integer[25];
        boolean[] used = new boolean[25];

        order[0] = CENTER_INDEX;
        used[CENTER_INDEX] = true;

        int pos=1;
        for(int i=0;i<25;i++)
            if(!used[i]) order[pos++] = i;

        Arrays.sort(order,1,25,(a,b) -> {
            int da = orthogonalNeighbors[a].length + diagonalNeighbors[a].length;
            int db = orthogonalNeighbors[b].length + diagonalNeighbors[b].length;
            return Integer.compare(db, da);
        });

        return order;
    }

    private static int[] toZeroBasedIndices(int[][] coords) {
        int[] arr = new int[coords.length];
        for (int i=0;i<coords.length;i++)
            arr[i] = (coords[i][0]-1)*5 + (coords[i][1]-1);
        return arr;
    }
}

class Result {
    public final int[] grid;
    public final int bottomRight;
    public Result(int[] g, int br) { this.grid=g; this.bottomRight=br; }
}
