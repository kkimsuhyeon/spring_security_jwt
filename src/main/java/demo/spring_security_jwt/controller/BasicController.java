package demo.spring_security_jwt.controller;

import demo.spring_security_jwt.config.jwt.TokenProvider;
import demo.spring_security_jwt.domain.Member;
import demo.spring_security_jwt.dto.RequestLogin;
import demo.spring_security_jwt.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BasicController {

    TokenProvider provider;
    MemberRepository memberRepository;


    @Autowired
    public BasicController(TokenProvider provider, MemberRepository memberRepository) {
        this.provider = provider;
        this.memberRepository = memberRepository;
    }

    @PostMapping(value = "/all")
    public ResponseEntity<?> allAllow() {
        return new ResponseEntity<>("all", HttpStatus.OK);
    }

    @PostMapping(value = "/not")
    public ResponseEntity<?> notAllow() {
        return new ResponseEntity<>("not", HttpStatus.OK);
    }

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> login(@ModelAttribute RequestLogin loginData) {

        Optional<Member> optionalMember = memberRepository.findById(loginData.getId());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String token = provider.createToken(member.getMemberId(), member.getId(), member.getRoles(), TokenProvider.ACCESS_TOKEN_EXPIRE_TIME);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping(value = "/token")
    public ResponseEntity<?> token() {
        return new ResponseEntity<>("token", HttpStatus.OK);
    }

    @PostMapping(value = "/user")
    public ResponseEntity<?> userAllow() {
        return new ResponseEntity<>("user", HttpStatus.OK);
    }

    @PostMapping(value = "/admin")
    public ResponseEntity<?> adminAllow() {
        return new ResponseEntity<>("admin", HttpStatus.OK);
    }

}
