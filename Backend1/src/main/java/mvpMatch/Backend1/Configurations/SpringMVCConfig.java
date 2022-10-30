package mvpMatch.Backend1.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMVCConfig implements WebMvcConfigurer {
    @Autowired
    Authentication authentication;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authentication);
    }
}
