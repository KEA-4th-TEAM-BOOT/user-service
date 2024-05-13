package userservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import userservice.config.UserSecurityConfig;
import userservice.dto.request.BaseUserRequestDto;
import userservice.dto.request.BaseUserUpdateRequestDto;
import userservice.global.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

import static userservice.global.UserUpdateValue.*;

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
    @Email    // 나중에 다시 주석 뺴주기
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
    private String userLink;
    @ColumnDefault("0")
    private Integer followingNum;
    @ColumnDefault("0")
    private Integer followerNum;
    @ColumnDefault("0")
    private Integer latestPostId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> categoryList = new ArrayList<>();

    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Follow> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "followerUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Follow> followerList = new ArrayList<>();
    public static User createUser(BaseUserRequestDto baseUserRequestDto){

        return User.builder()
                .name(baseUserRequestDto.name())
                .email(baseUserRequestDto.email())
                .password(new BCryptPasswordEncoder().encode(baseUserRequestDto.password()))
                .nickname(baseUserRequestDto.nickname())
                .userLink(baseUserRequestDto.blogUrl())
                .build();
    }

    public void updateUser(BaseUserUpdateRequestDto baseUserUpdateRequestDto){
        this.nickname = updateValue(this.nickname, baseUserUpdateRequestDto.nickname());
        this.profileUrl = updateValue(this.profileUrl, baseUserUpdateRequestDto.profileUrl());
        this.introduce = updateValue(this.introduce, baseUserUpdateRequestDto.introduce());
        this.followingNum = updateValue(this.followingNum, baseUserUpdateRequestDto.followingNum());
        this.followerNum = updateValue(this.followerNum, baseUserUpdateRequestDto.followerNum());
        this.latestPostId = updateValue(this.latestPostId, baseUserUpdateRequestDto.latestPostId());
    }

    public void changePassword(String encryptedPw){
        this.password = updateValue(this.password, encryptedPw);
    }

    public void addCategory(Category category){
        this.categoryList.add(category);
    }

    public void addFollowingUser(Follow follow){
        this.followingList.add(follow);
    }
    public void addFollowerUser(Follow follow){
        this.followerList.add(follow);
    }

}