def solution(menu, order, k):
    # complete_time 기준 음료 못받은사람 위치
    left = 0
    # 앞사람 음료 완성 시간
    last_finish_time = 0
    # 카페에 동시에 존재했던 최대 손님 수
    answer = 0
     # 아직 카페에 남아 있는 손님들의 음료 완성 시간
    complete_time = []


    for i in range(len(order)):
        # i번째 손님의 도착 시간
        arrival_time = i * k

        # 이전 음료 제조가 끝났고 손님도 도착했다면 제조 시작
        start_time = max(arrival_time, last_finish_time)
        # 현재 손님의 음료가 완성되는 시간
        last_finish_time = start_time + menu[order[i]]
        # 종료 시간 저장
        complete_time.append(last_finish_time)

        # 현재 손님 도착 시간 이전 또는 같은 시간에 음료를 받은 손님들은 이미 카페를 나감
        while (left < len(complete_time) and complete_time[left] <= arrival_time):
            left += 1
        # 현재까지 들어온 손님 수 - 이미 나간 손님 수
        current_person = i - left + 1

        answer = max(answer, current_person)

    return answer
