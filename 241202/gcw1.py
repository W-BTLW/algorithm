def solution(video_len, pos, op_start, op_end, commands):
    video_len_s = cal_time(video_len)
    op_start_s = cal_time(op_start)
    op_end_s = cal_time(op_end)

    after_com = cal_time(pos)

    # 오프닝이면 자동으로 SKIP 처리 
    after_com = skip_opening(after_com, op_start_s, op_end_s)

    for com in commands:

        # 반복시에도 오프닝안에서 돌아다니는거면 무조건 오프닝 끝으로 이동처리
        after_com = skip_opening(after_com, op_start_s, op_end_s)

        if com == 'prev':
            after_com = max(0, after_com - 10)
        elif com == 'next':
            after_com = min(video_len_s, after_com + 10)

        # prev 나 next 가 걸려도 똑같이 오프닝 체크해서 범위내에 있으면 범위로 집어넣기
        after_com = skip_opening(after_com, op_start_s, op_end_s)

    return cal_time_ans(after_com)


# 시간 문자열 형태 숫자로 바꾸어서 범위 계산
def cal_time(time_str):
    mm, ss = map(int, time_str.split(":"))
    return mm * 60 + ss

# 제출 전 숫자 -> 문자열 시간 형변환
def cal_time_ans(total_seconds):
    mm = total_seconds // 60
    ss = total_seconds % 60
    return f"{mm:02d}:{ss:02d}"

# 오프닝에 끼어있는 시간인지 체크
def skip_opening(cur, op_start_s, op_end_s):
    if op_start_s <= cur <= op_end_s:
        return op_end_s
    return cur
