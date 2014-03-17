package RSABigInteger;
import java.math.BigInteger;
import java.util.Random;

public class RSABigInteger extends BigInteger{

	private static final long serialVersionUID = 1L;

	private PowerStrategy strategy = new SimplePower();
	
	public RSABigInteger(String val) {
		super(val);
	}
	
	public RSABigInteger(byte[] val) {
		super(val);
	}

	public RSABigInteger(int numBits, Random rand) {
		super(numBits, rand);
	}

	@Override
	public BigInteger pow(int exponent) {
		return this.strategy.pow(this, exponent);
	}
	
	@Override
	public BigInteger modPow(BigInteger e, BigInteger n){
		
		BigInteger result = BigInteger.ZERO;
		
		result = this.strategy.modPow(this, e, n);
		
		return result;
	}
	
	public void modifyPowerStategy(PowerStrategy strategy){
		this.strategy = strategy;
	}
}