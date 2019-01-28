package io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class CanonicalTree {
	
	public CanonicalTree(List<Symbol> symbols){
		symbols.get(0)._code = 0;
		for(int i=1; i<symbols.size(); i++){
			symbols.get(i)._code = symbols.get(i-1)._code + 1;
			if(symbols.get(i)._length>symbols.get(i-1)._length){
				int dif = symbols.get(i)._length-symbols.get(i-1)._length;
				symbols.get(i)._code = symbols.get(i)._code<<dif;
			}
		}	
	}
	
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
				if(tempCode==symbolList.get(i)._code && length==symbolList.get(i)._length){
					output.write(symbolList.get(i)._symbol);
					tempCode=0;
					length=0;
					decoded++;
				}
			}
		}
		return output;
	}
	
}
