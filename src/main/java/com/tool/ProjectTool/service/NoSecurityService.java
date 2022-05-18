package com.tool.ProjectTool.service;

import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class NoSecurityService {
	
	public String sendVerificationOTP() {
		
		final String ACCOUNT_SID = "AC710f8ee0bcd11338a5cb2daf80158d00"; 
		final String MESSEGING_SID = "MG17f098de406767c01c9e3f9bec8d90b0";
	    final String AUTH_TOKEN = "6f0c577f4745d2abca6a8b378adacdf8"; 
	 
	    Twilio.init(ACCOUNT_SID, AUTH_TOKEN); 
        Message message = Message.creator( 
                new PhoneNumber("+917991035982"),   
                MESSEGING_SID, "Hey Buddy.. Good evening Again from Project tool")
            .create(); 
 
        //System.out.println(message.getSid()); 
		
		return message.toString();
	}
	
	

}
