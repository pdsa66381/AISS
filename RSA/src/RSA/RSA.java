package RSA;
import java.math.BigInteger;

public class RSA {
	
	private final RSARandomGen rsaRand;
	
	private boolean blind = false;
	
	public RSA(int size){
		this.rsaRand = new RSARandomGen(size);
	}
	
	
	public BigInteger encrypt(BigInteger msg){
		
		if(blind)
			return blindEncrypt(msg);
		else
			return encryptSimple(msg);
		
	}
	
	public BigInteger decrypt(BigInteger msg){
			
			if(blind)
				return unblindDecrypt(msg);
			else
				return decryptSimple(msg);
			
	}
	
	private BigInteger encryptSimple(BigInteger plaintext){
		
		BigInteger d = rsaRand.getPrivateKey();
		BigInteger n = rsaRand.getModulus();
		
		return plaintext.modPow(d, n);
		
	}
	
	private BigInteger decryptSimple(BigInteger ciphertext){
		
		BigInteger e = rsaRand.getPublicKey();
		BigInteger n = rsaRand.getModulus();
		
		return ciphertext.modPow(e, n);
	}
	
	private BigInteger blindEncrypt(BigInteger plaintext){
		
		BigInteger e = this.rsaRand.getPublicKey();
		BigInteger d = this.rsaRand.getPrivateKey();
		BigInteger n = this.rsaRand.getModulus();
		
		//Bling message
		BigInteger blindingFactor = this.rsaRand.generateBlindingFactor();
		BigInteger blind = plaintext.multiply(blindingFactor.modPow(e, n));
		
		//Encrypt Blinded Message
		return blind.modPow(d, n);
	}

	private BigInteger unblindDecrypt(BigInteger ciphertext){
		
		BigInteger e = this.rsaRand.getPublicKey();
		BigInteger n = this.rsaRand.getModulus();
		BigInteger r = this.rsaRand.getBlindingFactor();

		//Unblind
        BigInteger unblind = r.modInverse(n).multiply(ciphertext).mod(n);
		
        return unblind.modPow(e, n);
	}
	
	public String printRSA(){
		return this.rsaRand.toString();
	}

	public BigInteger getPublicKey() {
		return this.rsaRand.getPublicKey();
	}

	public void setBlinding(){
		blind = !blind;
	}
	
}

