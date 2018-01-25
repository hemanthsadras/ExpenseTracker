package com.pramatiworks.et;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pramatiworks.et.models.User;
import com.pramatiworks.et.repositories.UserRepository;

@RestController
@RequestMapping("/account")
public class UserAccountController {
	
	private UserRepository userRepository;
	private List<String> defaultCategories;
	
	public UserAccountController(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.defaultCategories = new ArrayList<>();
		defaultCategories.add("Entertainment");
		defaultCategories.add("Medical");
		defaultCategories.add("Food");
		defaultCategories.add("Travel");
		defaultCategories.add("Misc");
	}
	
	@PostMapping
	public User createUserAccount(@RequestBody User user) {
		user.setCategories(defaultCategories);
		return this.userRepository.insert(user);
	}
	
	@GetMapping
	public User getUser() {
		User user = new User();
		user.setUserId("hemanthId");
		user.setEmailId("hemanthemailId");
		user.setPassword("mypassword");
		user.setUserName("hemanth");
	    List<String> categories = new ArrayList<>();
	    categories.add("Home");
	    categories.add("Entertainment");
	    user.setCategories(categories);
	    return user;
	}

}
