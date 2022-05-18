package com.tool.ProjectTool.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tool.ProjectTool.entity.Users;
import com.tool.ProjectTool.exception.UserAlreadyExistException;
import com.tool.ProjectTool.exception.UserNotFoundException;
import com.tool.ProjectTool.model.request.UpdateUserRequest;
import com.tool.ProjectTool.model.request.UserRequest;
import com.tool.ProjectTool.model.response.UserResponse;
import com.tool.ProjectTool.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passEncode;

	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	public Users createUser(UserRequest user) {

		try {
			String email = user.getUsername();
			String ids = randomString(20);
			String id = null;

			Users existUser = userRepo.findByUsername(email);
			Users checkIds = userRepo.findByUserId(ids);

			if (existUser != null) {
				throw new UserAlreadyExistException("User with '" + email + "' already exist.");
			}

			Users users = new Users();

			users.setUsername(email);
			users.setEmail(email);
			users.setName(user.getName());

			if (checkIds != null) {
				id = randomString(20);
			} else {
				id = ids;
			}
			users.setUserId(id);
			users.setRoleName(user.getRoleName());
			users.setPassword(passEncode.encode(user.getPassword()));
			users.setStatus(0);

			return userRepo.save(users);
		} catch (Exception e) {
			throw new UserAlreadyExistException("User with '" + user.getUsername() + "' already exist.");
		}
	}

	public String updateUser(UpdateUserRequest request) {

		try {

			Users user = userRepo.findById(request.getUserId()).get();

			if (user != null) {
				user.setName(request.getName());
				user.setPhone(request.getPhone());
				user.setEmail(request.getEmail());

				userRepo.save(user);

				return "User updated successfully";
			}

		} catch (Exception e) {
			throw new UserNotFoundException("User not found");
		}

		return null;
	}

	public UserResponse getUserDetails(String email) {

		try {

			Users user = userRepo.findByUsername(email);
			UserResponse uRes = new UserResponse();

			if (user != null) {

				uRes.setName(user.getName());
				uRes.setUserEmail(user.getEmail());
				uRes.setUserPic(user.getImageUrl());
				uRes.setUserId(user.getUserId());
				uRes.setUserRole(user.getRoleName());

				return uRes;
			}
			throw new UserNotFoundException("User not found");
		} catch (Exception e) {
			throw new UserNotFoundException("User not found");
		}
	}
}
