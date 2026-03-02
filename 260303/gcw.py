from collections import deque

def solution(maze):
    # 빨강/파랑 수레가 "동시에" 한 턴에 한 칸씩 움직인다 (도착 전까지는 반드시 이동)
    # 각 수레는 자기 지나온 칸을 다시 방문할 수 없다
    # 두 수레는 같은 칸에 있을 수 없고, 서로 자리를 바꾸는 것도 금지

    # 미로의 세로/가로 크기
    # (최대 4x4)
    n = len(maze)
    m = len(maze[0])

    # 4방향 이동 벡터
    # 위, 아래, 왼, 오
    DIRS = [(-1, 0), (1, 0), (0, -1), (0, 1)]


    # 최대 4x4이므로 칸이 최대 16개 뿐 --> 각 칸을 0~15 같은 번호로 매기면
    # 방문 기록을 비트마스크(mask)로 아주 빠르고 작게 저장 가능
    
    # 예: 4x4에서 id 배치
    #  0  1  2  3
    #  4  5  6  7
    #  8  9 10 11
    # 12 13 14 15
    def to_id(x, y):
        # (x, y) 좌표를 1차원 칸 번호로 변환
        # x행, y열
        # id = x * m + y
        return x * m + y

    # ============================================================
    # GPT가 알려주는 MASK의 정의와 사용하는 이유
    #
    # mask는 "정수(int) 하나"인데, 내부의 "비트(bit)"들을 이용해서 "어떤 칸을 방문했는지"를 기록하는 방식
    #
    # 칸이 16개면 비트도 16개만 있으면 됨
    # i번 칸을 방문했다 => mask의 i번 비트를 1로 만든다
    #
    # 1) 방문 표시(켜기):
    #   mask |= (1 << i)
    #   - (1 << i)는 i번째 비트만 1인 숫자
    #   - OR(|=) 연산으로 해당 비트를 1로 "켜기"
    #
    # 2) 방문 여부 확인:
    #   (mask >> i) & 1
    #   - mask를 i칸만큼 오른쪽으로 밀어서 i번째 비트를 맨 끝으로 가져온 뒤
    #   - 마지막 비트(& 1)를 보면 0/1로 방문 여부가 나옴
    #
    # 예)
    # - i=5 방문이면 mask에 2^5(=32)가 포함됨
    # - mask가 0b100000(32)면 5번 칸 방문했다는 뜻


    # 시작/도착/벽 위치를 찾아서 저장
    # 0: 빈칸
    # 1: 빨강 시작
    # 2: 파랑 시작
    # 3: 빨강 도착
    # 4: 파랑 도착
    # 5: 벽

    r_start = None  # 빨강 시작 칸 id
    b_start = None  # 파랑 시작 칸 id
    r_end = None    # 빨강 도착 칸 id
    b_end = None    # 파랑 도착 칸 id
    walls = set()   # 벽 칸 id들을 저장하는 집합

    # 모든 칸을 훑으며 값에 따라 위치 기록
    for x in range(n):
        for y in range(m):
            v = maze[x][y]
            cid = to_id(x, y)  # 현재 칸의 1차원 id

            if v == 1:
                r_start = cid
            elif v == 2:
                b_start = cid
            elif v == 3:
                r_end = cid
            elif v == 4:
                b_end = cid
            elif v == 5:
                walls.add(cid)

    # 다음 이동 후보를 만드는 함수
    # 이 함수는 "해당 수레가 현재 칸 pos에 있을 때" 4방향으로 갈 수 있는 다음 칸들을 만들어 준다.
    # 4) 미로 밖을 넘지 않을 것
    # 5) 벽이 아닐 것
    def next_cells(pos):
        # pos(1차원) -> (x,y) 좌표로 복원
        x, y = divmod(pos, m)

        for dx, dy in DIRS:
            nx, ny = x + dx, y + dy

            # 조건 4) 미로 밖 금지
            if not (0 <= nx < n and 0 <= ny < m):
                continue

            np = to_id(nx, ny)

            # 조건 5) 벽 금지
            if np in walls:
                continue

            # 조건 4,5를 통과한 "이동 가능 후보"만 반환
            yield np

    # 상태 = (rPos, bPos, rMask, bMask)
    # rPos  : 빨강 수레의 현재 칸 id
    # bPos  : 파랑 수레의 현재 칸 id
    # rMask : 빨강이 "지나온 칸" 기록(비트마스크)
    # bMask : 파랑이 "지나온 칸" 기록(비트마스크)
    #
    # 왜 mask까지 포함하는지?!
    # 조건 6) 각 수레는 자기 지나온 경로를 다시 갈 수 없음
    # 같은 위치(rPos, bPos)에 있더라도,
    # "어떤 경로로 왔는지"가 다르면 다음에 갈 수 있는 칸이 달라짐
    # 그래서 단순히 (rPos, bPos)만으로 visited 체크하면 오답 가능

    # 초기 방문 마스크 만들기
    # 시작 칸도 "지나온 경로"에 포함이므로 시작칸 비트를 1로 켜서 시작한다.

    rMask0 = 1 << r_start  # 빨강 시작칸 방문 표시 ON
    bMask0 = 1 << b_start  # 파랑 시작칸 방문 표시 ON

    # BFS 큐: (rPos, bPos, rMask, bMask, dist)
    # dist = 지금까지 걸린 턴 수
    q = deque()
    q.append((r_start, b_start, rMask0, bMask0, 0))

    # visited: 이미 탐색한 상태를 다시 탐색하지 않기 위한 집합
    visited = set()
    visited.add((r_start, b_start, rMask0, bMask0))

    while q:
        # 큐에서 하나의 상태를 꺼낸다 (가장 먼저 들어온, 가장 짧은 dist 상태)
        rPos, bPos, rMask, bMask, dist = q.popleft()

        # 목표 도달 검사
        # BFS 특성상 "처음 도달한 순간"의 dist가 최소
        if rPos == r_end and bPos == b_end:
            return dist

        # 조건 3) 도착한 수레는 고정
        # - 도착한 수레는 다음 턴부터 "움직이지 않는다" (제자리 유지)

        r_done = (rPos == r_end)  # 빨강이 이미 도착했는가?
        b_done = (bPos == b_end)  # 파랑이 이미 도착했는가?

        # 조건 2) 도착 전까지는 무조건 움직임
        # 도착하지 않았다면 반드시 4방향 중 하나로 이동해야 한다
        # 즉 "제자리 유지"는 불가능
        #
        # 조건 3) 도착하면 고정
        # 도착했다면 다음 상태에서 rPos 그대로 유지(이동 후보는 [rPos] 하나)
        r_moves = [rPos] if r_done else list(next_cells(rPos))
        b_moves = [bPos] if b_done else list(next_cells(bPos))

        # "동시에 움직임" 구현 방법
        #
        # 한 턴에서 빨강의 이동 후보 rNext와
        #   파랑의 이동 후보 bNext를 조합해서 (rNext, bNext)를 만든다.
        # 그 조합이 문제 조건을 만족하면 다음 BFS 상태로 push
        for rNext in r_moves:
            # 조건 6) 빨강: 지나온 경로 재방문 금지
            #
            # (mask >> rNext) & 1 == 1  -> 이미 방문한 칸
            # 도착한 경우(r_done)는 고정이므로 이 체크를 할 필요가 없음
            if not r_done and ((rMask >> rNext) & 1):
                continue

            for bNext in b_moves:

                # 조건 6) 파랑도 동일
                if not b_done and ((bMask >> bNext) & 1):
                    continue
                # 조건 7) 수레가 겹쳐있을 수 없음
                # 같은 턴에 같은 칸으로 이동하면 겹침
                if rNext == bNext:
                    continue
                # 조건 8) 서로 자리를 바꾸지 않을 것
                # 빨강이 파랑의 현재 칸으로 가고 && 파랑이 빨강의 현재 칸으로 가면 "자리 교환(swap)"이 됨
                if rNext == bPos and bNext == rPos:
                    continue

                # 여기까지 오면:
                # 1. 미로 밖/벽 아님
                # 2. 재방문 아님
                # 3. 겹침 없음
                # 4. 자리교환 없음
                # => 조건(2~8)을 만족하는 "유효한 한 턴 이동" 완성

                # 다음 상태의 방문 마스크 갱신
                # 도착한 수레는 고정 -> 더 이상 경로가 늘어나지 않으므로 mask 그대로
                # 도착 전이면 -> 다음 칸(rNext/bNext)을 방문 표시 켜기
                # ====================================================
                nrMask = rMask if r_done else (rMask | (1 << rNext))
                nbMask = bMask if b_done else (bMask | (1 << bNext))

                # 다음 상태 생성
                next_state = (rNext, bNext, nrMask, nbMask)

                # 이미 봤던 상태면 스킵 (중복 탐색 방지)
                if next_state in visited:
                    continue

                visited.add(next_state)

                # dist + 1 : 한 턴 진행했으므로 턴 수 증가
                q.append((rNext, bNext, nrMask, nbMask, dist + 1))

    # 큐가 비었다 = 가능한 모든 경우를 다 봤는데도 도착 못함
    # 문제에서 불가능이면 0을 반환하도록 되어 있는 케이스가 많아서 0으로 둠
    return 0
