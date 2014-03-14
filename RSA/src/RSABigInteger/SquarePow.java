package RSABigInteger;
import java.math.BigInteger;


public class SquarePow implements PowerStrategy{

	@Override
	public BigInteger pow(BigInteger x, int e) {
		
		if(e < 0) 
			return pow(BigInteger.ONE.divide(x), -e);
		else if(e == 0)
			return BigInteger.ONE;
		else if(e == 1)
			return x;
		else if((e%2) == 0) //even
			return pow(x.multiply(x), (e/2));
		else return x.multiply(pow(x.multiply(x), ((e-1)/2))); //odd
	}
	
	@Override
	public BigInteger pow(BigInteger x, BigInteger e) {
		return this.pow(x, e.intValue());
	}
	
	
}
