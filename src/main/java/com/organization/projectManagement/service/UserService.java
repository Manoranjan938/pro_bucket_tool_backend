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
import com.organization.projectManagement.model.request.UpdateUserRequest;
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
			if(existUser != null) {
				throw new UserAlreadyExistException("User with '" + email + "' already exist.");
			}
			
			user.setPassword(passEncode.encode(user.getPassword()));
			user.setUsername(email);
			user.setRole("ROLE_PERSONAL");
			user.setProvider(AuthProvider.local);
			
			return userRepo.save(user);
		}catch(Exception e) {
			throw new UserAlreadyExistException("User with '" + user.getEmail() + "' already exist.");
		}
	}
	
	
	public String updateUser(UpdateUserRequest request) {
		
		try {
			
			User user = userRepo.findById(request.getUserId()).get();
			
			if(user != null) {
				user.setName(request.getName());
				user.setPhone(request.getPhone());
				
				userRepo.save(user);
				
				return "User updated successfully";
			}
			
		}catch(Exception e) {
			throw new UserNotFoundException("User not found");
		}
		
		return null;
	}
	
	
	public void updateResetPassword(String email) {
		User user = userRepo.findByEmail(email).get();
		
		if(user != null) {
			String token = generateToken();
			user.setResetPasswordToken(token);
			userRepo.save(user);
			
			try {
				sendEmailWithForgotPwdLink(email, token);
			} catch (UnsupportedEncodingException | MessagingException e) {
				e.printStackTrace();
			}
		}
		else {
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
	
	private void sendEmailWithForgotPwdLink(String email, String token) throws UnsupportedEncodingException, MessagingException {
		
		String resetPassLink = "http://localhost:9025/reset-password?token=" + token;
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("contact@probucket.com", "Pro Bucket Team");
		helper.setTo(email);
		
		String subject = "Here is the link to reset your password";
		String content = "<p> Hello, </p>"
				 + "<p>You have requested to reset your password</p>"
				 + "<p>Click the link below to change your password</p>"
				 + "<p><b><a href=\"" + resetPassLink + "\">Change password</a></b></p>";
		
		helper.setSubject(subject);
		helper.setText(content, true);
		
		mailSender.send(message);
		
	}

}
