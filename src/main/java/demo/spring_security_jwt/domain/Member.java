package demo.spring_security_jwt.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Member {

    private Long memberId;

    private String id;

    private String name;

    private String password;

    private List<String> roles = new ArrayList<>();

}
