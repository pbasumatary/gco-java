package com.codeoff;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class WebServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", exchange -> {
            String html = HtmlRenderer.renderHomePage();
            send(exchange, html);
        });

        server.createContext("/solve5x5", exchange -> {
            int[] grid = new Solver5x5().solve();
            send(exchange, HtmlRenderer.renderGrid("5×5 Base Solver", grid));
        });

        server.createContext("/solveMedian", exchange -> {
            Result median = new Solver5x5Median().solveAndGetResult();
            send(exchange, HtmlRenderer.renderMedianResult(median));
        });

        server.createContext("/solve6x6", exchange -> {
            int[][] grid = new Solver6x6().solve();
            send(exchange, HtmlRenderer.renderGrid2D("6×6 Solver", grid));
        });

        server.createContext("/countC1", exchange -> {
            String result = new SolverC1Counter().countSummary();
            send(exchange, HtmlRenderer.renderText("5×5 C1-Only Enumerator", result));
        });

        System.out.println("Solver WebServer running at http://localhost:8080/");
        server.start();
    }

    private static void send(HttpExchange ex, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
        ex.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }
}
