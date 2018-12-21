package com.mvwchina.funcation.basicauth.repository;

import com.mvwchina.funcation.basicauth.domain.Person;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/11/2 下午2:45
 */
@Repository
public class PersonRepository {

    public Flux<Person> allPeople() {
        return Flux.just(new Person());
    }

    public Mono<Person> getPerson(int personId) {
        return Mono.just(new Person());
    }

    public Mono<Void> savePerson(Mono<Person> person) {
        return Mono.empty();
    }
}
