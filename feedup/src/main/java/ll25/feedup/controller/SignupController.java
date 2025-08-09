package ll25.feedup.controller;

import ll25.feedup.Host.dto.HostSignUpRequest;
import ll25.feedup.Host.service.HostService;
import ll25.feedup.Mate.dto.MateSignUpRequest;
import ll25.feedup.Mate.service.MateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {

    private final HostService hostService;
    private final MateService mateService;

    @PostMapping("/host")
    public ResponseEntity<Void> HostSignup(@RequestBody HostSignUpRequest request){
        hostService.signup(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mate")
    public ResponseEntity<Void> MateSignup(@RequestBody MateSignUpRequest request){
        mateService.signup(request);
        return ResponseEntity.ok().build();
    }
}
