package RSABigInteger;
import java.math.BigInteger;


public class SquarePow implements PowerStrategy{

	@Override
	public BigInteger pow(BigInteger x, int e) {

		return null;
	}
	
	@Override
	public BigInteger pow(BigInteger x, BigInteger e) {
		return this.pow(x, e.intValue());
	}
	
	
}