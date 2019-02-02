package tests;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.math.*;

import io.Decoder;
import io.HuffmanEncoder;
import io.InsufficientBitsLeftException;

public class Huffman {

	public static void main(String[] args) throws IOException, InsufficientBitsLeftException {
		
		/*******Part 1*******/
		InputStream stream = new FileInputStream("compressed.dat");
		Decoder code = new Decoder(stream);
		code.decode("output.dat");
		
		/*******Part 2*******/
		InputStream stream2 = new FileInputStream("output.dat");
		HuffmanEncoder code2 = new HuffmanEncoder(stream2);
		code2.encode("compressed2.dat");
		
		//testing encoder
		InputStream stream3 = new FileInputStream("compressed2.dat");
		Decoder code3 = new Decoder(stream3);
		code3.decode("output2.dat");
		
		/*******Part 3*******/	
		InputStream stream4 = new FileInputStream("output.dat");
		int[] counts = new int[256];
		BigDecimal[] Counts = new BigDecimal[256];
		BigDecimal[] probs = new BigDecimal[256];
		double[] info = new double[256];
		int total = stream4.available();
		BigDecimal Total = new BigDecimal(total);
		int i=0;
		int temp;
		while(i<total){
			temp = stream4.read();
			for(int j=0; j<256; j++){
				if(temp==j){
					counts[j]++;
				}
			}
			i++;
		}
		for(int j=0;j<256;j++){
			Counts[j] = new BigDecimal(counts[j]);
		}
		
		//Question 2
		List<Integer> used = new ArrayList<Integer>();
		for(int j=0; j<256; j++){
			probs[j] =Counts[j].divide(Total,15, RoundingMode.HALF_UP);
			byte b = (byte) j;
			System.out.println("P("+(char)b+") = "+probs[j]);
			double temp2 = probs[j].doubleValue();
			info[j] = -(Math.log10(temp2)/Math.log10(2.0));
			if(probs[j].compareTo(new BigDecimal(0))==1){
				used.add(j);
			}
		}
		
		//Question 3 - Theoretical Entropy of Data
		double entropy = 0;
		for(int j=0; j<256; j++){
			if(used.contains(j)){
				entropy += probs[j].doubleValue()*info[j];
			}
		}
		System.out.println("Theoretical Entropy = " + entropy+ " bits/symbol"); //4.531702948915821
		
		//Question 4 - Original Compressed Entropy 
		double compressedEnt = 0;
		for(int j=0; j<256; j++){
			if(used.contains(j)){
				compressedEnt += probs[j].doubleValue()*code.getList().get(j).getLength();
			}
		}
		System.out.println("Compressed Entropy from Original Compressed File = " + compressedEnt + " bits/symbol"); //14.157000445223586
	}
}
