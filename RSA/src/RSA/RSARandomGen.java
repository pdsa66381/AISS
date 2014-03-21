package RSA;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RSARandomGen {
	
	private final BigInteger one = BigInteger.ONE;
	private final SecureRandom random = new SecureRandom();
	
	private final int size;
	private final BigInteger privateKey;
	private final BigInteger publicKey;
	private final BigInteger modulus;

	private BigInteger blindingFactor;
	
	/**
	 * Randomly generates an exponent with a given size.
	 * @param size the size of the exponent
	 * @return the exponent which is an RSABigInteger
	 */
	public RSARandomGen(int size){

		this.size = size;
		
		BigInteger 	p, q, phiN;
		BigInteger e;
		
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
			e = new BigInteger(size, new Random());
		}while(phiN.compareTo(e)!=1 || e.gcd(phiN).compareTo(BigInteger.ONE)!=0);
	
		this.publicKey = e;
		this.privateKey = this.publicKey.modInverse(phiN);

	}

	/**
	 * 
	 * @return
	 */
	public BigInteger generateBlindingFactor(){
		
		BigInteger r, gcd;
		
		do {
            r = new BigInteger(size/2, random);
            gcd = r.gcd(this.modulus);
            	
        }
        while(!gcd.equals(one) || r.compareTo(modulus)>=0);
		
		this.blindingFactor = r;
		
		return r;
	}
	
	public BigInteger getBlindingFactor(){
		return this.blindingFactor;
	}

	public BigInteger getPrivateKey(){
		return this.privateKey;
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
