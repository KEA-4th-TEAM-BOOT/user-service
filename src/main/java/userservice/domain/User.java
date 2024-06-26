package userservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import userservice.dto.request.BaseUserRequestDto;
import userservice.dto.request.BaseUserUpdateRequestDto;
import userservice.global.BaseTimeEntity;

import java.util.ArrayList;
import java.util.Collections;
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

    @Email
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

    @ColumnDefault("0")
    private Integer postCnt;

    @Column(length = 200)
    private String voiceModelUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> categoryList = Collections.synchronizedList(new ArrayList<>());

    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Follow> followingList = Collections.synchronizedList(new ArrayList<>());

    @OneToMany(mappedBy = "followerUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Follow> followerList = Collections.synchronizedList(new ArrayList<>());

    public static User createUser(BaseUserRequestDto baseUserRequestDto, String encryptedPw) {

        return User.builder()
                .name(baseUserRequestDto.name())
                .email(baseUserRequestDto.email())
                .password(encryptedPw)
                .nickname(baseUserRequestDto.nickname())
                .introduce(baseUserRequestDto.introduce())
                .profileUrl(baseUserRequestDto.profileUrl())
                .userLink(baseUserRequestDto.userLink())
                .build();
    }

    public void updateUser(BaseUserUpdateRequestDto baseUserUpdateRequestDto) {
        this.nickname = updateValue(this.nickname, baseUserUpdateRequestDto.nickname());
        this.profileUrl = updateValue(this.profileUrl, baseUserUpdateRequestDto.profileUrl());
        this.introduce = updateValue(this.introduce, baseUserUpdateRequestDto.introduce());
        this.followingNum = updateValue(this.followingNum, baseUserUpdateRequestDto.followingNum());
        this.followerNum = updateValue(this.followerNum, baseUserUpdateRequestDto.followerNum());
        this.latestPostId = updateValue(this.latestPostId, baseUserUpdateRequestDto.latestPostId());
        this.postCnt = updateValue(this.postCnt, baseUserUpdateRequestDto.postCnt());
        this.voiceModelUrl = updateValue(this.voiceModelUrl, baseUserUpdateRequestDto.voiceModelUrl());
    }

    public void changePassword(String encryptedPw) {
        this.password = encryptedPw;
    }

    public void addCategory(Category category) {
        this.categoryList.add(category);
    }

    public void addFollowingUser(Follow follow) {
        this.followingList.add(follow);
        this.followingNum = this.followingList.size();
    }

    public void addFollowerUser(Follow follow) {
        this.followerList.add(follow);
        this.followerNum = this.followerList.size();
    }
}