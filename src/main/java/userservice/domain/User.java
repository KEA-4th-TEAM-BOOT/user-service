package userservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import userservice.vo.BaseUserEnumVo;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Getter
@Builder
@Table(name = "BlogUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Email
    private String email;
    private String password;
    private String nickname;
    private String profileUrl;
    private String introduce;
    private String blogUrl;
    private int followingNum;
    private int followerNum;
    private int latestPostId;
    private String state;
    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime modifiedTime;
    public static User createUser(BaseUserEnumVo baseUserEnumVo){
        return User.builder()
                .name(baseUserEnumVo.name())
                .email(baseUserEnumVo.email())
                .password(baseUserEnumVo.password())
                .nickname(baseUserEnumVo.nickname())
                .profileUrl(baseUserEnumVo.profileUrl())
                .introduce(baseUserEnumVo.introduce())
                .blogUrl(baseUserEnumVo.blogUrl())
                .followingNum(0)
                .followerNum(0)
                .latestPostId(0)
                .state(baseUserEnumVo.state())
                .createdTime(baseUserEnumVo.createdTime())
                .modifiedTime(baseUserEnumVo.modifiedTime())
                .build();
    }
}

