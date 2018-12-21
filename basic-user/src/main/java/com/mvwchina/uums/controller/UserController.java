package com.mvwchina.uums.controller;

import com.mvwchina.uums.domain.user.User;
import com.mvwchina.uums.exception.MovedPermanentlyException;
import com.mvwchina.uums.exception.NoContentException;
import com.mvwchina.uums.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Name:
 * Description:
 * Copyright: Copyright (c) 2018 MVWCHINA All rights Reserved
 * Company: 北京医视时代科技发展有限公司
 *
 * @author lujiewen
 * @version 1.0
 * @since 2018/10/30 上午10:20
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation("获取用户列表")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Page<User> list(
            @ApiParam("页数") @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @ApiParam("每页记录数") @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
            @ApiParam("每页记录数") @RequestParam(value = "direction", defaultValue = "DESC", required = false) String direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), "id");
        return userService.getList(pageable);
    }

    @ApiOperation("根据ID获取用户信息")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public User get(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (user == null) throw new NoContentException();
        return user;
    }

    @ApiOperation("根据ID更新用户信息")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public User put(@PathVariable("id") Long id, @RequestBody User user) {
        return userService.put(id, user);
    }

    @ApiOperation("增加用户")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public User post(@RequestBody User user) {
        return userService.post(user);
    }

    @ApiOperation("根据ID修改用户信息")
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public User patch(@PathVariable("id") Long id, @RequestBody Map<String, Object> map) {
        if (userService.getById(id) == null) throw new MovedPermanentlyException();
        return userService.patch(id, map);
    }

    @ApiOperation("根据ID删除用户")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

}
