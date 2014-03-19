import java.math.BigInteger;
import java.util.Random;

import utils.NanoStopWatch;
import utils.ResultsWriter;
import RSA.RSA;
import RSA.RSARandomGen;
import RSABigInteger.RSABigInteger;
import RSABigInteger.SquarePow;

public class RSATestSuite {
	
	private int sample_size = 1;
	
	public RSATestSuite(int sampling_size){
		this.sample_size = sampling_size;
	}
	
	public void setSamplingSize(int size){
		
		if(size > 0)
			this.sample_size = size;
	}
	
	/**
	 * Given a {@link RSA} instance and an input, it executes the encryption, sample_size times
	 * in order to get the average execution time
	 * @param rsa the encryption object
	 * @param input the input to be encrypted
	 * @param sample_size the number of samples taken
	 * @return the average execution time
	 */
	public double performUnitTest(RSA rsa, BigInteger input){
		
		int i;
		double sum=0;
		NanoStopWatch timer = new NanoStopWatch();
		
		for(i=0; i < this.sample_size; i++){
			timer.startClock();
			rsa.encrypt(input);
			sum+=timer.stopClock();
		}
		
		return sum/this.sample_size;
	}

	
	public double[] performTestBatteryFixedKey(RSA rsa, int input_size, int battery_size){
		
		int i;
		double sum = 0;
		double[] results = new double[4];
		Random rand = new Random(); 
		BigInteger[] inputPop = new BigInteger[battery_size];

		//Populate the sampling population
		for(i=0; i<battery_size; i++)
			inputPop[i] = new BigInteger(input_size, rand);
		
		//Standard
		for(i=0; i < battery_size; i++){
			sum+=performUnitTest(rsa, inputPop[i]);
		}
		results[0]=sum/battery_size;
		
		//Iterative Power
		RSABigInteger iterative;
		for(i=0, sum=0; i < battery_size; i++){
			iterative = new RSABigInteger(inputPop[i].toString());
			sum+=performUnitTest(rsa, iterative);
		}
		results[1]=sum/battery_size;
		
		//Square Power
		RSABigInteger square;
		for(i=0, sum=0; i < battery_size; i++){
			square = new RSABigInteger(inputPop[i].toString());
			square.modifyPowerStategy(new SquarePow());
			sum+=performUnitTest(rsa, square);
		}
		results[2]=sum/battery_size;
		
		//SquarePower with BlindingOn
		results[3]=0;
		
		return results;
	}
	
	
	/**
	 * Tests the execution of the different versions of the RSA execution, with different sized 
	 * inputs (32 to 1024 bits), and with a fixed key. 
	 * @param rsa
	 * @param battery_size 
	 */
	public void performTest1(RSA rsa, String file_title, int battery_size){
		
		BigInteger two = new BigInteger("2");
		int exp, i;
		String output = "Performing test1 with battery of "+battery_size+" samples...\n";
		double[] results = new double[4];
		
		for(i=0, exp=5; exp <= 10; exp++, i++){
			output += ""+i+") Testing all versions with inputs of size "+two.pow(exp).toString()+"\n";
			results = performTestBatteryFixedKey(rsa, two.pow(exp).intValue(), battery_size);
			output += "Standard: \t"	+results[0]	+"\t ns\n";
			output += "Iterative: \t"	+results[1]	+"\t ns\n";
			output += "Square Power: \t"+results[2]	+"\t ns\n";
			output += "RSA Blinding: \t"+results[3]	+"\t ns\n";
			output += "----------------------------------\n\n";
		}

		//Test for RSABigInteger with Blinding
		//blindTest(key_size, input);
		
		ResultsWriter.printResultsToFile(file_title, output);
		
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
		
		RSATestSuite testSuite = new RSATestSuite(1);
		RSA rsa = new RSA(8);
		testSuite.performTest1(rsa, "test1_keysize8", 10);
	}
}
