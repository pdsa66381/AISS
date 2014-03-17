import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

import RSABigInteger.RSABigInteger;
import RSABigInteger.SquarePow;

public class RSATestSuite {
	
	
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
		
		
		NanoStopWatch stopWatch = new NanoStopWatch();
		
		RSA rsa = new RSA(8);
		
		BigInteger in1 = new BigInteger(8, new Random());
		RSABigInteger in2 = new RSABigInteger(in1.toString());
		RSABigInteger in3 = new RSABigInteger(in1.toString());
		in3.modifyPowerStategy(new SquarePow());
		
		BigInteger ciphertext;
		double elapsed_time;
		
		stopWatch.startClock();
		ciphertext = rsa.encrypt(in1);
		elapsed_time=stopWatch.stopClock();
		System.out.println("BigInteger cipher:\t"+ciphertext+" - Elapsed Time (ns):"+elapsed_time);
		
		stopWatch.startClock();
		ciphertext = rsa.encrypt(in2);
		elapsed_time=stopWatch.stopClock();
		System.out.println("RSABigInteger cipher:\t"+ciphertext+" - Elapsed Time (ns):"+elapsed_time);
		
		stopWatch.startClock();
		ciphertext = rsa.encrypt(in2);
		elapsed_time=stopWatch.stopClock();
		System.out.println("RSABigInteger(square) cipher:\t"+ciphertext+" - Elapsed Time (ns):"+elapsed_time);
		
	}
}
