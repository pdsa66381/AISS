	package RSABigInteger;
import java.math.BigInteger;


public class SquarePow implements PowerStrategy{

	@Override
	public BigInteger pow(BigInteger x, BigInteger e) {
		// TODO Auto-generated method stub
		return null;
	}	

	@Override
	public BigInteger pow(BigInteger x, int e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger modPow(BigInteger x, BigInteger e, BigInteger n) {
		System.out.println("Using power square:");
		BigInteger y = BigInteger.ONE;
		char arr[] = e.toString(2).toCharArray();
		int i;
		//BigInteger range = n;
		
		for(i=0; i<arr.length;i++){
			y=(y.multiply(y)).mod(n);
			if(arr[i]== '1')
				y=(x.multiply(y)).mod(n);	
		}
		
		return y;
	}
	
}
