package com.example.todo_test.todo;

import com.example.todo_test.todo.VO.TodoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoMapper todoMapper;

    // 특정 사용자의 할 일 목록을 조회
    public List<HashMap<String, Objects>> selectTodo(Long userSeq){
        return todoMapper.selectTodo(userSeq);
    }

    // 모든 사용자의 할 일 목록을 조회
    public List<HashMap<String, Objects>> selectAllTodos() {
        return todoMapper.selectAllTodos();  // 모든 사용자의 할 일을 조회
    }

    // 할 일 추가
    public TodoVO insertTodo(TodoVO todoVO){
        todoMapper.insertTodo(todoVO);
        return todoVO;
    }

    // 할 일 삭제
    public void deleteTodo(Long todoSeq){
        todoMapper.deleteTodo(todoSeq);
    }

    // 할 일 상태 업데이트 (완료/미완료)
    public void updateTodo(Long todoSeq){
        todoMapper.updateTodo(todoSeq);
    }
}
