package com.codeoff.gridc1;

import java.math.BigInteger;

/**
 * CLI entry point for the 5x5 C1-only grid counter.
 *
 * NOTE: Full enumeration is extremely expensive.
 * This main method includes an optional node/solution limit
 * so you can sanity-check the logic without running forever.
 */
public class Main {

    public static void main(String[] args) {
        // Optional limits for demo / testing; set to null for "unbounded".
        Long maxNodes = null;       // e.g. 10_000_000L
        BigInteger maxSolutions = null; // e.g. new BigInteger("1000000");

        C1Counter counter = new C1Counter(maxNodes, maxSolutions);

        System.out.println("Starting C1-only 5x5 grid enumeration...");
        BigInteger count = counter.countAll();

        System.out.println("Search finished (may be partial depending on limits).");
        System.out.println("Nodes visited    : " + counter.getNodesVisited());
        System.out.println("Solutions found  : " + count);
    }
}
