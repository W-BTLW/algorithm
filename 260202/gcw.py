# 빠른 바늘이 느린 바늘을 한바퀴 따라잡은 순간 & 겹치는 순간은 항상 같은 시간 간격(주기)으로 반복
# 시침 & 분침 겹침: 43200 / 11 초
# 분침 & 초침 겹침: 3600  / 59 초
# 시침 & 초침 겹침: 43200 / 719 초
# 하루동안 발생하는 이 모든 알람이 울리는 조건을 미리 생성 처리
# 시작 ~ 끝 시간 구간 안에 들어오는 것만 COUNT 처리

from fractions import Fraction
# float 계산 시 유리수로 정확히 비교 가능하도록 함수 사용

def solution(h1, m1, s1, h2, m2, s2):

    all_day_sec = 24 * 3600  

    # 시침 <-> 분침 차이 주기
    hour_min_diff = Fraction(43200, 11)  
    # 분침 <-> 초침 차이 주기 
    min_sec_diff = Fraction(3600, 59) 
    # 시침 <-> 초침 차이 주기
    hour_sec_diff = Fraction(43200, 719)  

    def to_sec(h, m, s):
        return h * 3600 + m * 60 + s

    def cal_all_day():
        # 시/분/초 가 전부 겹치는 건수 제외를 위해 set사용
        day_set = set()
        for period in (min_sec_diff, hour_sec_diff):
            k = 0
            # 00:00:00 이 첫 겹치는 구간이므로 0 부터 시작 처리
            while True:
                t = period * k
                if t >= all_day_sec:
                    break
                day_set.add(t)
                k += 1
        return day_set

    all_day_set = cal_all_day()

    start_sec = to_sec(h1, m1, s1)
    end_sec = to_sec(h2, m2, s2)

    # 자정 넘는 경우 구간을 2개로 분할
    # [EX] 시작: 23:55:00 -> 86100 / 종료: 00:05:00 -> 300
    # start <= t <= end 조건성립이 불가하기 때문에 구간 분할 후 로직 처리

    if start_sec <= end_sec:
        ranges = [(start_sec, end_sec)]
    else:
        ranges = [(start_sec, all_day_sec - 1), (0, end_sec)]

    def in_range(t):
        for a, b in ranges:
            if Fraction(a, 1) <= t <= Fraction(b, 1):
                return True
        return False

    alarm_count = 0
    for t in all_day_set:
        if in_range(t):
            alarm_count += 1

    return alarm_count
