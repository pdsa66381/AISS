package RSABigInteger;
import java.math.BigInteger;


public class SimplePower implements PowerStrategy {

	/**
	 * Performs an exponentiation through iterative multiplication
	 */
	@Override
	public BigInteger pow(BigInteger x, BigInteger e) {
		
		BigInteger result = BigInteger.ONE;
		
		while(e.intValue()>0){
			result = x.multiply(result);
			e= e.subtract(BigInteger.ONE);
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

}
