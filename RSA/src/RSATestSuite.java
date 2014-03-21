import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

import utils.NanoStopWatch;
import utils.ResultsWriter;
import RSA.RSA;
//import RSABigInteger.IterativeBigInteger; //TODO
import RSABigInteger.SquareBigInteger;

public class RSATestSuite {
	
	private int sample_size = 1;
	private int debug_lvl = 3;
	
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
		
		if(debug_lvl >= 3)
			System.out.print("\t\t[...");
		
		for(i=0; i < this.sample_size; i++){
			timer.startClock();
			rsa.encrypt(input);
			sum+=timer.stopClock();
			
			if(debug_lvl>=3){
				if(i == sample_size/4)
					System.out.print("...25%...");
				
				if(i == sample_size/2)
					System.out.print("...50%...");
				
				if(i==3*sample_size/4)
					System.out.print("...75%...");
			}
		}
		
		if(debug_lvl>=3)
			System.out.println("...]");
		
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
		if(debug_lvl>=2)
			System.out.println("\t Testing for BigInteger...");
		
		for(i=0; i < battery_size; i++){
			sum+=performUnitTest(rsa, inputPop[i]);
		}
		results[0]=sum/battery_size;
		
		//Iterative Power
		/*
		if(debug_lvl>=2)
			System.out.println("\t Testing for IterativeBigInteger...");
		
		IterativeBigInteger iterative;
		for(i=0, sum=0; i < battery_size; i++){
			iterative = new IterativeBigInteger(inputPop[i].toString());
			sum+=performUnitTest(rsa, iterative);
		}
		results[1]=sum/battery_size;
		*/
		
		//Square Power
		if(debug_lvl>=2)
			System.out.println("\t Testing for SquareBigInteger...");
		
		SquareBigInteger square;
		for(i=0, sum=0; i < battery_size; i++){
			square = new SquareBigInteger(inputPop[i].toString());
			sum+=performUnitTest(rsa, square);
		}
		results[2]=sum/battery_size;
		
		//SquarePower with BlindingOn
		if(debug_lvl>=2)
			System.out.println("\t Testing for RSA Blinding...");
		
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
		
		if(debug_lvl>=2){
			System.out.println("Percentile Required: "+percentile*100+"%");
			System.out.println("Enabled bits required: "+bitCount+" obtained "+standard.bitCount());
			System.out.println("Required size: "+input_size+" obtained "+standard.bitLength()+"\n");
		}
		
		
		//Adjust inputs to type
		//IterativeBigInteger iterative = new IterativeBigInteger(standard.toString()); //TODO 
		SquareBigInteger squarepow = new SquareBigInteger(standard.toString(2));
		SquareBigInteger blinding 	= new SquareBigInteger(standard.toString(2));
		
		//Test for each version
		results[0] = performUnitTest(rsa, standard);
		//results[1] = performUnitTest(rsa, iterative); //TODO
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
			
			if(debug_lvl >= 1)
				System.out.println(""+i+") Testing all versions with fixed with inputs of size "
						+two.pow(exp).toString()+"\n");
			
			output += ""+i+") Testing all versions with fixed with inputs of size "+two.pow(exp).toString()+"\n";
			results = performTestBatteryFixedKey(rsa, two.pow(exp).intValue(), battery_size);
			output += "Standard: \t"	+results[0]	+"\t ns\n";
			//output += "Iterative: \t"	+results[1]	+"\t ns\n"; //TODO
			output += "Square Power: \t"+results[2]	+"\t ns\n";
			output += "RSA Blinding: \t"+results[3]	+"\t ns\n";
			output += "----------------------------------\n\n";
			
			if(debug_lvl>=1)
				System.out.println("----------------------------------\n\n");
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
		
		int i, exp;
		RSA rsa;
		double[] results = new double[4];
		BigInteger two = new BigInteger("2");
		
		String output = "";
		output += "Performing test2 with battery of "+this.sample_size+" samples...\n";
		output += "[Input]: \t"+input.toString()+ "of size" + input.bitLength()+"\n";
		
