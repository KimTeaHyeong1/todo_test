package com.example.todo_test.todo;

import com.example.todo_test.todo.VO.TodoVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {
    private final TodoService todoService;

    // 할 일 추가
    @PostMapping("/add")
    public String insertTodo(TodoVO todoVO, HttpSession session) {
        todoVO.setUserSeq((Long) session.getAttribute("userSeq"));
        TodoVO userSeq = todoService.insertTodo(todoVO);
        return "redirect:/";
    }

    // 할 일 삭제
    @PostMapping("/delete")
    public String deleteTodo(Long todoSeq){
        todoService.deleteTodo(todoSeq);
        return "redirect:/";
    }

    // 할 일 상태 업데이트
    @PostMapping("/update")
    public String updateTodo(Long todoSeq){
        todoService.updateTodo(todoSeq);
        return "redirect:/";
    }

    // 홈 화면을 불러올 때, 모든 사용자의 할 일 목록을 전달
    @GetMapping("/talkList")
    public String talkListPage(Model model) {
        // 모든 사용자의 할 일 목록을 가져와서 모델에 추가
        model.addAttribute("contentList", todoService.selectAllTodos());
        return "/talkList";  // home.html로 전달
    }


}
