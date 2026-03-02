import java.util.*;

class Solution {

    static int N, M;
    static int[][] board;
    static boolean[][][][] visited;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};

    static class Node {
        int rx, ry, bx, by, dist;

        Node(int rx, int ry, int bx, int by, int dist) {
            this.rx = rx;
            this.ry = ry;
            this.bx = bx;
            this.by = by;
            this.dist = dist;
        }
    }

    public int solution(int[][] maze) {

        board = maze;
        N = maze.length;
        M = maze[0].length;

        visited = new boolean[N][M][N][M];

        int rStartX = 0, rStartY = 0;
        int bStartX = 0, bStartY = 0;
        int rEndX = 0, rEndY = 0;
        int bEndX = 0, bEndY = 0;

        // 시작/도착 위치 찾기
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (board[i][j] == 1) {
                    rStartX = i;
                    rStartY = j;
                } else if (board[i][j] == 2) {
                    bStartX = i;
                    bStartY = j;
                } else if (board[i][j] == 3) {
                    rEndX = i;
                    rEndY = j;
                } else if (board[i][j] == 4) {
                    bEndX = i;
                    bEndY = j;
                }
            }
        }

        Queue<Node> q = new LinkedList<>();
        q.offer(new Node(rStartX, rStartY, bStartX, bStartY, 0));
        visited[rStartX][rStartY][bStartX][bStartY] = true;

        while (!q.isEmpty()) {

            Node cur = q.poll();

            // 두 수레 모두 도착하면 종료
            if (cur.rx == rEndX && cur.ry == rEndY
             && cur.bx == bEndX && cur.by == bEndY) {
                return cur.dist;
            }

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {

                    int nrx = cur.rx;
                    int nry = cur.ry;
                    int nbx = cur.bx;
                    int nby = cur.by;

                    // 빨강이 도착 안했으면 이동
                    if (!(cur.rx == rEndX && cur.ry == rEndY)) {
                        nrx = cur.rx + dx[i];
                        nry = cur.ry + dy[i];
                    }

                    // 파랑이 도착 안했으면 이동
                    if (!(cur.bx == bEndX && cur.by == bEndY)) {
                        nbx = cur.bx + dx[j];
                        nby = cur.by + dy[j];
                    }

                    // 빨강 범위/벽 체크 (도착 안한 경우만)
                    if (!(cur.rx == rEndX && cur.ry == rEndY)) {
                        if (!inRange(nrx, nry) || board[nrx][nry] == 5)
                            continue;
                    }

                    // 파랑 범위/벽 체크
                    if (!(cur.bx == bEndX && cur.by == bEndY)) {
                        if (!inRange(nbx, nby) || board[nbx][nby] == 5)
                            continue;
                    }

                    // 겹침 방지
                    if (nrx == nbx && nry == nby)
                        continue;

                    // 자리 교환 방지
                    if (nrx == cur.bx && nry == cur.by
                     && nbx == cur.rx && nby == cur.ry)
                        continue;

                    if (!visited[nrx][nry][nbx][nby]) {
                        visited[nrx][nry][nbx][nby] = true;
                        q.offer(new Node(nrx, nry, nbx, nby, cur.dist + 1));
                    }
                }
            }
        }

        return 0; // 도달 불가
    }

    static boolean inRange(int x, int y) {
        return x >= 0 && y >= 0 && x < N && y < M;
    }
}
