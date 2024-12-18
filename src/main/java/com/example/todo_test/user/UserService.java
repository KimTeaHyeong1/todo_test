package com.example.todo_test.user;


import com.example.todo_test.user.VO.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    UserVO login(UserVO userVO) {// UserVO 타입의 파라미터 userVO를 받는 UserVO 타입의 login 메서드.
        UserVO result = userMapper.selectUser(userVO);
        // UserVO 타입의 result 는 userMapper.selectUserByUserIdAndUserPw(userVO) 메서드의 결과 선언.

        if(Objects.isNull(result)) {// result 가 없음(null)일경우
            return null; //null 을 return
        }
        return result; //아니면 result 를 리턴
    }

    void register(UserVO userVO) {
        userMapper.insertUser(userVO);

    }

    void updateUser(UserVO userVO) {
        userMapper.updateUser(userVO);
    }

    void deleteUser(Long userSeq) {
        userMapper.deleteUser(userSeq);
    }


}
