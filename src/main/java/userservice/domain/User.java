package userservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import userservice.dto.request.BaseUserRequestDto;
import userservice.dto.response.BaseUserResponseDto;
import userservice.global.BaseTimeEntity;
import userservice.vo.BaseUserEnumVo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Getter
    @Column(length = 10)

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> categoryList = new ArrayList<>();

    @OneToMany(mappedBy = "followerUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Follow> followerList = new ArrayList<>();

    @OneToMany(mappedBy = "followedUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Follow> followedList = new ArrayList<>();
    public static User createUser(BaseUserRequestDto baseUserRequestDto){
        return User.builder()
                .name(baseUserRequestDto.name())
                .email(baseUserRequestDto.email())
                .password(baseUserRequestDto.password())
                .nickname(baseUserRequestDto.nickname())
                .blogUrl(baseUserRequestDto.blogUrl())
                .build();
    }

    public void updateUser(BaseUserEnumVo baseUserEnumVo){
        this.nickname = baseUserEnumVo.nickname();
        this.profileUrl = baseUserEnumVo.profileUrl();
        this.introduce = baseUserEnumVo.introduce();
        this.followingNum = baseUserEnumVo.followingNum();
        this.followerNum = baseUserEnumVo.followerNum();
        this.latestPostId = baseUserEnumVo.latestPostId();
    }

    public void addCategory(Category category){
        this.categoryList.add(category);
    }

    public void addFollowerUser(Follow follow){
        this.followerList.add(follow);
    }
    public void addFollowedUser(Follow follow){
        this.followedList.add(follow);
    }

}

// // follow schema
// id / user_id /