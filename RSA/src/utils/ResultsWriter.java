package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ResultsWriter {


	public static void printResultsToFile(String title, String contents){
		
		try{
			
			File file = new File(title+".out");
			
			if(!file.exists())
				file.createNewFile();
		
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			
			bw.write(contents);
			bw.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
