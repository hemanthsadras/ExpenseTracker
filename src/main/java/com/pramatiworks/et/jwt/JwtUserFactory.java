package com.pramatiworks.et.jwt;

import com.pramatiworks.et.models.User;

public class JwtUserFactory
{
	public static JwtUser getJwtUser(User userAccount)
	{
		return new JwtUser(userAccount);
	}
}

