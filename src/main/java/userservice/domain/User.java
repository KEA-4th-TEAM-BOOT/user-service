package userservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import userservice.dto.request.BaseUserRequestDto;
import userservice.global.BaseTimeEntity;
import userservice.vo.BaseUserEnumVo;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicInsert
@Entity
@Getter
@Builder
@Table(name = "Users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String name;

//    @Email    나중에 다시 주석 뺴주기
    @Column(length = 100)
    private String email;
    @Column(length = 100)
    private String password;
    @Column(length = 30)
    private String nickname;
    @Column(length = 200)
    private String profileUrl;
    @Column(length = 100)
    private String introduce;
    @Column(length = 30)
    private String blogUrl;
    @ColumnDefault("0")
    private Integer followingNum;
    @ColumnDefault("0")
    private Integer followerNum;
    @ColumnDefault("0")
    private Integer latestPostId;
    @Column(length = 10)
    private String state;

    public static User createUser(BaseUserRequestDto baseUserRequestDto){
        return User.builder()
                .name(baseUserRequestDto.name())
                .email(baseUserRequestDto.email())
                .password(baseUserRequestDto.password())
                .nickname(baseUserRequestDto.nickname())
                .blogUrl(baseUserRequestDto.blogUrl())
                .build();
    }
}

