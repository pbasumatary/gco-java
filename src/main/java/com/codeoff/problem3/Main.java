package com.codeoff.problem3;

/**
 * CLI entry point for the 6x6 rook puzzle.
 */
public class Main {

    public static void main(String[] args) {
        ProblemFour solver = new ProblemFour();
        int[] solution = solver.solve();

        if (solution == null) {
            System.out.println("No valid solution found.");
            return;
        }

        System.out.println("Valid 6x6 grid:");
        printGrid(solution);

        System.out.println();
        System.out.println("Submission string (row-major, comma-separated):");
        System.out.println(toSubmissionString(solution));

        if (!solver.isValidSolution(solution)) {
            System.err.println("WARNING: solution failed validation!");
        }
    }

    private static void printGrid(int[] grid) {
        final int size = 6;
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
