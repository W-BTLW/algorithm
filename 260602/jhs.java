import java.util.*;

class Solution {

    int[] dx = {-1, 1, 0, 0};
    int[] dy = {0, 0, -1, 1};

    public int solution(String[] storage, String[] requests) {

        int n = storage.length;
        int m = storage[0].length();

        // 바깥 공기 표현을 위해 +2
        char[][] map = new char[n + 2][m + 2];

        for (int i = 0; i < n + 2; i++) {
            Arrays.fill(map[i], '*');
        }
        
        /**
        ******
        ******
        ******
        ******
        ******
        **/

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                map[i + 1][j + 1] = storage[i].charAt(j);
            }
        }
        
        /**
        ******
        *ABCD*
        *EFGH*
        *IJKL*
        ******
        **/

        for (String req : requests) {

            char target = req.charAt(0);

            // 외부 공기 체크
            boolean[][] outside = getOutside(map);

            List<int[]> removeList = new ArrayList<>();

            if (req.length() == 1) {
                // 지게차
                for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= m; j++) {

                        if (map[i][j] != target) {
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
            } else {
                // 크레인
                for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= m; j++) {
                        if (map[i][j] == target) {
                            removeList.add(new int[]{i, j});
                        }
                    }
                }
            }

            for (int[] pos : removeList) {
                map[pos[0]][pos[1]] = '*';
            }
        }

        int answer = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (map[i][j] != '*') {
                    answer++;
                }
            }
        }

        return answer;
    }

    private boolean[][] getOutside(char[][] map) {

        int n = map.length;
        int m = map[0].length;

        boolean[][] visited = new boolean[n][m];

        Queue<int[]> q = new LinkedList<>();

        q.offer(new int[]{0, 0});
        visited[0][0] = true;

        while (!q.isEmpty()) {

            int[] cur = q.poll();

            for (int d = 0; d < 4; d++) {

                int nx = cur[0] + dx[d];
                int ny = cur[1] + dy[d];

                if (nx < 0 || ny < 0 || nx >= n || ny >= m) {
                    continue;
                }

                if (visited[nx][ny]) {
                    continue;
                }

                // 빈 공간만 외부 공기로 확장
                if (map[nx][ny] == '*') {
                    visited[nx][ny] = true;
                    q.offer(new int[]{nx, ny});
                }
            }
        }

        return visited;
    }
}
