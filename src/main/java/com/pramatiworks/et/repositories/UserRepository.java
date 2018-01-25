package com.pramatiworks.et.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pramatiworks.et.models.User;

public interface UserRepository extends MongoRepository<User,String> {
		User findByEmailId(String emailId);
}
