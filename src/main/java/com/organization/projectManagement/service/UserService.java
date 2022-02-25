package com.organization.projectManagement.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.organization.projectManagement.entity.AuthProvider;
import com.organization.projectManagement.entity.User;
import com.organization.projectManagement.exception.UserAlreadyExistException;
import com.organization.projectManagement.exception.UserNotFoundException;
import com.organization.projectManagement.model.request.PasswordChangeRequest;
import com.organization.projectManagement.model.request.UpdateUserRequest;
import com.organization.projectManagement.model.response.UserResponse;
import com.organization.projectManagement.repo.UserRepository;

import net.bytebuddy.utility.RandomString;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passEncode;

	@Autowired
	private JavaMailSender mailSender;

	public User createUser(User user) {

		try {
			String email = user.getEmail();

			User existUser = userRepo.findByUsername(email);
			if (existUser != null) {
				throw new UserAlreadyExistException("User with '" + email + "' already exist.");
			}

			user.setPassword(passEncode.encode(user.getPassword()));
			user.setUsername(email);
			user.setRole("ROLE_PERSONAL");
			user.setProvider(AuthProvider.local);

			return userRepo.save(user);
		} catch (Exception e) {
			throw new UserAlreadyExistException("User with '" + user.getEmail() + "' already exist.");
		}
	}

	public String updateUser(UpdateUserRequest request) {

		try {

			User user = userRepo.findById(request.getUserId()).get();

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

			User user = userRepo.findById(userId).get();
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
	
	public String updateOldPassword(PasswordChangeRequest requestPwd) {
		
		try {
			
			User user = userRepo.findById(requestPwd.getUserId()).get();
			if(user != null) {
				updatePassword(user, requestPwd.getPassword());
				sendPasswordChangeEmail(user.getEmail(), user.getName());
				
				return "Password updated successfully";
			}
			
			throw new UserNotFoundException("User not Found");
		}catch(Exception e) {
			throw new UserNotFoundException("User not Found");
		}
		
	}

	public void updateResetPassword(String email) {
		User user = userRepo.findByEmail(email).get();

		if (user != null) {
			String token = generateToken();
			user.setResetPasswordToken(token);
			userRepo.save(user);

			try {
				sendEmailWithForgotPwdLink(email, token);
			} catch (UnsupportedEncodingException | MessagingException e) {
				e.printStackTrace();
			}
		} else {
			throw new UserNotFoundException("Could not found any user with email" + email);
		}
	}

	public User get(String passwordToken) {
		return userRepo.findByResetPasswordToken(passwordToken);
	}

	public void updatePassword(User user, String newPassword) {

		BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();

		String encodePass = passEncoder.encode(newPassword);

		user.setPassword(encodePass);
		user.setResetPasswordToken(null);

		userRepo.save(user);

	}

	public String generateToken() {

		String token = RandomString.make(60);
		return token;
	}

	private void sendEmailWithForgotPwdLink(String email, String token)
			throws UnsupportedEncodingException, MessagingException {

		String resetPassLink = "https://localhost:3002/reset-password?token=" + token;

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("contact@probucket.com", "Pro Bucket Team");
		helper.setTo(email);

		String subject = "Here is the link to reset your password";
		String content = "<p> Hello, </p>" + "<p>You have requested to reset your password</p>"
				+ "<p>Click the link below to change your password</p>" + "<p><b><a href=\"" + resetPassLink
				+ "\">Change password</a></b></p>";

		helper.setSubject(subject);
		helper.setText(content, true);

		mailSender.send(message);

	}
	
	
	private void sendPasswordChangeEmail(String email, String name) throws 
									UnsupportedEncodingException, MessagingException {
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		String link = "https://localhost:3002/query";
		
		helper.setFrom("contact@probucket.com", "Pro Bucket Team");
		helper.setTo(email);
		
		String subject = "Password change notification";
		String content = "<p>Hello " + name + ",</p>"
			+ "<p>This is a confirmation that your Pro Bucket password has been changed successfully.</p>"
			+ "<p>If you requested the password change, there is no further action needed, and your new Pro Bucket"
			+ " password is active immediately.</p>"
			+ "<p>If you have any question or did not request a password change, Please contact us at:</p>"
			+ "<p><a href=\"https://localhost:3002/query\">" + link + "</a></p>"
			+ "<p>Thank you,</p>"
			+ "<p>Team Pro Bucket</p>";
		
		helper.setSubject(subject);
		helper.setText(content);
		
		mailSender.send(message);
		
	}

}
