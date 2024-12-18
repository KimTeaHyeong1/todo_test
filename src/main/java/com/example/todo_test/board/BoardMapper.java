package com.example.todo_test.board;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface BoardMapper {

    void insertBoard(String boardHd, String boardBd, String userId, String imagePath);

    HashMap<String, Object> boardNumber(Integer boardSeq);

    List<HashMap<String, Object>> boardShow();

    // 게시글 수정 쿼리 (이미지 경로 포함)
    void updateBoard(Integer boardSeq, String boardHd, String boardBd, String imagePath);

    void deleteBoard(Integer boardSeq);

    void incrementViews(Integer boardSeq);
}
