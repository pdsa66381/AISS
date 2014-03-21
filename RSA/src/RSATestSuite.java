import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

import utils.NanoStopWatch;
import utils.ResultsWriter;
import RSA.RSA;
import RSABigInteger.IterativeBigInteger;
import RSABigInteger.SquareBigInteger;

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
		IterativeBigInteger iterative;
		for(i=0, sum=0; i < battery_size; i++){
			iterative = new IterativeBigInteger(inputPop[i].toString());
			sum+=performUnitTest(rsa, iterative);
		}
		results[1]=sum/battery_size;
		
		//Square Power
		SquareBigInteger square;
		for(i=0, sum=0; i < battery_size; i++){
			square = new SquareBigInteger(inputPop[i].toString());
			sum+=performUnitTest(rsa, square);
		}
		results[2]=sum/battery_size;
		
		//SquarePower with BlindingOn
		SquareBigInteger blindingSquare;
		rsa.setBlinding();
		for(i=0, sum=0; i < battery_size; i++){
			blindingSquare = new SquareBigInteger(inputPop[i].toString());
			sum+=performUnitTest(rsa, blindingSquare);
		}
		rsa.setBlinding();
		results[3]=sum/battery_size;;
		
		return results;
	}
	
	public double[] performTestUnitByPercentage(RSA rsa, int input_size, double percentile){
		
		Random rnd = new Random();
		BigInteger 	standard = BigInteger.ZERO,
					two = new BigInteger("2");
		int bitCount = (int) (percentile*(input_size));
		double[] results = new double[4];
		
		//Adjust to input_size according to the percentile
		if(percentile > 0){
			standard = standard.flipBit(input_size-1);
			
			if(percentile == 1)
				standard = (two.pow(input_size)).subtract(BigInteger.ONE);
			else{
				//Add bits until bit count matches the percentile
				while(standard.bitCount()<bitCount){
					
					int index;
					
					do{
						index = rnd.nextInt(input_size-1);
					}while(standard.testBit(index));
					
					standard = standard.flipBit(index);
				}
			}
		}
		
		System.err.println("Percentile Required: "+percentile*100+"%");
		System.err.println("Enabled bits required: "+bitCount+" obtained "+standard.bitCount());
		System.err.println("Required size: "+input_size+" obtained "+standard.bitLength()+"\n");
		
		
		//Adjust inputs to type
		IterativeBigInteger iterative = new IterativeBigInteger(standard.toString());
		SquareBigInteger squarepow = new SquareBigInteger(standard.toString(2));
		SquareBigInteger blinding 	= new SquareBigInteger(standard.toString(2));
		
		//Test for each version
		results[0] = performUnitTest(rsa, standard);
		results[1] = performUnitTest(rsa, iterative);
		results[2] = performUnitTest(rsa, squarepow);
		
		rsa.setBlinding();
		results[3] = performUnitTest(rsa, blinding);
		rsa.setBlinding();
		
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
		String output = "";
		double[] results = new double[4];
		
		output += "Performing test1 with battery of "+battery_size+" samples...\n";
		output += "[PublicKey]: \t"+rsa.getPublicKey()+" of size "+rsa.getPublicKey().bitLength()+"bits\n";
		
		for(i=0, exp=5; exp <= 10; exp++, i++){
			output += ""+i+") Testing all versions with fixed with inputs of size "+two.pow(exp).toString()+"\n";
			results = performTestBatteryFixedKey(rsa, two.pow(exp).intValue(), battery_size);
			output += "Standard: \t"	+results[0]	+"\t ns\n";
			output += "Iterative: \t"	+results[1]	+"\t ns\n";
			output += "Square Power: \t"+results[2]	+"\t ns\n";
			output += "RSA Blinding: \t"+results[3]	+"\t ns\n";
			output += "----------------------------------\n\n";
		}

		ResultsWriter.printResultsToFile(file_title, output);
	}
	
	/**
	 * 
	 * @param input
	 * @param file_title
	 * @param battery_size
	 */
	public void performTest2(BigInteger input, String file_title){
		
		int i, size;
		RSA rsa;
		double[] results = new double[4];
		
		String output = "";
		output += "Performing test2 with battery of "+this.sample_size+" samples...\n";
		output += "[Input]: \t"+input.toString()+ "of size" + input.bitLength()+"\n";
		
		//
		IterativeBigInteger iterative = new IterativeBigInteger(input.toString());
		SquareBigInteger squarepow = new SquareBigInteger(input.toString());
		SquareBigInteger blinding 	= new SquareBigInteger(input.toString());
		
		for(i=0, size=0; size<= 32; size+=size, i++){
			rsa = new RSA(size);
			output += ""+i+") Testing all versions with key"+rsa.getPublicKey();
			output += " of size"+rsa.getPublicKey().bitLength()+"\n";
			System.err.println(rsa.getPublicKey().bitLength());
			results[0] = performUnitTest(rsa, input);
			results[1] = performUnitTest(rsa, iterative);
			results[2] = performUnitTest(rsa, squarepow);
			rsa.setBlinding();
			results[3] = performUnitTest(rsa, blinding);
			rsa.setBlinding();
			output += "Standard: \t"	+results[0]	+"\t ns\n";
			output += "Iterative: \t"	+results[1]	+"\t ns\n";
			output += "Square Power: \t"+results[2]	+"\t ns\n";
			output += "RSA Blinding: \t"+results[3]	+"\t ns\n";
			output += "----------------------------------\n\n";
		}
		
		ResultsWriter.printResultsToFile(file_title, output);
	}
	
	/**
	 * Test the execution of the different versions of the RSA execution, with inputs, of size
	 * input_bit_size, with different percentages of bits set to one, in order to study the
	 * overall pattern exposure.
	 * @param rsa
	 * @param input_bit_size
	 * @param sample_size
	 */
	public void performTest3(RSA rsa, int input_bit_size, String file_title){
		
		String output="";
		double[] results;
		double[] percentiles = {0, 0.2, 0.4, 0.5, 0.6, 0.75, 1.0};
		
		output += "Performing test3 with battery of "+sample_size+" samples...\n";
		output += "[PublicKey]: \t"+rsa.getPublicKey()+" of size "+rsa.getPublicKey().bitLength()+"bits\n";
		
		for(int i=0; i < percentiles.length; i++){
			results = performTestUnitByPercentage(rsa, input_bit_size, percentiles[i]);
			output += "Testing all versions with "+percentiles[i]*100+"% active bits\n";
			output += "Standard: \t"	+results[0]	+"\t ns\n";
			output += "Iterative: \t"	+results[1]	+"\t ns\n";
			output += "Square Power: \t"+results[2]	+"\t ns\n";
			output += "RSA Blinding: \t"+results[3]	+"\t ns\n";
			output += "----------------------------------\n\n";
		}
		
		ResultsWriter.printResultsToFile(file_title, output);
	}
	

		
	public static void main(String[] args){
		
		RSATestSuite testSuite = new RSATestSuite(1);
		
		Scanner reader = new Scanner(System.in);
		int in, key_size, input_size, battery_size;
		String filename;
		String user_info = "";
		
		user_info += "[1] Perform test 1 - Test with a fixed key, inputs with different size (32bits to 1024)\n";
		user_info += "[2] Perform test 2 - Test with a fixed input, different sized keys\n";
		user_info += "[3] Perform test 3 - Test with a fixed key, inputs with different percentages of set bits.\n";
		user_info += "[0] Exit";
		
		while(true){
			
			System.out.println(user_info);
			in = reader.nextInt();
			
			switch (in) {
			case 0:
				reader.close();
				return;
			case 1:
				System.out.println("Key size (bits):");
				key_size = reader.nextInt();
				System.out.println("Number of tests:");
				battery_size = reader.nextInt();
				System.out.println("Save results as (test1_keysize.out):");
				filename = reader.next();
				testSuite.performTest1(new RSA(key_size), filename, battery_size);
				break;
			case 2:
				System.out.println("Input size (bits):");
				input_size = reader.nextInt();
				System.out.println("Number of tests:");
				battery_size = reader.nextInt();
				System.out.println("Save results as (test2_):");
				filename = reader.next();
				testSuite.setSamplingSize(battery_size);
				testSuite.performTest2(new BigInteger(input_size, new Random()), filename);
				break;
			case 3:
				System.out.println("Key size (bits):");
				key_size = reader.nextInt();
				System.out.println("Input size (bits):");
				input_size = reader.nextInt();
				System.out.println("Number of tests:");
				battery_size = reader.nextInt();
				System.out.println("Save results as (test3_):");
				filename = reader.next();
				testSuite.setSamplingSize(battery_size);
				testSuite.performTest3(new RSA(key_size), input_size, filename);
				break;
			default:
				break;
			}
		}
	
	}
	
}
