import java.util.*;

class Solution {

    int[][] land;
    Map<Integer, Integer> indexScoreMap;
    int N, M;

    public int solution(int[][] land) {
        int answer = 0;
        this.land = land;
        N = land.length;
        M = land[0].length;

        // 인덱스별 스코어보드
        indexScoreMap = new HashMap<>();

        // 인덱스별로 색칠하기
        int index = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (land[i][j] == 1) {
                    index += 1;
                    int score = bfs(i, j, index);
                    indexScoreMap.put(index, score);
                }
            }
        }

        answer = calScore();

        return answer;
    }

    // bfs로 색칠
    public int bfs(int startX, int startY, int index) {

        int[] dx = {0,0,-1,1};
        int[] dy = {-1,1,0,0};
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{startX, startY});

        int score = 0;
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];

            if (land[x][y] != 1) continue;
            score++;
            land[x][y] = index;

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if (nx >= 0 && nx < N && ny >= 0 && ny < M && land[nx][ny] == 1) {
                    q.add(new int[]{nx, ny});
                }
            }
        }
        return score;
    }

    // 열방향 시추관 탐색
    public int calScore() {
        int answer = 0;
        for (int b = 0; b < M; b++) {
            int tempAnswer = 0;
            Set<Integer> seenIndex = new HashSet<>();
            for (int a = 0; a < N; a++) {
                int nowIndex = land[a][b];
                // nowIndex가 처음 추가될때만 true 반환
                if (nowIndex != 0 && seenIndex.add(nowIndex)) {
                    tempAnswer += indexScoreMap.get(nowIndex);
                }
            }
            answer = Math.max(answer, tempAnswer);
        }
        return answer;
    }
}
