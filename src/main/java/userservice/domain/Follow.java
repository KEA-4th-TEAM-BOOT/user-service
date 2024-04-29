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

    //
    @ManyToOne
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    private User followerUser;

    @ManyToOne
    @JoinColumn(name = "followed_id", referencedColumnName = "id")
    private User followedUser;

    public static Follow createFollow(User followerUser, User followedUser){
        Follow follow = Follow.builder()
                .followerUser(followerUser)
                .followedUser(followedUser)
                .build();

        followerUser.addFollowerUser(follow);
        followedUser.addFollowedUser(follow);
        return follow;
    }
}
