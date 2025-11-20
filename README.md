# 5x5 Constraint Grid Solver (Great Java Code-Off)

This repository contains a Java solution to a constrained 5×5 grid puzzle.

The task is to fill a 5×5 grid (25 cells) using each of the numbers 1–25 **exactly once** so that the following constraints are satisfied:

1. **Fixed Seed (Center Cell)**  
   The center cell (Row 3, Column 3) must be set to **13**.

2. **C1 – Orthogonal Constraint**  
   No two *orthogonally* adjacent cells (sharing a side: up, down, left, right) may contain **consecutive integers**.  
   In other words, for a cell containing `N`, its orthogonal neighbors cannot contain `N - 1` or `N + 1`.

3. **C2 – Diagonal Constraint**  
   No two *diagonally* adjacent cells (sharing a corner) may contain numbers whose values differ by **exactly 2**.  
   For a cell containing `N`, its diagonal neighbors cannot contain `N - 2` or `N + 2`.

4. **C3 – Prime/Even Sum Constraint**  
   The sum of the values in the following 10 “prime-numbered cells” must be an **even number**:

   - Grid(1,2), Grid(1,4)
   - Grid(2,1), Grid(2,3)
   - Grid(3,2), Grid(3,4)
   - Grid(4,1), Grid(4,3)
   - Grid(5,2), Grid(5,4)

   Coordinates are 1-based: `Grid(row, column)`.

5. **Submission Format**  
   The grid must be output as a single string of **25 comma-separated integers**, read row-by-row, left-to-right.

   Example format (not actual answer):

   ```text
   v1,v2,v3,...,v25
