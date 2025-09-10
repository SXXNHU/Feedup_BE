package ll25.feedup.mate.controller;

import ll25.feedup.mate.dto.MateSignupRequest;
import ll25.feedup.mate.service.MateSignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signup")
public class MateSignupController {

    private final MateSignupService mateService;

    /** 대학생 회원가입 **/
    @PostMapping("/mate")
    public ResponseEntity<Void> MateSignup(@RequestBody MateSignupRequest request){
        mateService.signup(request);
        return ResponseEntity.ok().build();
    }
}