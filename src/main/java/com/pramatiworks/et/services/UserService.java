package com.pramatiworks.et.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pramatiworks.et.jwt.JwtUserFactory;
import com.pramatiworks.et.models.User;
import com.pramatiworks.et.repositories.UserRepository;

;

@Service
public class UserService implements UserDetailsService
{
	@Autowired
	private UserRepository userAccountRepository;

	@Override
	public UserDetails loadUserByUsername ( String username ) throws UsernameNotFoundException
	{
		User userAccount = userAccountRepository.findByEmailId(username);
		if(userAccount != null)
		{
			return JwtUserFactory.getJwtUser(userAccount);
		}
		else
		{
			throw new UsernameNotFoundException("no user found with - " + username);
		}
		
	}
	
	public User createUserAccount(User userAccount)
	{
		//TODO : verify that no user already exists with same username
		return userAccountRepository.save(userAccount);
	}
	
	public User getUserAccountByUserName(String userName)
	{
		return userAccountRepository.findByEmailId(userName);
	}
	
	public List<User> getAllUserAccounts()
	{
		return userAccountRepository.findAll();
	}

}

