package com.tool.ProjectTool.service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.tool.ProjectTool.entity.Users;
import com.tool.ProjectTool.exception.UserAlreadyExistException;
import com.tool.ProjectTool.model.request.RequestTeamMember;
import com.tool.ProjectTool.model.response.EmailResponse;
import com.tool.ProjectTool.repo.UserRepository;

import freemarker.template.Configuration;
import freemarker.template.Template;
import net.bytebuddy.utility.RandomString;

@Service
public class TeamService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passEncode;
	
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration config;
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	public String createNewTeamMember(RequestTeamMember team) {
		
		String ids = randomString(20);
		String id = null;
		
		Users user = userRepo.findByEmail(team.getEmail());
		Users checkIds = userRepo.findByUserId(ids);
		
		if(user != null) {
			throw new UserAlreadyExistException("User with email " + team.getEmail() + " is already exists.");
		}
		
		Users newUser = new Users();
		
		String randomPass = RandomString.make(14);
		
		if (checkIds != null) {
			id = randomString(20);
		} else {
			id = ids;
		}
		
		newUser.setUserId(id);
		newUser.setEmail(team.getEmail());
		newUser.setName(team.getName());
		newUser.setEmailVerified(false);
		newUser.setPassword(passEncode.encode(randomPass));
		
		userRepo.save(newUser);
		sendNewTeamMemberConfirmationEmail(team.getEmail(), randomPass);
		
		return "New team member added sucessfully";
	}
	
	public EmailResponse sendNewTeamMemberConfirmationEmail(String email, String secretString) {

		EmailResponse response = new EmailResponse();
		MimeMessage message = mailSender.createMimeMessage();

		Map<String, Object> model = new HashMap<>();

		try {

			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			// helper.addAttachment("logo.png", new ClassPathResource("/static/logo.png"));

			model.put("secretString", secretString);

			Template t = config.getTemplate("new-team-member-template.ftl");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

			helper.setTo(email);
			helper.setText(html, true);
			helper.setSubject("Complete your password reset request");
			helper.setFrom("probucket@info.co.in");
			helper.addInline("logo.png", new ClassPathResource("logo.png"));

			mailSender.send(message);

			response.setMessage("Mail send to: " + email + " and successfuly requested.");
			response.setStatus(true);

		} catch (Exception e) {
			response.setMessage(
					"Mail Sending failure " + e.getMessage() + " and something went wrong while requesting.");
			response.setStatus(false);
		}

		return response;

	}
	
}
