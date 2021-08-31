package com.neueda.atm.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private final ServletContext servletContext;
    private final String baseHost;
    private final String basePath;

    @Autowired
    public SwaggerConfig(ServletContext servletContext,
                         @Value("${swagger.baseHost}") final String baseHost,
                         @Value("${swagger.basePath}") final String basePath) {
        this.servletContext = servletContext;
        this.baseHost = baseHost;
        this.basePath = basePath;
    }

    @Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, getGlobalResponseMessages())
                .globalResponseMessage(RequestMethod.GET, getGlobalResponseMessages())
                .globalResponseMessage(RequestMethod.PUT, getGlobalResponseMessages())
                .globalResponseMessage(RequestMethod.DELETE, getGlobalResponseMessages());

        if (baseHost != null & basePath!= null){
            // The following replaces the default swagger configs, host and path.
            // These are used to access the swagger ui without the nodeport.
            docket.pathProvider(new RelativePathProvider(servletContext) {
                @Override
                public String getApplicationBasePath() {
                    return basePath;
                }
            });
            docket.host(baseHost);
        }
        return docket;
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("ATM Service REST API Documentation")
                .version("1.0")
                .build();
    }

    private List<ResponseMessage> getGlobalResponseMessages() {
        return Arrays.asList(
                new ResponseMessageBuilder()
                        .code(HttpStatus.OK.value())
                        .message(HttpStatus.OK.getReasonPhrase())
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.CREATED.value())
                        .message(HttpStatus.CREATED.getReasonPhrase())
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .build());
    }
}
