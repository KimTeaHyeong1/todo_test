package com.example.todo_test;

import com.example.todo_test.todo.TodoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final TodoService todoService;
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        Long userSeq = (Long) session.getAttribute("userSeq");

        if(userSeq != null) {
            model.addAttribute("contentList", todoService.selectTodo(userSeq));

        }
        return "/home";
    }

    @GetMapping("/D-Day")
    public String dayPage() {
        return "D-Day";
    }
}
