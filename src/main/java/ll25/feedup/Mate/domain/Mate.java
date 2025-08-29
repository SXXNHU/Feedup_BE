package ll25.feedup.Mate.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mates")
@Getter @Setter
public class Mate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "phone")
    private String phone;
}