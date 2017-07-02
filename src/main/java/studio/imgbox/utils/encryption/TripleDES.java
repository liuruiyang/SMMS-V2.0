package studio.imgbox.utils.encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.*;

import sun.misc.*;

/**
 * TripleDES encryption algorithm, providing the encryption and decryption
 * algorithm for byte array and string
 */
public class TripleDES {
	// The length of Encryptionstring should be 24 bytes and not be a weak key
	private String EncryptionString;
	// The initialization vector should be 8 bytes
	private final byte[] EncryptionIV = "CKI1TEN2".getBytes();
	private final static String DESede = "DESede/CBC/PKCS5Padding";
	private final static TripleDES cry = new TripleDES("CKI1TEN2WIF3IAC4MQO5SRE6POR7TFE@");

	public TripleDES() {
	}

	/**
	 * Saving key for encryption and decryption
	 * @param EncryptionString String
	 */
	public TripleDES(String EncryptionString) {
		this.EncryptionString = EncryptionString;
	}

	/**
	 * Encrypt a byte array
	 * @param SourceData byte[]
	 * @throws Exception
	 * @return byte[]
	 */
	public byte[] EncryptionByteData(byte[] SourceData) throws Exception {
		byte[] retByte = null;
		// Create SecretKey object
		byte[] EncryptionByte = EncryptionString.getBytes();
		DESedeKeySpec dks = new DESedeKeySpec(EncryptionByte);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Create IvParameterSpec object with initialization vector
		IvParameterSpec spec = new IvParameterSpec(EncryptionIV);
		// Create Cipter object
		Cipher cipher = Cipher.getInstance(DESede);
		// Initialize Cipher object
		cipher.init(Cipher.ENCRYPT_MODE, securekey, spec);
		// Encrypting data
		retByte = cipher.doFinal(SourceData);
		return retByte;
	}

	/**
	 * Decrypt a byte array
	 * @param SourceData byte[]
	 * @throws Exception
	 * @return byte[]
	 */
	public byte[] DecryptionByteData(byte[] SourceData) throws Exception {
		byte[] retByte = null;

		// Create SecretKey object
		byte[] EncryptionByte = EncryptionString.getBytes();
		DESedeKeySpec dks = new DESedeKeySpec(EncryptionByte);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		SecretKey securekey = keyFactory.generateSecret(dks);

		// Create IvParameterSpec object with initialization vector
		IvParameterSpec spec = new IvParameterSpec(EncryptionIV);

		// Create Cipter object
		Cipher cipher = Cipher.getInstance(DESede);

		// Initialize Cipher object
		cipher.init(Cipher.DECRYPT_MODE, securekey, spec);

		// Decrypting data
		retByte = cipher.doFinal(SourceData);
		return retByte;
	}

	/**
	 * Encrypt a string
	 * @param SourceData String
	 * @throws Exception
	 * @return String
	 */
	@SuppressWarnings("restriction")
	public String EncryptionStringData(String sourceData) throws Exception {
		String retStr = null;
		byte[] retByte = null;

		// Transform SourceData to byte array
		byte[] sorData = sourceData.getBytes();

		// Encrypte data
		retByte = EncryptionByteData(sorData);

		// Encode encryption data
		BASE64Encoder be = new BASE64Encoder();
		retStr = be.encode(retByte);

		return retStr;
	}

	/**
	 * Decrypt a string
	 * @param SourceData String
	 * @throws Exception
	 * @return String
	 */
	@SuppressWarnings("restriction")
	public String DecryptionStringData(String sourceData) throws Exception {
		String retStr = null;
		byte[] retByte = null;

		// Decode encryption data
		BASE64Decoder bd = new BASE64Decoder();
		byte[] sorData = bd.decodeBuffer(sourceData);

		// Decrypting data
		retByte = DecryptionByteData(sorData);
		retStr = new String(retByte);

		return retStr;
	}

	/**
	 * 加密接口
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encryption(String data) throws Exception {
		// return cry.EncryptionStringData(data).replaceAll("\\+", "F3K").replaceAll("=", "").replaceAll("/", "X64");
		return cry.EncryptionStringData(data);
	}

	/**
	 * 解密接口
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String decryption(String data) throws Exception {
		// return cry.DecryptionStringData((data+"=").replaceAll("F3K", "\\+").replaceAll("X64", "/"));
		return cry.DecryptionStringData(data);
	}

}