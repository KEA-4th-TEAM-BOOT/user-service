package userservice.global;

import java.util.Objects;

public class UserUpdateValue {
    public static <T> T updateValue(T currentValue, T newValue){
        if(Objects.isNull(newValue))
            return currentValue;
        else
            return newValue;
    }
}
