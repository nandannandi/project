package com.example.easyShop;

import java.security.MessageDigest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MD5PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		
		String encPass = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(rawPassword.toString().getBytes());
            String b64 = Base64.encodeBase64String(digest);
            encPass = new String(b64);
          //  encPass = encPass.replaceAll("=", "");
        }catch(Exception ex){
            System.out.println("An exception trying to encode a password"+ ex);
        }
        return encPass;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encode(rawPassword).equals(encodedPassword);
	}

}
