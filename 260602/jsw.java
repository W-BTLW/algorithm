import java.util.*;

class Solution {

    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, -1, 1};

    public int solution(String[] storage, String[] requests) {

        int n = storage.length;
        int m = storage[0].length();

        // 패딩
        char[][] board = new char[n + 2][m + 2];

        for (int i = 0; i < n + 2; i++) {
            Arrays.fill(board[i], '.');
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                board[i + 1][j + 1] = storage[i].charAt(j);
            }
        }

        for (String req : requests) {

            char target = req.charAt(0);

            // 크레인
            if (req.length() >= 2) {

                for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= m; j++) {

                        if (board[i][j] == target) {
                            board[i][j] = '.';
                        }
                    }
                }

            } else {

                // 외부 공기 탐색
                boolean[][] outside = getOutside(board);

                List<int[]> removeList = new ArrayList<>();

                for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= m; j++) {

                        if (board[i][j] != target) {
                            continue;
                        }

                        for (int d = 0; d < 4; d++) {

                            int nx = i + dx[d];
                            int ny = j + dy[d];

                            if (outside[nx][ny]) {
                                removeList.add(new int[]{i, j});
                                break;
                            }
                        }
                    }
                }

                for (int[] pos : removeList) {
                    board[pos[0]][pos[1]] = '.';
                }
            }
        }

        int answer = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {

                if (board[i][j] != '.') {
                    answer++;
                }
            }
        }

        return answer;
    }

    private boolean[][] getOutside(char[][] board) {

        int rows = board.length;
        int cols = board[0].length;

        boolean[][] visited = new boolean[rows][cols];

        Queue<int[]> q = new LinkedList<>();

        q.offer(new int[]{0, 0});
        visited[0][0] = true;

        while (!q.isEmpty()) {

            int[] cur = q.poll();

            int x = cur[0];
            int y = cur[1];

            for (int d = 0; d < 4; d++) {

                int nx = x + dx[d];
                int ny = y + dy[d];

                if (nx < 0 || ny < 0 || nx >= rows || ny >= cols) {
                    continue;
                }

                if (visited[nx][ny]) {
                    continue;
                }

                // 빈 공간만 이동 가능
                if (board[nx][ny] != '.') {
                    continue;
                }

                visited[nx][ny] = true;
                q.offer(new int[]{nx, ny});
            }
        }

        return visited;
    }
}

// 외부랑 접촉하는 타일을 찾아야 함 -> 겉에 padding을 주고 외부 타일을 BFS 처리
// 약간의 역발상?