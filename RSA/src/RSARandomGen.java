import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import RSABigInteger.RSABigInteger;

public class RSARandomGen {
	
	private final SecureRandom random = new SecureRandom();
	
	private BigInteger privateKey;
	private BigInteger publicKey;
	private BigInteger modulus;
	
	/**
	 * Randomly generates an exponent with a given size.
	 * @param size the size of the exponent
	 * @return the exponent which is an RSABigInteger
	 */
	public void RSAGen(int size){

		BigInteger 	p, q, phiN;
		RSABigInteger e;
		
		//Two prime numbers q and p are randomly chosen
		p = BigInteger.probablePrime(size/2, this.random);
		q = BigInteger.probablePrime(size/2, this.random);
		
		//Then we calculate n, such that n = p * q
		//this.n = q.multiply(q);
		this.modulus = q.multiply(p);
		
		//phiN is calculated such as phiN=(p-1).(q-1)
		phiN = p.subtract(BigInteger.ONE);
		phiN = phiN.multiply(q.subtract(BigInteger.ONE));
		
		//A random e exponent is calculated such that 
		do{
			e = new RSABigInteger(size, new Random());
		}while(e.compareTo(phiN)!=1 || e.gcd(phiN).compareTo(BigInteger.ONE)!=0);
	
		this.publicKey = e;
		this.privateKey = this.publicKey.modInverse(phiN);
	}
	
	public BigInteger getPublicKey(){
		return this.publicKey;
	}
	
	public BigInteger getModulus(){
		return this.modulus;
	}
	
	public String toString(){
		String s = "";
		
		s += "Public Key: \t"+this.publicKey+"\n";
		s += "Private Key: \t"+ this.privateKey+"\n";
		s += "Modulus: \t"+this.modulus;
		
		return s;
	}
}
