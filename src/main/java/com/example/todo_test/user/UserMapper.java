package com.example.todo_test.user;


import com.example.todo_test.user.VO.UserVO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {
    void insertUser(UserVO userVO);
    UserVO selectUser(UserVO userVO);
    void deleteUser(Long userSeq);
    void updateUser(UserVO userVO);
}
