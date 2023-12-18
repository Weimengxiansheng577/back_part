package com.azhai.config;

import com.azhai.interceptor.JwtTokenAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
    }



    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("azhai外卖")
                .version("1.0")
                .description("azhai外卖接口档案")
                .build();
    }
    /**
     * 配置不生效
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public Docket docket() {
        log.info("准备生成接口文档...");
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.azhai.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }


    /**
     * 设置静态资源映射了可以使用knife4j
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("设置静态资源映射");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    
    /**
     * @description: 统一信息管理 TODO 这里有个bug不知道怎么解决：统一信息管理会导致knife4j配置无效，感觉高版本knife4j有点不兼容
     * @author azhai
     * @date 2023/12/18 4:56
     * @version 1.0
     */
//    @Override
//    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        log.info("扩展消息转换器...");
//        //创建一个消息转换器对象
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        //设置对象转换器
//        converter.setObjectMapper(new JacksonObjectMapper());
//        //把转换器加到容器中
//        converters.add(0,converter);
//    }
}
