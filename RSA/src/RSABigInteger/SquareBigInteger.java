package RSABigInteger;
import java.math.BigInteger;
import java.util.Random;

public class SquareBigInteger extends BigInteger{

	private static final long serialVersionUID = 1L;

	public SquareBigInteger(String val) {
		super(val);
	}
	
	public SquareBigInteger(byte[] val) {
		super(val);
	}

	public SquareBigInteger(int numBits, Random rand) {
		super(numBits, rand);
	}

	
	@Override
	public BigInteger modPow(BigInteger e, BigInteger n){
		
		BigInteger y = BigInteger.ONE;
		char arr[] = e.toString(2).toCharArray();
		int i;
		
		for(i=0; i<arr.length;i++){
			y=(y.multiply(y)).mod(n);
			if(arr[i]== '1')
				y= (this.multiply(y)).mod(n);	
		}
		
		return new SquareBigInteger(y.toByteArray());
	}
}