package ll25.feedup.Host.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hosts")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Host {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    //자영업자 닉네임은 개인 상호명으로 통일
    private String nickname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private PlaceCategory category;

    @Column(name = "thumbnail")
    private String thumbnail;
}

