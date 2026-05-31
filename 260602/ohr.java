import java.util.*;

class Solution {
    
    static int N, M;
    static char[][] map;
    static boolean[][] visited;
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};
    
    public int solution(String[] storage, String[] requests) {
        int answer = 0;
        
        N = storage.length;
        M = storage[0].length();
        
        map = new char[N+2][M+2];
        
        // 테두리 빈 값으로
        for(int i=0;i<N+2;i++){
            for(int j=0;j<M+2;j++){
                map[i][j] = ' ';
            }
        }
        
        // storage -> map 변환
        for(int i=0;i<N;i++){
            for(int j=0;j<M;j++){
                map[i+1][j+1] = storage[i].charAt(j);
            }
        }
        
        // 요청 지게차 or 크레인
        for (int i=0;i<requests.length;i++){
            char target = requests[i].charAt(0);
            if (requests[i].length() == 1) lift(target);  // 길이가 1이면 지게차
            else crane(target);  // 아니면 크레인
        }
        
        for (int i=1;i<=N;i++){
            for (int j=1;j<=M;j++){
                if(map[i][j] != ' ') answer ++;
            }
        }
        
        return answer;
    }
    
    static void lift (char target) {
        visited = new boolean[N+2][M+2];
        boolean[][] remove = new boolean[N+2][M+2];
        
        Queue<Pos> q = new LinkedList<>();
        visited[0][0] = true;
        q.offer(new Pos(0, 0));
        
        while (!q.isEmpty()) {
            Pos cur = q.poll();
            for (int d=0;d<4;d++){
                int nr = cur.r + dr[d];
                int nc = cur.c + dc[d];
                
                if (nr < 0 || nr >=N+2 || nc < 0 || nc >= M+2) continue;
                if (visited[nr][nc]) continue;
                
                visited[nr][nc] = true;
                if (map[nr][nc] == target) {  // 지워야하는 문자면
                    remove[nr][nc] = true;    // remove 배열에 등록
                } else if (map[nr][nc] == ' ') {      // 빈 칸으로만 이동하면서 봐야함
                    q.offer(new Pos(nr, nc));         // target을 queue에 넣고 탐색하면 타고 들어가서 확인함
                }
                
            }
        }
        
        for (int i=0;i<=N+1;i++){
            for (int j=0;j<=M+1;j++){
                if (remove[i][j]) map[i][j] = ' ';
            }
        }
        
    }
    
    
    static void crane(char target) {
        for (int i=1;i<=N;i++){
            for(int j=1;j<=M;j++){
                if (map[i][j] == target) map[i][j] = ' ';
            }
        }
    }
    
    
    static class Pos{
    int r, c;
        Pos (int r, int c){
            this.r = r;
            this.c = c;
        }
    }
}
