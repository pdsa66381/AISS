package RSABigInteger;

import java.math.BigInteger;

public class BlindingRSA implements PowerStrategy{

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
		System.out.println("Using blinding rsa:");
		//Recebo um m (msg)
		// m' = m*squarePow(x,e,n)
		// s'= (m')^d (mod N)
		//s = s' * r ^-1 (mod N)
		
		return null;
	}

}
