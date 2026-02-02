class Solution {
    // DFS (재귀)

    static int N, M;
    static int[][] map;
    static boolean[][] rVisited, bVisited;
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};
    static int answer = Integer.MAX_VALUE;  // 최소 턴을 구해야하니까

    public int solution(int[][] maze) {
        /**
         * 1->3
         * 2->4
         * 5(벽)으로 이동 불가
         * 같은 칸에 두 수레 동시에 위치 불가
         * 이미 방문한 칸 재방문 불가
         * 
         * 1 4
         * 0 0
         * 2 3
         * 
         * 1 0 2
         * 0 0 0
         * 5 0 5
         * 4 0 3
         */
        map = maze;
        N = maze.length;
        M = maze[0].length;
        rVisited = new boolean[N][M];
        bVisited = new boolean[N][M];

        int srr = 0;
        int src = 0;
        int sbr = 0;
        int sbc = 0;

        /* red, blue 초기 위치 찾기 */
        for (int i=0;i<N;i++){
            for (int j=0;j<M;j++){
                if (map[i][j] == 1) {
                    srr = i;
                    sbc = j;
                } else if (map[i][j] == 2){
                    sbr = i;
                    sbc = j;
                }
            }
        }

        rVisited[srr][src] = true;
        bVisited[sbr][sbc] = true;

        dfs(srr, src, sbr, sbc, 0);

        answer = (answer == Integer.MAX_VALUE) ? 0 : answer;

       return answer;
    }

    public void dfs(int rr, int rc, int br, int bc, int turnCount) {
        boolean rArrived = false;
        boolean bArrived = false;

        // 현재위치는 수레의 도착지?
        if(map[rr][rc]==3) rArrived = true;
        if(map[br][bc]==4) bArrived = true;

        // 두 수레 모두 true면 재귀 끝
        if (rArrived && bArrived) {
            answer = Math.min(answer, turnCount);
            return;
        }

        // 이미 현재 턴이 최솟값보다 크면 재귀 끝
        if (turnCount >= answer) return;

        // 사방탐색
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){

                // red 또는 blue 중 이미 도착했으면 제자리, 아니면 움직임
                int nrr = rArrived ? rr : rr + dr[i];
                int nrc = rArrived ? rc : rc + dc[i];
                int nbr = bArrived ? br : br + dr[j];
                int nbc = bArrived ? bc : bc + dc[j];

                if(canMove(rr, rc, br, bc, nrr, nrc, nbr, nbc, rArrived, bArrived)) {
                    // 움직일 수 있으면 방문처리
                    if (!rArrived) rVisited[nrr][nrc] = true;
                    if (!bArrived) bVisited[nbr][nbc] = true;

                    dfs (nrr, nrc, nbr, nbc, turnCount+1);

                    // 다음 경우의 수를 위해 방문 기록 삭제
                    if (!rArrived) rVisited[nrr][nrc] = false;
                    if (!bArrived) bVisited[nbr][nbc] = false;
                }
            }
        }  
    }

    public boolean canMove(int rr, int rc, int br, int bc, int nrr, int nrc, int nbr, int nbc, boolean rDone, boolean bDone){

        // 벽
        if (nrr<0 || nrr>=N || nrc<0 || nrc>=M || map[nrr][nrc]==5) return false;
        if (nbr<0 || nbr>=N || nbc<0 || nbc>=M || map[nbr][nbc]==5) return false;

        // 방문했던 곳 금지
        if (!rDone && rVisited[nrr][nrc]) return false;
        if (!bDone && bVisited[nbr][nbc]) return false;

        // 두 수레 같은 위치 금지
        if (nrr == nbr && nrc == nbc) return false;

        // 두 수레 교차 이동 금지
        if (nrr == br && nrc == bc && nbr == rr && nbc == rc) return false;

        return true;
    }


}