package com.example.todo_test.board;

import com.example.todo_test.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;

    // 게시글 목록 페이지
    @GetMapping("/boardList")
    public String boardPage(Model model) {
        model.addAttribute("boardShow", boardService.boardShow());
        return "/board/boardList";
    }

    // 게시글 작성 처리
    @PostMapping("/board")
    public String insertBoard(String boardHd, String boardBd, MultipartFile imageFile, HttpSession session, Model model) throws IOException {
        String userId = (String) session.getAttribute("userId"); // 세션에서 userId를 가져옴

        // 제목과 내용 체크
        if (boardHd == null || boardHd.isEmpty()) {
            model.addAttribute("errorMsg", "제목을 입력하세요.");
            model.addAttribute("boardBd", boardBd);
            return "/board/board";
        }

        if (boardBd == null || boardBd.isEmpty()) {
            model.addAttribute("errorMsg", "내용을 입력하세요.");
            model.addAttribute("boardHd", boardHd);
            return "/board/board";
        }

        // 이미지 경로 처리
        String imagePath = null;
        if (imageFile != null && !imageFile.isEmpty()) {  // null 체크 후 파일 처리
            // 업로드할 디렉터리 설정 (static/images 폴더에 저장)
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images";  // 프로젝트 루트 디렉토리 내 static/images 폴더 경로
            File uploadDirectory = new File(uploadDir);

            // 디렉터리가 존재하지 않으면 생성
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }

            // 파일 이름 변경 및 저장
            String originalFileName = imageFile.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
            File dest = new File(uploadDirectory, fileName);
            imageFile.transferTo(dest); // 파일을 지정된 경로에 저장

            // 이미지 경로 (DB에 저장할 경로)
            imagePath = "/images/" + fileName;
        }

        // 게시글 작성 시 userId와 imagePath도 함께 저장
        boardService.insertBoard(boardHd, boardBd, userId, imagePath);

        return "redirect:/board/boardList";
    }

    @GetMapping("/boardDetail/{boardSeq}")
    public String boardDetail(@PathVariable Integer boardSeq, Model model, HttpSession session) {
        HashMap<String, Object> boardDetail = null;

        try {
            // 게시글 정보 조회
            boardDetail = boardService.boardNumber(boardSeq);

            // 게시글이 존재하지 않으면 예외 처리
            if (boardDetail == null) {
                model.addAttribute("errorMsg", "삭제된 게시글입니다.");
                return "/board/boardList";  // 게시글 목록으로 리다이렉트
            }

        } catch (Exception e) {
            model.addAttribute("errorMsg", "게시글을 찾을 수 없습니다.");
            return "/board/boardList";  // 게시글 목록으로 리다이렉트
        }

        // 이미지 경로 확인 (DB에서 가져온 imagePath 값)
        String imagePath = (String) boardDetail.get("IMAGE_PATH");
        if (imagePath != null && !imagePath.isEmpty()) {
            // 이미지 경로가 있다면 모델에 추가
            model.addAttribute("imagePath", imagePath);
        }

        // 게시글 상세 정보 모델에 추가
        model.addAttribute("boardDetail", boardDetail);

        // 조회수 증가
        boardService.incrementViews(boardSeq);

        // 세션에서 로그인한 사용자 ID를 가져옴
        String loggedInUserId = (String) session.getAttribute("userId");

        // 작성자가 맞는지 확인
        if (loggedInUserId != null && loggedInUserId.equals(boardDetail.get("USER_ID").toString())) {
            model.addAttribute("boardEdit", true);
        } else {
            model.addAttribute("boardEdit", false);
        }

        return "/board/boardDetail";
    }


    // 게시글 수정 페이지
    @GetMapping("/boardEdit/{boardSeq}")
    public String boardEdit(@PathVariable Integer boardSeq, Model model, HttpSession session) {
        String loggedInUserId = (String) session.getAttribute("userId");

        HashMap<String, Object> boardDetail = boardService.boardNumber(boardSeq);

        if (boardDetail != null && loggedInUserId != null
                && loggedInUserId.equals(boardDetail.get("USER_ID").toString())) {
            model.addAttribute("boardEdit", boardDetail);  // 수정 페이지로 이동
            return "/board/boardEdit";
        } else {
            return "redirect:/board/boardList";  // 권한이 없으면 목록으로 리다이렉트
        }
    }

    // 게시글 수정 처리
    @PostMapping("/updateBoard")
    public String updateBoard(Integer boardSeq, String boardHd, String boardBd, MultipartFile imageFile, HttpSession session) throws IOException {
        String loggedInUserId = (String) session.getAttribute("userId");

        // 게시글 정보 조회
        HashMap<String, Object> boardDetail = boardService.boardNumber(boardSeq);

        if (boardDetail != null && loggedInUserId.equals(boardDetail.get("USER_ID"))) {
            String imagePath = null;

            // 새로운 이미지가 업로드된 경우 처리
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images"; // 절대 경로로 변경
                File uploadDirectory = new File(uploadDir);

                // 디렉터리가 존재하지 않으면 생성
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }

                // 파일 이름을 UUID로 변경하여 저장
                String originalFileName = imageFile.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

                File dest = new File(uploadDirectory, fileName);
                imageFile.transferTo(dest); // 파일을 지정된 경로에 저장

                // 이미지 경로 (DB에 저장할 경로)
                imagePath = "/images/" + fileName;
            } else {
                // 이미지를 변경하지 않으면 기존 이미지 경로 사용
                imagePath = (String) boardDetail.get("IMAGE_PATH");
            }

            // 게시글 업데이트
            boardService.updateBoard(boardSeq, boardHd, boardBd, imagePath);
            return "redirect:/board/boardList"; // 수정 후 게시글 목록으로 리다이렉트
        } else {
            return "redirect:/board/boardList"; // 권한이 없으면 목록으로 리다이렉트
        }
    }


    // 게시글 삭제 처리
    @PostMapping("/deleteBoard/{boardSeq}")
    public String deleteBoard(@PathVariable Integer boardSeq, HttpSession session) {
        String loggedInUserId = (String) session.getAttribute("userId");

        HashMap<String, Object> boardDetail = boardService.boardNumber(boardSeq);

        if (boardDetail != null && loggedInUserId.equals(boardDetail.get("USER_ID"))) {
            // 이미지 파일 삭제 처리
            String imagePath = (String) boardDetail.get("IMAGE_PATH");
            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File("src/main/resources/static" + imagePath);
                if (file.exists()) {
                    file.delete(); // 파일 삭제
                }
            }

            boardService.deleteBoard(boardSeq);  // 게시글 삭제
            return "redirect:/board/boardList";  // 게시글 목록으로 리다이렉트
        } else {
            return "redirect:/board/boardList";  // 권한이 없으면 목록으로 리다이렉트
        }
    }
}
