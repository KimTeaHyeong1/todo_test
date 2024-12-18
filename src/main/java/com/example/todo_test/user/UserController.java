package com.example.todo_test.user;

import com.example.todo_test.user.VO.UserVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/userController")
public class UserController {
    private final UserService userService;
    @GetMapping("/login")//로그인주소로 이동
    public String loginPage() {// String 타입의 loginPage 매서드
        return "login";//폴더경로
    }

    @PostMapping("/login")//로그인 동작
    public String login(UserVO userVO, Model model, HttpSession session) {
        //UserVO userVO, Model model, HttpSession session 타입의 파마리터를 받는 String 타입의 login 매서드.
        UserVO result = userService.login(userVO);
        // UserVO 타입의 result 는 userService.login(userVO) 메서드의 결과 선언.
        if(result == null) {//userSeq, userId, userPw 값이 없음(null)일때
            model.addAttribute("errorMsg", "아이디&비밀번호를 확인하세요.");
            return "/login"; //로그인 html 로 이동
        }
        session.setAttribute("userSeq", result.getUserSeq());//세션에 키는 useSeq 값은 result.getUserSeq
        session.setAttribute("userId", result.getUserId());//세션에 키는 useSeq 값은 result.getUserId
        return "redirect:/"; //메인화면 주소로 다시 바로 이동
    }

    @PostMapping("/logOut")//로그아웃
    public String logOut(HttpSession session) {//HttpSession 타입의 session 파라미터를 받는 String 타입의 logout 매서드.
        session.removeAttribute("userSeq");//세션에 키는 useSeq 제거
        return "redirect:/";//메인화면 주소로 다시 바로 이동
    }

    @GetMapping("/signup")// 회원가입 주소로 이동
    public String signUpPage() {//String 타입의 signUpPage 매서드.
        return "/signup";//회원가입 html 로 이동
    }

    @PostMapping("/signup")
    public String signup(UserVO userVO, Model model) {
        try {
            // 입력값 검증 (길이 체크)
            if (userVO.getUserName().length() > 10) {
                model.addAttribute("errorMsg", "이름은 10자 이내로 입력해주세요.");
                return "/signup";
            }
            if (userVO.getUserId().length() > 20) {
                model.addAttribute("errorMsg", "아이디는 20자 이내로 입력해주세요.");
                return "/signup";
            }
            if (userVO.getUserPw().length() > 20) {
                model.addAttribute("errorMsg", "비밀번호는 20자 이내로 입력해주세요.");
                return "/signup";
            }
            if (userVO.getUserGender().length() > 10) {
                model.addAttribute("errorMsg", "성별은 10자 이내로 입력해주세요.");
                return "/signup";
            }
            userService.register(userVO);
            return "redirect:/userController/login"; // 회원가입 성공 후 로그인 페이지로 리다이렉트
        } catch (Exception e) {
            // 예외 발생 시 오류 메시지 추가
            model.addAttribute("errorMsg", "회원가입 중 오류가 발생했습니다. 다시 시도해 주세요.");
            return "/signup";
        }
    }

    @GetMapping("/update")
    public String updatePage() {
        return "/update";
    }

    @PostMapping("/update")
    public String updateUser(UserVO userVO) {
        userService.updateUser(userVO);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long userSeq, HttpSession session) {
        userService.deleteUser(userSeq);
        session.removeAttribute("userSeq");
        return "redirect:/";
    }


}

