package RSABigInteger;

import java.math.BigInteger;


public class SimplePower implements PowerStrategy {

	/**
	 * Performs an exponentiation through iterative multiplication
	 */
	@Override
	public BigInteger pow(BigInteger x, BigInteger e) {
					
		System.out.println("Using SimplePower...");
		
		BigInteger result = BigInteger.ONE;
		while(e.doubleValue()>0){
			result = x.multiply(result);
			e=e.subtract(BigInteger.ONE);	
		}
		
		return result;
	}

	@Override
	public BigInteger pow(BigInteger x, int e) {

		BigInteger result = BigInteger.ONE;
		
		while(e>0){
			result = x.multiply(result);
			e--;	
		}
		
		return result;
	}

	@Override
	public BigInteger modPow(BigInteger x, BigInteger e, BigInteger n) {
		// TODO Auto-generated method stub
		return null;
	}

}
