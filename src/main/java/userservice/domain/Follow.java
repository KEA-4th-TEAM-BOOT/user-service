package userservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import userservice.global.BaseTimeEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicInsert
@Entity
@Getter
@Builder
@Table(name = "Follow")
public class Follow extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @ManyToOne
    @JoinColumn(name = "following_id", referencedColumnName = "id")
    private User followingUser;

    @ManyToOne
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    private User followerUser;

    public static void createFollow(User followingUser, User followerUser) {
        Follow follow = Follow.builder()
                .followingUser(followingUser)
                .followerUser(followerUser)
                .build();

        followingUser.addFollowingUser(follow);
        followerUser.addFollowerUser(follow);
    }
}
