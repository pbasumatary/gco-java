package com.codeoff;

import java.util.Arrays;

public class HtmlRenderer {

    public static String renderHomePage() {
        return """
        <html>
        <head><title>Codeoff Constraint Solver</title></head>
        <body style='font-family:Arial;'>
        <h1>Codeoff Constraint Solvers</h1>

        <ul>
          <li><a href='/solve5x5'>Solve 5×5 Problem 1</a></li>
          <li><a href='/solveMedian'>Solve 5×5 Median Problem 2</a></li>
          <li><a href='/solve6x6'>Solve 6×6 Rook Problem 3</a></li>
          <li><a href='/countC1'>Run C1-Only Counter Problem 4</a></li>
        </ul>

        </body></html>
        """;
    }

    public static String renderGrid(String title, int[] grid) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body><h2>").append(title).append("</h2><pre>");

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++)
                sb.append(String.format("%3d ", grid[r*5 + c]));
            sb.append("\n");
        }

        sb.append("</pre><a href='/'>Back</a></body></html>");
        return sb.toString();
    }

    public static String renderGrid2D(String title, int[][] grid) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body><h2>").append(title).append("</h2><pre>");

        for (int[] row : grid) {
            for (int v : row) sb.append(String.format("%3d ", v));
            sb.append("\n");
        }

        sb.append("</pre><a href='/'>Back</a></body></html>");
        return sb.toString();
    }

    public static String renderText(String title, String text) {
        return "<html><body><h2>" + title + "</h2><pre>" +
               text + "</pre><a href='/'>Back</a></body></html>";
    }

    public static String renderMedianResult(Result result) {
        return "<html><body><h2>5×5 Median Solver</h2>" +
                "<p><b>Grid(5,5):</b> " + result.bottomRight +
                "</p><pre>" + formatGrid(result.grid) +
                "</pre><a href='/'>Back</a></body></html>";
    }

    private static String formatGrid(int[] g) {
        StringBuilder sb = new StringBuilder();
        for (int r=0;r<5;r++) {
            for (int c=0;c<5;c++) sb.append(String.format("%3d ", g[r*5+c]));
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String renderWaitPage(String targetUrl) {
    return """
    <html>
    <head>
      <meta http-equiv="refresh" content="0; URL='%s'"/>
      <title>Please Wait...</title>
    </head>
    <body style='font-family:Arial;'>
      <h2>Please wait...</h2>
      <p>Your request is being processed.</p>
      <p>You will be redirected automatically.</p>
    </body>
    </html>
    """.formatted(targetUrl);
}
}
