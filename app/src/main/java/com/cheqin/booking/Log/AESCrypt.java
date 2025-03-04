package com.cheqin.booking.Log;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 */
public final class AESCrypt {

	private static final String TAG = "AESCrypt";

	// AESCrypt-ObjC uses CBC and PKCS7Padding
	private static final String AES_MODE = "AES/CBC/PKCS7Padding";
	private static final String CHARSET = "UTF-8";

	// AESCrypt-ObjC uses SHA-256 (and so a 256-bit key)
	private static final String HASH_ALGORITHM = "SHA-256";

	// AESCrypt-ObjC uses blank IV (not the best security, but the aim here is
	// compatibility)
	private static final byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	// togglable log option (please turn off in live!)
	public static boolean DEBUG_LOG_ENABLED = false;

	
	/**
	 * Method generateKey.
	 * @param password String
	 * @return SecretKeySpec
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	private static SecretKeySpec generateKey(final String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
		byte[] bytes = password.getBytes("UTF-8");
		digest.update(bytes, 0, bytes.length);
		byte[] key = digest.digest();

		log("SHA-256 key ", key);

		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		return secretKeySpec;
	}

	
	/**
	 * Method encrypt.
	 * @param password String
	 * @param message String
	 * @return String
	 * @throws GeneralSecurityException
	 */
	public static String encrypt(final String password, String message)
			throws GeneralSecurityException {

		try {
			final SecretKeySpec key = generateKey(password);

			log("message", message);

			byte[] cipherText = encrypt(key, ivBytes, message.getBytes(CHARSET));

			// NO_WRAP is important as was getting \n at the end
			String encoded = Base64.encodeToString(cipherText, Base64.NO_WRAP);
			log("Base64.NO_WRAP", encoded);
			return encoded;
		} catch (UnsupportedEncodingException e) {
			if (DEBUG_LOG_ENABLED)
				Log.e(TAG, "UnsupportedEncodingException ", e);
			throw new GeneralSecurityException(e);
		}
	}

	
	/**
	 * Method encrypt.
	 * @param key SecretKeySpec
	 * @param iv byte[]
	 * @param message byte[]
	 * @return byte[]
	 * @throws GeneralSecurityException
	 */
	public static byte[] encrypt(final SecretKeySpec key, final byte[] iv,
			final byte[] message) throws GeneralSecurityException {
		final Cipher cipher = Cipher.getInstance(AES_MODE);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		byte[] cipherText = cipher.doFinal(message);

		log("cipherText", cipherText);

		return cipherText;
	}

	
	/**
	 * Method decrypt.
	 * @param password String
	 * @param base64EncodedCipherText String
	 * @return String
	 * @throws GeneralSecurityException
	 */
	public static String decrypt(final String password,
			String base64EncodedCipherText) throws GeneralSecurityException {

		try {
			final SecretKeySpec key = generateKey(password);

			log("base64EncodedCipherText", base64EncodedCipherText);
			byte[] decodedCipherText = Base64.decode(base64EncodedCipherText,
					Base64.NO_WRAP);
			log("decodedCipherText", decodedCipherText);

			byte[] decryptedBytes = decrypt(key, ivBytes, decodedCipherText);

			log("decryptedBytes", decryptedBytes);
			String message = new String(decryptedBytes, CHARSET);
			log("message", message);

			return message;
		} catch (UnsupportedEncodingException e) {
			if (DEBUG_LOG_ENABLED)
				Log.e(TAG, "UnsupportedEncodingException ", e);

			throw new GeneralSecurityException(e);
		}
	}
	/**
	 * Method decrypt.
	 * @param key SecretKeySpec
	 * @param iv byte[]
	 * @param decodedCipherText byte[]
	 * @return byte[]
	 * @throws GeneralSecurityException
	 */
	public static byte[] decrypt(final SecretKeySpec key, final byte[] iv,
			final byte[] decodedCipherText) throws GeneralSecurityException {
		final Cipher cipher = Cipher.getInstance(AES_MODE);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		byte[] decryptedBytes = cipher.doFinal(decodedCipherText);

		log("decryptedBytes", decryptedBytes);

		return decryptedBytes;
	}

	/**
	 * Method log.
	 * @param what String
	 * @param bytes byte[]
	 */
	private static void log(String what, byte[] bytes) {
		if (DEBUG_LOG_ENABLED)
			Log.d(TAG, what + "[" + bytes.length + "] [" + bytesToHex(bytes)
					+ "]");
	}

	/**
	 * Method log.
	 * @param what String
	 * @param value String
	 */
	private static void log(String what, String value) {
		if (DEBUG_LOG_ENABLED)
			Log.d(TAG, what + "[" + value.length() + "] [" + value + "]");
	}
	/**
	 * Method bytesToHex.
	 * @param bytes byte[]
	 * @return String
	 */
	private static String bytesToHex(byte[] bytes) {
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	
}