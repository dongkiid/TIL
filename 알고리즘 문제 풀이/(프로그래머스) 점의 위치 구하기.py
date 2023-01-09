def solution(dot):
   
    if (dot[0] > 0) & (dot[1] > 0):
        answer = 1
    
    elif (dot[0] < 0) & (dot[1] > 0):
        answer = 2
    
    elif (dot[0] < 0) & (dot[1] < 0):
        answer = 3
    else:
        answer = 4
        
    return answer