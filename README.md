This repository contains a collection of Java-based solvers for several constraint-driven grid puzzles. Each puzzle involves filling a grid with unique integers while respecting a specific set of adjacency and global restrictions. The solvers use backtracking with heuristics, adjacency precomputation, and optional Docker and web-API execution modes.
The projects included are:
1. 5×5 Grid Solver (Core Constraint Engine)
A general solver for a 5×5 grid using unique values 1–25 under multiple spatial constraints.
Includes:
Orthogonal adjacency rules
Diagonal adjacency rules
Global parity checks
Fixed-value seed
Output as row-major submission strings
2. 5×5 Median-Constraint Solver
An extended solver that implements all baseline constraints plus an additional median requirement applied to the first row.
The program returns the final value at a specific cell once a valid grid is found.
3. 6×6 Rook-Constraint Solver
A solver for a 6×6 grid using values 1–36.
Additional logic enforces a "rook placement" requirement for a special subset of values.
The solver prints a valid grid in submission format.
4. 5×5 C1-Only Enumerator
A specialized enumeration engine designed to count all 5×5 grids that satisfy only the orthogonal adjacency rule.
Due to the combinatorial size of the search space, this tool includes optional depth and solution limits for safe experimentation.