def solution(video_len, pos, op_start, op_end, commands):
   
    video = int(video_len[0:2]) * 60 + int(video_len[3:])
    pos = int(pos[0:2]) * 60 + int(pos[3:])
    op_s = int(op_start[0:2]) * 60 + int(op_start[3:])
    op_e = int(op_end[0:2]) * 60 + int(op_end[3:])

    if  op_s <= pos <= op_e:
        pos = op_e

    for cmd in commands:
        if cmd == 'next':
            pos = min(pos+10, video)
        elif cmd == 'prev':
            pos = max(pos-10, 0)

        if  op_s <= pos <= op_e:
            pos = op_e

    minute = str(pos//60).zfill(2)
    second = str(pos - int(minute)*60).zfill(2)

    return  minute + ':' + second