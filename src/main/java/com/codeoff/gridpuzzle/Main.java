package com.codeoff.gridpuzzle;

/**
 * Entry point for the 5x5 constraint grid solver.
 *
 * Prints a valid grid, both in pretty 5x5 form and
 * as the required comma-separated submission string.
 */
public class Main {

    public static void main(String[] args) {
        GridSolver solver = new GridSolver();
        int[] solution = solver.solve();

        if (solution == null) {
            System.out.println("No valid solution found.");
            return;
        }

        // Pretty-print 5x5 grid
        System.out.println("Valid 5x5 grid:");
        printGrid(solution);

        // Print submission string
        System.out.println();
        System.out.println("Submission string (row-major, comma-separated):");
        System.out.println(toSubmissionString(solution));

        // Optional: double-check validity
        if (!solver.isValidSolution(solution)) {
            System.err.println("WARNING: The computed grid does NOT satisfy all constraints.");
        }
    }

    private static void printGrid(int[] grid) {
        final int size = 5;
        for (int r = 0; r < size; r++) {
            StringBuilder line = new StringBuilder();
            for (int c = 0; c < size; c++) {
                if (c > 0) {
                    line.append("\t");
                }
                line.append(grid[r * size + c]);
            }
            System.out.println(line);
        }
    }

    private static String toSubmissionString(int[] grid) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(grid[i]);
        }
        return sb.toString();
    }
}

