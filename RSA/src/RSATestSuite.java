import java.math.BigInteger;
import java.util.Random;


import RSABigInteger.RSABigInteger;
import RSABigInteger.SimplePower;
import RSABigInteger.SquarePow;

public class RSATestSuite {
	

	
	public String performTest1(int key_size, int input_size){
		
		RSA rsa = new RSA(key_size);
		NanoStopWatch timer = new NanoStopWatch();
		double[] results = new double[10];
		int i;
		double sum = 0,
				average;
		
		BigInteger input = new BigInteger(input_size, new Random());
		
		System.out.println("Initiating test unit with key size "+key_size+" and input size "+ input_size+"...");
		
		//Test for BigInteger
		for(i=0; i < results.length; i++){
			timer.startClock();
			rsa.encrypt(input);
			results[i]=timer.stopClock();
		}
		for(i=0; i < results.length; i++)
			sum += results[i];
		
		average = sum/results.length;
		
		System.out.println("Standart: \t"+average+" ns");
		
		//Test for RSABigInteger with SimplePower
		RSABigInteger simplePower = new RSABigInteger(input.toString());
		simplePower.modifyPowerStategy(new SimplePower());
		
		for(i=0; i < results.length; i++){
			timer.startClock();
			rsa.encrypt(simplePower);
			results[i]=timer.stopClock();
		}
		for(i=0; i < results.length; i++)
			sum += results[i];
		
		average = sum/results.length;
		
		System.out.println("SimplePower: \t"+average+" ns");
			
		//Test for RSABigInteger with SimplePower
		RSABigInteger squarePower = new RSABigInteger(input.toString());
		simplePower.modifyPowerStategy(new SquarePow());
		
		for(i=0; i < results.length; i++){
			timer.startClock();
			rsa.encrypt(squarePower);
			results[i]=timer.stopClock();
		}
		for(i=0; i < results.length; i++)
			sum += results[i];
		
		average = sum/results.length;
		
		System.out.println("SquarePower: \t"+average+" ns");
		System.out.println("--------------------------");
		
		//Test for RSABigInteger with Blinding
		blindTest(key_size, input);
		return null;
	}
	
	public void blindTest(int size, BigInteger msg){
		RSARandomGen keys = new RSARandomGen();
		keys.RSAGen(16);
		BigInteger n = keys.getModulus();
		BigInteger exp = keys.getPrivateKey();
		BigInteger e = keys.obtainLargeE();
		BigInteger r =  new BigInteger(16, new Random());
		BigInteger blindValue;
		BigInteger signature;
		
		/*System.out.println("n:" + n);
		System.out.println("exp:" + exp);
		System.out.println("e:" + e);
		System.out.println("r:" + r);*/
		
		//blindValue = r.modPow(e, n);
		//blindValue =msg.multiply(blindValue);
		blindValue= ((r.modPow(e,n)).multiply(msg)).mod(n);
		System.out.println("blindValue:" + blindValue);
		signature=blindValue.modPow(exp, n);
		System.out.println("Signature:" + signature);
		
		BigInteger s = r.modInverse(n).multiply(signature).mod(n);
		System.out.println("teste:" + s);
		System.out.println(s.modPow(e,n));
		
		System.out.println("mod pow:" + msg.modPow(exp, n));
	}
	
	public static void main(String[] args){
		
		/* WRITER
		try{
		
			File file = new File("test.out");
			
			if(!file.exists())
				file.createNewFile();
		
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			
			bw.write("HelloMotherfucker");
			bw.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
		*/
		
		RSATestSuite testSuite = new RSATestSuite();
		
		testSuite.performTest1(4, 32);
		testSuite.performTest1(8, 32);
	}
}
