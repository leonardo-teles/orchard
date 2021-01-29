package com.orchard.utility;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.orchard.model.AppUser;

@Component
public class EmailConstructor {

	@Autowired
	private Environment env;
	
	@Autowired
	private TemplateEngine template;
	
	public MimeMessagePreparator newUserEmail(AppUser user, String password) {
		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("password", password);
		
		String html = template.process("email/newUser", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8"); 
				helper.setPriority(1);
				helper.setTo(user.getEmail());
				helper.setText(html, true);
				helper.setSubject("Welcome to Orchart");
				helper.setFrom(new InternetAddress(env.getProperty("support.email")));
			}
		};
		
		return messagePreparator;
	}
	
	public MimeMessagePreparator updateUserEmail(AppUser user) {
		Context context = new Context();
		context.setVariable("user", user);

		String html = template.process("email/updateUser", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8"); 
				helper.setPriority(1);
				helper.setTo(user.getEmail());
				helper.setText(html, true);
				helper.setSubject("Profile Update - Orchart");
				helper.setFrom(new InternetAddress(env.getProperty("support.email")));
			}
		};
		
		return messagePreparator;
	}
	
	public MimeMessagePreparator resetPasswordEmail(AppUser user, String password) {
		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("password", password);

		String html = template.process("email/resetPassword", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8"); 
				helper.setPriority(1);
				helper.setTo(user.getEmail());
				helper.setText(html, true);
				helper.setSubject("New Password - Orchart");
				helper.setFrom(new InternetAddress(env.getProperty("support.email")));
			}
		};
		
		return messagePreparator;
	}
	
}
