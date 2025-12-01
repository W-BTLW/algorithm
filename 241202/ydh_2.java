// PCCP 기출문제 1회 2번 석유시추
/*
효율성 테스트
테스트 케이스 1 〉 통과 (12.13ms, 65.9MB)
테스트 케이스 2 〉 통과 (56.13ms, 78MB)
테스트 케이스 3 〉 통과 (56.61ms, 76.7MB)
테스트 케이스 4 〉 통과 (14.57ms, 65.5MB)
테스트 케이스 5 〉 통과 (56.12ms, 81.4MB)
테스트 케이스 6 〉 통과 (14.91ms, 65.9MB)
*/
import java.util.*;

class Solution {
    private int[][] visited; // 방문자 배열
    private int[] dx = {0, 0, 1, -1};
    private int[] dy = {1, -1, 0, 0};
    private int N, M;

    private int dfs(int[][] land, int x, int y, int idx) {
        visited[x][y] = idx; // 현재 방문 idx 적기
        int size = 1;

        for (int k = 0; k < 4; k++) {
            // 4방 탐샋
            int nx = x + dx[k];
            int ny = y + dy[k];

            // land범위이고 아직 방문하지 않았다면
            if (nx >= 0 && nx < N && ny >= 0 && ny < M && land[nx][ny] == 1 && visited[nx][ny] != idx) {
                size += dfs(land, nx, ny, idx); // 크기 합산
            }
        }
        return size;
    }

    public int solution(int[][] land) {
        int answer = 0;

        N = land.length;
        M = land[0].length;
        visited = new int[N][M];

        // idx별 사이즈 저장할 맵
        Map<Integer, Integer> sizeMap = new HashMap<>();

        int idx = 1;
        for (int i=0; i<N; i++) {
            for (int j=0; j<M; j++) {
                if (land[i][j] == 1 && visited[i][j] == 0) {
                    // 석유나오고 방문안한놈이면 dfs 돌리기
                    int size = dfs(land,i,j,idx);
                    sizeMap.put(idx++, size); // 결과값 저장
                }
            }
        }

        // 열 기준 최대 크기를 구해야 하므로 이중포문 반대로
        for (int i=0; i<M; i++) {
            int temp = 0; // 정답 구하기 위한 임시 변수
            boolean[] already = new boolean[idx]; // 이미 방문한 idx면 방문안하기 위함
            for (int j=0; j<N; j++) {
                int v = visited[j][i];
                if (v == 0 || already[v]) continue;
                // 빈땅 아니고 이미 계산한 값 아니면 temp에 더해주고 already도 체크
                temp += sizeMap.get(v);
                already[v] = true;
            }
            // 최대값 비교
            if (answer < temp) answer = temp;
        }

        return answer;
    }
}
