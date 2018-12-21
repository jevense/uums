package com.mvwchina.funcation.basicauth.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.mvwchina.funcation.basicauth.domain.Person;
import com.mvwchina.funcation.basicauth.repository.PersonRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/11/2 下午2:08
 */
@RestController
public class HelloController {

    @Resource
    private PersonRepository personRepository;

    @GetMapping("/hello")
    public Mono<String> hello() {   // 【改】返回类型为Mono<String>
        return Mono.just("Welcome to reactive world ~");     // 【改】使用Mono.just生成响应式数据
    }

    @ResponseBody
    @GetMapping("person")
    @JsonView(Person.WithoutPasswordView.class)
    public Mono<Person> handle1() {
        return personRepository.getPerson(1);
    }

//    @ExceptionHandler
//    public ResponseEntity<?> handle(IOException ex) {
//        return ResponseEntity.ok().build();
//    }
}
