package com.pramatiworks.et;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pramatiworks.et.models.CategoryRequest;
import com.pramatiworks.et.models.User;
import com.pramatiworks.et.repositories.UserRepository;

@RestController
@RequestMapping("/category")
public class CategoryController {
	
	private UserRepository userRepository;
	
	public CategoryController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@PostMapping
	public List<String> addCategory(@RequestBody CategoryRequest categoryRequest) {
		
		String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmailId(emailId);
		user.getCategories().add(categoryRequest.getCategoryName());
		userRepository.save(user);
		return user.getCategories();
	}
	
	@GetMapping
	public List<String> getCategories() {
		String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmailId(emailId);
		return user.getCategories();
	}

}
