package RSA;
import java.math.BigInteger;

public class RSA {
	
	private RSARandomGen rsaRand = new RSARandomGen();
	
	public RSA(int size){
		this.rsaRand.RSAGen(size);
	}
	
	public BigInteger encrypt(BigInteger plaintext){
		
		BigInteger exp = rsaRand.getPublicKey();
		BigInteger modulus = rsaRand.getModulus();
		
		return plaintext.modPow(exp, modulus);
		
	}
	
	public String decrypt(String ciphertext){
		return "Not Implemented";
	}
	
	public String printRSA(){
		return this.rsaRand.toString();
	}	
}
