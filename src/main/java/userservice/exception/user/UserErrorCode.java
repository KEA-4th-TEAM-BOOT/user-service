//package userservice.exception.user;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import org.springframework.http.HttpStatus;
//import userservice.global.BaseErrorCode;
//import userservice.global.ErrorReason;
//
//import static org.springframework.http.HttpStatus.*;
//
//@AllArgsConstructor
//@Getter
//public class UserErrorCode implements BaseErrorCode {
//
//    // 유저가 없을 때
//    // 이메일이 중복될 때
//    // url이 중복될 때
//    // 요청이 잘못됐을 때
//
//    USER_NOT_FOUND(NOT_FOUND, "USER_404_1", "사용자를 찾을 수 없습니다.")
//
//
//    private HttpStatus status;
//    private String code;
//    private String reason;
//
//    @Override
//    public ErrorReason getErrorReason() {
//        return ErrorReason.of(status.value(), code, reason);
//    }
//}
