//package userservice.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    private final TokenInterceptor tokenInterceptor;
//    public WebConfig(TokenInterceptor interceptor){
//        this.tokenInterceptor = interceptor;
//    }
//
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(tokenInterceptor);
//    }
//}
