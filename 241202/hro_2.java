import java.util.*;

class Solution {

    static int N, M;
    static boolean[][] visited;  // 방문여부
    static int [][] group;       // 그룹 번호 매긴 map
    static int [] groupSize;     // 그룹별 사이즈
    static int [][] map;         // 그냥 내가 쓰기 편하게 (=land)

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    public int solution(int[][] land) {
        int answer = 0;

        N = land.length;
        M = land[0].length;

        map = new int[N][M];
        map = land;

        visited = new boolean[N][M];
        group = new int[N][M];
        groupSize = new int[N*M+1];  // 그룹최댓값 (번호1부터 쓰고싶어서 +1)

        int id = 0;  // 그룹id
        for(int i=0;i<N;i++){
            for(int j=0;j<M;j++){
                if(map[i][j] == 1 && !visited[i][j]){
                    id ++; 
                    bfwLabel(i,j,id);
                }
            }
        }

        //---------group으로 탐색--------------

        for(int c=0;c<M;c++){
            boolean[] seen = new boolean[id+1];
            int sum = 0;
            for(int r=0;r<N;r++){
                int g = group[r][c];
                if(g>0 && !seen[g]){
                    seen[g] = true;
                    sum += groupSize[g];
                }
                answer = Math.max(answer, sum);
            }
        }

        return answer;
    }

    public void bfwLabel(int sr, int sc, int id){
        // sr, sc : 현재위치
        // id : 덩어리 번호

        Queue<Pos> q = new LinkedList<>();
        visited[sr][sc] = true;  // 방문처리
        group[sr][sc] = id;
        q.offer(new Pos(sr, sc));

        int size = 0;
        while (!q.isEmpty()){
            Pos cur = q.poll();
            size++;  // 크기 측정

            for(int d=0;d<4;d++){
                int nr = cur.r + dr[d];
                int nc = cur.c + dc[d];
                
                if(nr<0 || nr>=N || nc <0 || nc>=M) continue;
                if(visited[nr][nc] || map[nr][nc] == 0) continue;

                visited[nr][nc] = true;
                group[nr][nc] = id;
                q.offer(new Pos(nr, nc));
            }
        }
        groupSize[id] = size;
    }

    static class Pos{
    int r, c;
    Pos (int r, int c){
        this.r = r;
        this.c = c;
    }
}
}

