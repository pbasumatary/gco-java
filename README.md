Find one valid assignment for a 5 by 5 grid (25 cells, using values 1-25 exactly once) that satisfies the following three highly restrictive constraints:
Fixed Seed: The center cell, Row 3, Column 3, must be set to the value 13.
C1 (Orthogonal): No two orthogonally adjacent cells (sharing a side: up, down, left, right) can contain consecutive numbers (i.e., if a cell holds N, its neighbors cannot hold N-1 or N+1).
C2 (Diagonal): No two diagonally adjacent cells (sharing a corner) can contain numbers with a difference of exactly 2 (i.e., N and N plus or minus 2).
C3 (Prime/Even Sum): The sum of the values in the 10 prime-numbered cells must be an even number.
(Hint: The prime-numbered cells are Grid(1,2),Grid(1,4),Grid(2,1),Grid(2,3),Grid(3,2),Grid(3,4),Grid(4,1),Grid(4,3),Grid(5,2),Grid(5,4)).
Submission Format: Submit the grid as a single string of 25 comma-separated integers, reading the grid row-by-row, left-to-right.
