def solution(diffs, times, limit):

    len_diff = len(diffs)

    # 난이도 기준으로 세팅해두고 max_try로 정답수렴 
    min_try, max_try = 1, max(diffs)
    ans = max_try
    
    while min_try <= max_try:
        mean_try = (min_try + max_try) // 2
        # 중간으로 찍고 가능여부 boolean으로 return 받아 확인 
        if can_clear(mean_try, diffs, times, limit, len_diff):
            ans = mean_try
            max_try = mean_try - 1
        else:
            min_try = mean_try + 1

    return ans


def can_clear(level, diffs, times, limit, len_diff):
    # 전체시간 / 이전에 쓴 시간
    total = 0
    prev_time = 0 

    for i in range(len_diff):
        # 현재 퍼즐 난이도
        diff = diffs[i]
        # 현재 퍼즐 푸는 시간
        cur_time = times[i]

        # 퍼즐에서 몇번 틀릴지 계산
        diff_lev = diff - level

        # 난이도 > level
        if diff_lev > 0:
            # diff > level -> diff - level 횟수만큼 실수한다. 
            # 현재 퍼즐 → time_cur
            # 이전 퍼즐 다시 풀어야함 → time_prev
            # 마지막에 정답 시도 -> time_cur
            total += diff_lev * (cur_time + prev_time) + cur_time
        else:
            # diff <= level -> 소요 시간 = time_cur
            total += cur_time

        # 시간초과하면 의미없음
        if total > limit:
            return False

        # 다음퍼즐 계산 용도 이전시간 저장
        prev_time = cur_time

    return True

