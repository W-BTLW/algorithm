import heapq
# heapq는 기본적으로 가장 작은 값을 먼저 꺼냄

def solution(program):
    # 우선순위 10개
    answer = [0] * 11

    # 호출시간(index=1) 기준 정렬
    program.sort(key=lambda x: x[1])
    n = len(program)

    # 현재시간
    time = 0

    # 정렬된 리스트에서 heap에 넣지 않은 항목 인덱스
    idx = 0
    
    # (점수, 호출시간, 실행시간) 형태
    heap = []

    while idx < n or heap:
        # 현재시간 < TIME 이면 heap에 데이터 넣기
        while idx < n and program[idx][1] <= time:
            score, callTime, runTime = program[idx]

            # heapp : 첫번째 값부터 비교 / 사용시 낮은값이 먼저나옴(우선순위가 높은 경우가 낮은값)
            # score가 동일시 callTime 비교 / 후순위 runTime 비교
            heapq.heappush(heap, (score, callTime, runTime))

            idx += 1

        # heap이 빈값이면 현재시간에 도달한 프로그램 X
        if not heap:
            # 다음 프로그램의 호출시간으로 현재 시간을 이동
            # 현재 time이 0이고 첫 프로그램 호출시간이 5라면 time을 5로 이동
            time = program[idx][1]

            continue

        # 우선순위가 가장 높은 프로그램을 꺼냄
        score, call_time, run_time = heapq.heappop(heap)

        # 대기시간 = 실제 실행 시작 시간(time) - 호출 시간
        answer[score] += time - call_time

        # 프로그램 실행시 현재 시간 증가처리
        time += run_time

    answer[0] = time

    return answer
