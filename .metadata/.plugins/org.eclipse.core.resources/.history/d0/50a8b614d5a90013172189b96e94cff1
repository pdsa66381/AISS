import java.math.BigInteger;

public class RSA {
	
	public BigInteger modPow(String msg, BigInteger exp, BigInteger n){
		
		BigInteger convert = new BigInteger(msg);
		BigInteger result=convert;
		while(exp.intValue()>1){
			result = convert.multiply(result);
			exp= exp.subtract(BigInteger.ONE);
		}
		result=result.mod(n);
		
		return result;
		}

	private BigInteger expBySquare(BigInteger x, BigInteger n){
		
		if(n.intValue()< 0) 
			return expBySquare(BigInteger.ONE.divide(x), n.negate());
		else if(n.intValue()==0)
			return BigInteger.ONE;
		else if(n.intValue()==1)
			return x;
		else if(n.divide(BigInteger.valueOf(2)).intValue() == 0) //even
			return expBySquare(x.multiply(x), n.divide(BigInteger.valueOf(2)));
		else return x.multiply(expBySquare(x.multiply(x), n.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2))));
	}
	
	private void showTime(BigInteger value, long rsaStartTime){
		System.out.println("RSA value: " + value);
		System.out.println("Total Time:  " + (System.nanoTime() - rsaStartTime));
	}
	
	public static void main(String[] args){
		
		RSA rsa= new RSA();
		BigInteger p = new BigInteger("400");
		BigInteger n = new BigInteger("2047");
		BigInteger msg = new BigInteger("1024");
	
		
		System.out.println("ModPow:");
		long rsaStartTime = System.nanoTime();
		BigInteger value = rsa.modPow("1024", p, n);
		rsa.showTime(value, rsaStartTime);
		
		System.out.println("RSA bIvalue: ");
		rsaStartTime = System.nanoTime();
		value=msg.modPow(p, n);
		rsa.showTime(value, rsaStartTime);
		
		System.out.println("RSA exponential by square: ");
		rsaStartTime = System.nanoTime();
		value=rsa.expBySquare(msg, p);
		rsa.showTime(value, rsaStartTime);
	}
	
}
