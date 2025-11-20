package com.codeoff.gridmedian;

public class Main {

    public static void main(String[] args) {
        System.out.println("Finding Median Matrix....");
        GridSolverMedian solver = new GridSolverMedian();
        int[] result = solver.solve();
        System.out.println("Grid is \n");
        printMatrix(result);

        if (result == null) {
            System.out.println("No valid solution found.");
            return;
        }

        int value = result[24]; // cell (5,5)
        System.out.println("Solution found.");
        System.out.println("Bottom-right value (Grid 5,5) = " + value);
    }

    private static void printMatrix(int[] grid) {
    final int size = 5;
    for (int r = 0; r < size; r++) {
        for (int c = 0; c < size; c++) {
            System.out.printf("%3d ", grid[r * size + c]);
        }
        System.out.println();
    }
}

}
