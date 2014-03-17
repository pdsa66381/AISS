package RSABigInteger;
import java.math.BigInteger;


public interface PowerStrategy {
	
	BigInteger pow(BigInteger x, BigInteger e);
	BigInteger pow(BigInteger x, int e);
	BigInteger modPow(BigInteger x, BigInteger e, BigInteger n);
	
}
