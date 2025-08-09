package ll25.feedup.controller;

import jakarta.validation.Valid;
import ll25.feedup.Host.dto.HostSignUpRequest;
import ll25.feedup.Host.service.HostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/signup")
public class SignupController {

    private final HostService hostService;

    @PostMapping("/host")
    public ResponseEntity<Void> signup(@Valid @RequestBody HostSignUpRequest request){
        hostService.signup(request);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/mate")

}
