def solution(s):
    str = s.lower() #입력값을 전부 소문자로 변환
    arr = list(str)
    plen = arr.count('p')
    ylen = arr.count('y')
    if plen == ylen:
        return True
    else:
        return False 