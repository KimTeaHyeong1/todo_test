package com.example.todo_test.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;

    // 게시글 작성 처리 (이미지 경로 포함)
    void insertBoard(String boardHd, String boardBd, String userId, String imagePath){
        boardMapper.insertBoard(boardHd, boardBd, userId, imagePath);
    }

    HashMap<String, Object> boardNumber(Integer boardSeq) {
        return boardMapper.boardNumber(boardSeq);
    }

    List<HashMap<String, Object>> boardShow(){
        return boardMapper.boardShow();
    }

    // 게시글 수정 처리 (이미지 경로 포함)
    void updateBoard(Integer boardSeq, String boardHd, String boardBd, String imagePath) {
        boardMapper.updateBoard(boardSeq, boardHd, boardBd, imagePath);
    }

    void deleteBoard(Integer boardSeq) {
        boardMapper.deleteBoard(boardSeq);
    }

    // 게시글 조회수 증가
    public void incrementViews(Integer boardSeq) {
        boardMapper.incrementViews(boardSeq);
    }
}
