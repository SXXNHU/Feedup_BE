package ll25.feedup.host.controller;

import ll25.feedup.host.dto.HostSignupRequest;
import ll25.feedup.host.service.HostSignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signup")
public class HostSignupController {

    private final HostSignupService hostService;

    /** 자영업자 회원가입 **/
    @PostMapping("/host")
    public ResponseEntity<Void> HostSignup(@RequestBody HostSignupRequest request){
        hostService.signup(request);
        return ResponseEntity.ok().build();
    }
}