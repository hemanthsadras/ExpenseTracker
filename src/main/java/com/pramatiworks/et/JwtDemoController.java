package com.pramatiworks.et;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtDemoController {
	
	@GetMapping("/jwtDemo/greetings")
	public String sayHello() {
		return "Welcome to Securing REST APIs with JWT session";
	}

}
