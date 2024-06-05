package userservice.exception.follow;

public class FollowRelationshipNotFoundException extends RuntimeException {
    public FollowRelationshipNotFoundException(String message) {
        super(message);
    }
}
