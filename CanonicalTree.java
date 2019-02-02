package io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class CanonicalTree {
	
	public CanonicalTree(List<Symbol> symbols){
		//assigns codes to each symbol 
		symbols.get(0).setCode(0);
		for(int i=1; i<symbols.size(); i++){
			symbols.get(i).setCode(symbols.get(i-1).getCode() + 1);
			if(symbols.get(i).getLength()>symbols.get(i-1).getLength()){
				int dif = symbols.get(i).getLength()-symbols.get(i-1).getLength();
				symbols.get(i).setCode(symbols.get(i).getCode()<<dif);
			}
		}	
	}
	
	//decodes by matching length and code to symbol read
	public OutputStream decode(List<Symbol> symbolList, String out, int symbols, InputStreamBitSource stream) throws IOException, InsufficientBitsLeftException {
		OutputStream output = new FileOutputStream(out);
		int decoded=0;
		int tempCode=0;
		int length=0;
		while(decoded<symbols){
			tempCode=tempCode<<1;
			tempCode=tempCode|stream.next(1);
			length++;
			for(int i=0;i<255;i++){
				if(tempCode==symbolList.get(i).getCode() && length==symbolList.get(i).getLength()){
					output.write(symbolList.get(i).getSymbol());
					tempCode=0;
					length=0;
					decoded++;
				}
			}
		}
		return output;
	}
	
	
}
