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
	
	/*public  BigInteger modPowSquare(String msg, BigInteger exp, BigInteger n){
		rodrigo.jm.lourenco@gmail.com
		githup
		BigInteger convert = new BigInteger(msg);
		BigInteger result=convert;
		return 
	}*/
	
	private BigInteger expBySquare(BigInteger x, BigInteger n){
		
		if(n.intValue()< 0) 
			return expBySquare(BigInteger.ONE.divide(x), n.negate());
		else if(n.intValue()==0)
			return BigInteger.ONE;
		else if(n.intValue()==1)
			return x;
		//else if 
		
	}
	
	public static void main(String[] args){
		
		RSA rsa= new RSA();
		BigInteger p = new BigInteger("400");
		BigInteger n = new BigInteger("2047");
		BigInteger msg = new BigInteger("1024");
		long rsaStartTime = System.nanoTime();
		System.out.println("RSA value: " + rsa.modPow("1024", p, n) + " FinalTime: " + (System.nanoTime() - rsaStartTime));
		rsaStartTime = System.nanoTime();
		System.out.println("RSA bIvalue: " + msg.modPow(p, n)+ " FinalTime: " + (System.nanoTime() - rsaStartTime));
		
	}
}
