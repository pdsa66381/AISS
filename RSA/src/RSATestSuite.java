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
		double 	sum = 0,
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
		sum=0;
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
		sum=0;
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
		return null;
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
		testSuite.performTest1(16, 32);
		testSuite.performTest1(24, 32);
		testSuite.performTest1(32, 32);
	}
}
