package com.tool.ProjectTool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool.ProjectTool.entity.Users;
import com.tool.ProjectTool.exception.UserAlreadyExistException;
import com.tool.ProjectTool.exception.UserNotFoundException;
import com.tool.ProjectTool.model.request.UpdateUserRequest;
import com.tool.ProjectTool.model.response.UserResponse;
import com.tool.ProjectTool.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	public Users createUser(Users user) {

		try {
			String email = user.getEmail();

			Users existUser = userRepo.findByUsername(email);
			if (existUser != null) {
				throw new UserAlreadyExistException("User with '" + email + "' already exist.");
			}

			user.setUsername(email);

			return userRepo.save(user);
		} catch (Exception e) {
			throw new UserAlreadyExistException("User with '" + user.getEmail() + "' already exist.");
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

	public UserResponse getUserDetails(long userId) {

		try {

			Users user = userRepo.findById(userId).get();
			UserResponse uRes = new UserResponse();

			if (user != null) {
				
				uRes.setUserName(user.getName());
				uRes.setUserEmail(user.getEmail());
				uRes.setUserPic(user.getImageUrl());
				
				return uRes;
			}
			throw new UserNotFoundException("User not found");
		} catch (Exception e) {
			throw new UserNotFoundException("User not found");
		}
	}
}
