package com.lcc.web.controller;

import com.lcc.web.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: lcc
 * @Date: 2018-05-21
 **/
@RestController
@RequestMapping(value = "/user")
public class UserController {
	@GetMapping("/list")
	List<User> listUser(@RequestParam(required = false, value = "username") String username) {
		List<User> list = new ArrayList<>();
		list.add(new User("xiaoming"));
		list.add(new User("xiaohong"));
		list.add(new User("xiaolan"));
		return list;
	}

	@GetMapping("/me")
	Object getCurrentUser() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
}
