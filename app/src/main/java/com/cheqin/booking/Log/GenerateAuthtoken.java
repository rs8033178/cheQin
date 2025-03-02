package com.cheqin.booking.Log;

import android.util.Base64;

import com.cheqin.booking.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;


/**
 */
public final class GenerateAuthtoken {

	/**
	 * Method generate.
	 * @param email String
	 * @param pword String
	 * @param login_type String
	 * @return String
	 */
	public static String generate(String email,String pword,String login_type)
	{
		String authToken_final = null;
		if(login_type.equalsIgnoreCase("otp")||login_type.equalsIgnoreCase("facebook")||login_type.equalsIgnoreCase("gmail")||login_type.equalsIgnoreCase("social"))
		{
			String authString=email+"|"+ Constants.encryption_salt+"|"+pword+"|"+login_type;
			System.out.println(authString);
			String authToken_1;

			try {
				authToken_1 = AESCrypt.encrypt(Constants.aes_key, authString);
				byte[] encrypt_byte = null;
				try {
					encrypt_byte = authToken_1.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				authToken_final = Base64.encodeToString(encrypt_byte, Base64.DEFAULT);


			} catch (GeneralSecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		return authToken_final;
	}
	/**
	 * Method encryptpasschange.
	 * @param oldpass String
	 * @param newpass String
	 * @param repass String
	 * @return String
	 */
	public static String encryptpasschange(String oldpass, String newpass, String repass)
	{
		String passString=oldpass+"|"+Constants.encryption_salt+"|"+newpass+"|"+repass;
		System.out.println(passString);
		String encryptedpass = null;

		try {
			String password_1 = AESCrypt.encrypt(Constants.aes_key, passString);
			byte[] encrypt_byte = null;
			try {
				encrypt_byte = password_1.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			encryptedpass = Base64.encodeToString(encrypt_byte, Base64.DEFAULT);


		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(encryptedpass);
		return encryptedpass;
	}
	/**
	 * Method encryptpasswordreset.
	 * @param newpass String
	 * @return String
	 */
	public static String encryptpasswordreset(String newpass)
	{
		return null;
	}

	/**
	 * Method encryptpassregistration.
	 * @param password String
	 * @return String
	 */
	public static String encryptpassregistration(String password)
	{
		String passString=Constants.encryption_salt+"|"+password;
		System.out.println(passString);
		String encryptedpass = null;

		try {
			String password_1 = AESCrypt.encrypt(Constants.aes_key, passString);
			byte[] encrypt_byte = null;
			try {
				encrypt_byte = password_1.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			encryptedpass = Base64.encodeToString(encrypt_byte, Base64.DEFAULT);


		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(encryptedpass);
		return encryptedpass;
	}
	/**
	 * Method decryptpassword.
	 * @param password String
	 * @return String
	 */
	public static String decryptpassword(String password)
	{
		String authToken1=null;
		String authToken_final=null;
		byte[] bytes = Base64.decode(password, Base64.DEFAULT);
		try {
			authToken1 = new String(bytes, "UTF-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			authToken_final = AESCrypt.decrypt(Constants.aes_key,authToken1);
			
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("decpassword"+authToken_final);
		return authToken_final;

	}
}