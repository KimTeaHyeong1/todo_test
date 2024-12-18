package com.example.todo_test.user.VO;

import lombok.Data;

@Data
public class UserVO {
    private Long userSeq;
    private String userName;
    private String userId;
    private String userPw;
    private String userGender;
    private Data userRegAt;
}
