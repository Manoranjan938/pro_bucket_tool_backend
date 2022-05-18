package com.tool.ProjectTool.emails;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.tool.ProjectTool.model.response.EmailResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class PasswordChangeRequest {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration config;

	public EmailResponse sendRegistrationConfirmationEmail(String email) {

		EmailResponse response = new EmailResponse();
		MimeMessage message = mailSender.createMimeMessage();

		Map<String, Object> model = new HashMap<>();

		try {

			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			// helper.addAttachment("logo.png", new ClassPathResource("/static/logo.png"));

			model.put("user_email", email);

			Template t = config.getTemplate("password-change-template.ftl");
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
