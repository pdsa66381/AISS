package RSABigInteger;
import java.math.BigInteger;
import java.util.Random;

public class IterativeBigInteger extends BigInteger{

	private static final long serialVersionUID = 1L;

	
	public IterativeBigInteger(String val) {
		super(val);
	}
	
	public IterativeBigInteger(byte[] val) {
		super(val);
	}

	public IterativeBigInteger(int numBits, Random rand) {
		super(numBits, rand);
	}

	@Override
	public BigInteger modPow(BigInteger e, BigInteger n){
		
		BigInteger result = BigInteger.ONE;
		
		while(e.doubleValue()>0){
			result = this.multiply(result);
			e=e.subtract(BigInteger.ONE);	
		}
		
		return new IterativeBigInteger(result.mod(n).toByteArray());
	}
}