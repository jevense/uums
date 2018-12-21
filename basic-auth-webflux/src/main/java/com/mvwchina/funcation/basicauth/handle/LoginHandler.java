package com.mvwchina.funcation.basicauth.handle;

import com.mvwchina.funcation.basicauth.domain.Person;
import com.mvwchina.funcation.basicauth.repository.LoginRepository;
import com.mvwchina.util.URLDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/12/18 下午2:50
 */
@Component
public class LoginHandler {

    @Value("classpath:/templates/home.ftl")
    private Resource homeHtml;

    @Value("classpath:/public/index.html")
    private Resource indexPage;

    @Value("classpath:/public/login.html")
    private Resource loginPage;

    private final LoginRepository loginRepository;

    @Autowired
    public LoginHandler(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }


    public Mono<ServerResponse> login(ServerRequest request) {

        request.formData().subscribe(System.out::println, Throwable::printStackTrace);

//        Mono<MultiValueMap<String, String>> map = request.body(BodyExtractors.toFormData());

//        System.out.println(map.block());

        System.out.println(request.queryParams());

//        loginRepository.findAll(request.formData());

        Flux.create(cityFluxSink -> {
            loginRepository.findAll().forEach(cityFluxSink::next);
            cityFluxSink.complete();
        });


//        Objects.requireNonNull(request.formData().block()).forEach((k, v) -> {
//            System.out.println(k);
//        });

//        MultiValueMap<String, String> map = request.body(BodyExtractors.toFormData()).block();
//        map.forEach((k, v) -> {
//            System.out.println(k + v);
//        });
//        map.subscribe(x -> System.out.println(x.toSingleValueMap()));
//
        Optional<String> redirectUri = request.queryParam("redirect_uri");
        if (redirectUri.isPresent()) {
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(URLDecoder.decode(redirectUri.get())).build();
            return ServerResponse.temporaryRedirect(uriComponents.toUri()).build();
        }
        return ok().contentType(MediaType.valueOf("text/html;charset=utf-8")).syncBody(homeHtml);
    }

    public Mono<ServerResponse> token(ServerRequest request) {
        Mono<Person> personMono = request.bodyToMono(Person.class);

        return ok().body(personMono, Person.class);
    }
}

