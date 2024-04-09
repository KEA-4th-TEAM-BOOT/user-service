package userservice.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import userservice.dto.request.BaseUserRequestDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Builder
public record BaseUserEnumVo(

        String name,
        String email,
        String password,
        String nickname,
        String profileUrl,
        String introduce,
        String blogUrl,
        Integer followingNum,
        Integer followerNum,
        Integer latestPostId,
        String state
) {
    public static BaseUserEnumVo of(BaseUserRequestDto baseUserRequestDto,
                                    BaseUserEnumVo baseUserEnumVo){
        return BaseUserEnumVo.builder()
                .name(baseUserRequestDto.name())
                .email(baseUserRequestDto.email())
                .password(baseUserRequestDto.password())
                .nickname(baseUserRequestDto.nickname())
                .profileUrl(Optional.ofNullable(baseUserEnumVo.profileUrl).orElse(""))
                .introduce(Optional.ofNullable(baseUserEnumVo.introduce).orElse(""))
                .blogUrl(baseUserRequestDto.blogUrl())
                .followingNum(baseUserEnumVo.followingNum)
                .followerNum(baseUserEnumVo.followerNum)
                .latestPostId(baseUserEnumVo.latestPostId)
                .state(baseUserEnumVo.state)
                .build();
    }
}