		//
		//IterativeBigInteger iterative = new IterativeBigInteger(input.toString()); //TODO
		SquareBigInteger squarepow = new SquareBigInteger(input.toString());
		SquareBigInteger blinding 	= new SquareBigInteger(input.toString());
		
		for(i=0, exp=2; exp<=5; exp++, i++){
			
			int size = two.pow(exp).intValue();
			rsa = new RSA(size);
			
			if(debug_lvl>=1)
				System.out.println(""+i+") Testing all versions with key "+rsa.getPublicKey()
					+" of size "+rsa.getPublicKey().bitLength()+" out of "+size);
			
			output += ""+i+") Testing all versions with key"+rsa.getPublicKey();
			output += " of size"+rsa.getPublicKey().bitLength()+"\n";
			
			if(debug_lvl>=1)
				System.out.println("Testing for standard BigInteger...");
			results[0] = performUnitTest(rsa, input);
			
			/* TODO
			if(debug_lvl>=1)
				System.out.println("Testing for standard IterativeBigInteger...");
			results[1] = performUnitTest(rsa, iterative);
			*/
			
			if(debug_lvl>=1)
				System.out.println("Testing for standard SquareBigInteger...");
			results[2] = performUnitTest(rsa, squarepow);
			
			rsa.setBlinding();
			if(debug_lvl>=1)
				System.out.println("Testing for standard SquareBigInteger with RSABlinding...");
			results[3] = performUnitTest(rsa, blinding);
			rsa.setBlinding();
			
			
			output += "Standard: \t"	+results[0]	+"\t ns\n";
			//output += "Iterative: \t"	+results[1]	+"\t ns\n"; //TODO
			output += "Square Power: \t"+results[2]	+"\t ns\n";
			output += "RSA Blinding: \t"+results[3]	+"\t ns\n";
			output += "----------------------------------\n\n";
			
			if(debug_lvl>=1)
				System.out.println("----------------------------------\n\n");
			
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
			
			if(debug_lvl>=1)
				System.out.println("Testing all versions with "+percentiles[i]*100+"% active bits\n");
			
			output += "Testing all versions with "+percentiles[i]*100+"% active bits\n";
			output += "Standard: \t"	+results[0]	+"\t ns\n";
			//output += "Iterative: \t"	+results[1]	+"\t ns\n"; //TODO
			output += "Square Power: \t"+results[2]	+"\t ns\n";
			output += "RSA Blinding: \t"+results[3]	+"\t ns\n";
			output += "----------------------------------\n\n";
			
			if(debug_lvl>=1)
				System.out.println("----------------------------------\n\n");
		}
		
		ResultsWriter.printResultsToFile(file_title, output);
	}
	
	public void setDebugLevel(int lvl){
		this.debug_lvl = lvl;
	}
		
	public static void main(String[] args){
		
		RSATestSuite testSuite = new RSATestSuite(10);
		
		Scanner reader = new Scanner(System.in);
		int in, key_size, input_size, battery_size;
		String filename;
		String user_info = "";
		
		user_info += "\n\n[1] Perform test 1 - Test with a fixed key, inputs with different size (32bits to 1024)\n";
		user_info += "[2] Perform test 2 - Test with a fixed input, different sized keys\n";
		user_info += "[3] Perform test 3 - Test with a fixed key, inputs with different percentages of set bits.\n";
		user_info += "[4] Change default sample size\n";
		user_info += "[5] Change verbose level\n";
		user_info += "[0] Exit";
		
		System.out.println("NOTE: This version will ignore the iterative implementation of modPow.\n");
		
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
			case 4:
				System.out.print("Set sample size to:");
				battery_size = reader.nextInt();
				testSuite.setSamplingSize(battery_size);
				break;
			case 5:
				String debug_info="";
				debug_info+="[0] No debugging info\n";
				debug_info+="[1] Low debugging info\n";
				debug_info+="[2] Average debugging info\n";
				debug_info+="[3] High debugging info\n";
				debug_info+="Set debugging info to:";
				System.out.print(debug_info);
				int lvl = reader.nextInt();
				testSuite.setDebugLevel(lvl);
			default:
				break;
			}
		}
	
	}
	
}
