import java.util.*;

class Solution {

    int n, m;
    int answer = Integer.MAX_VALUE;

    int[] dr = {-1, 1, 0, 0};
    int[] dc = {0, 0, -1, 1};

    public int solution(int[][] maze) {

        n = maze.length;
        m = maze[0].length;

        int rr = 0, rc = 0, br = 0, bc = 0;
        int rgR = 0, rgC = 0, bgR = 0, bgC = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (maze[i][j] == 1) { rr = i; rc = j; }
                else if (maze[i][j] == 2) { br = i; bc = j; }
                else if (maze[i][j] == 3) { rgR = i; rgC = j; }
                else if (maze[i][j] == 4) { bgR = i; bgC = j; }
            }
        }

        boolean[][] redVisited = new boolean[n][m];
        boolean[][] blueVisited = new boolean[n][m];

        redVisited[rr][rc] = true;
        blueVisited[br][bc] = true;

        dfs(rr, rc, br, bc, rgR, rgC, bgR, bgC,
            redVisited, blueVisited, maze, 0);

        return answer == Integer.MAX_VALUE ? 0 : answer;
    }

    void dfs(int rr, int rc, int br, int bc,
             int rgR, int rgC, int bgR, int bgC,
             boolean[][] redVisited,
             boolean[][] blueVisited,
             int[][] maze,
             int depth) {

        if (depth >= answer) return;

        if (rr == rgR && rc == rgC &&
            br == bgR && bc == bgC) {
            answer = depth;
            return;
        }

        for (int i = 0; i < 4; i++) {

            int nrr = rr;
            int nrc = rc;

            // 빨강 이동
            if (!(rr == rgR && rc == rgC)) {
                nrr = rr + dr[i];
                nrc = rc + dc[i];

                if (!isValid(nrr, nrc, maze) || redVisited[nrr][nrc])
                    continue;
            }

            for (int j = 0; j < 4; j++) {

                int nbr = br;
                int nbc = bc;

                // 파랑 이동
                if (!(br == bgR && bc == bgC)) {
                    nbr = br + dr[j];
                    nbc = bc + dc[j];

                    if (!isValid(nbr, nbc, maze) || blueVisited[nbr][nbc])
                        continue;
                }

                // 같은 칸 금지
                if (nrr == nbr && nrc == nbc) continue;

                // 자리 바꾸기 금지
                if (nrr == br && nrc == bc &&
                    nbr == rr && nbc == rc) continue;

                redVisited[nrr][nrc] = true;
                blueVisited[nbr][nbc] = true;

                dfs(nrr, nrc, nbr, nbc,
                    rgR, rgC, bgR, bgC,
                    redVisited, blueVisited,
                    maze, depth + 1);

                redVisited[nrr][nrc] = false;
                blueVisited[nbr][nbc] = false;
            }
        }
    }

    boolean isValid(int r, int c, int[][] maze) {
        return r >= 0 && c >= 0 &&
               r < n && c < m &&
               maze[r][c] != 5;
    }
}
