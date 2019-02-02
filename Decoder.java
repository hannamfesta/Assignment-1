package io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Decoder {
	
	private InputStreamBitSource _bitStream;
	private List<Symbol> _symbolList; //lengths of codes
	private int _symbols; //# of symbols
	private CanonicalTree _tree;
	
	public Decoder(InputStream stream) throws IOException, InsufficientBitsLeftException {
	
		_symbolList = new ArrayList<Symbol>();
		_bitStream = new InputStreamBitSource(stream);
		
		//read in lengths
		for(int i=0; i<256; i++){
			Symbol temp = new Symbol();
			temp.setLength(_bitStream.next(8));
			temp.setSymbol(i);
			_symbolList.add(temp);
		}
		
		int num[] = new int[4];
		_symbols=0;
		
		//read in number of symbols 
		for(int i=0; i<4; i++){
			int temp = _bitStream.next(8); 
			num[i] = temp<<8*(4-i-1);
			_symbols += num[i];
		}
		
		//sort list by lengths 
		Collections.sort(_symbolList);
		
		//add symbols to canonical tree
		_tree = new CanonicalTree(_symbolList);
		
	}
	
	public OutputStream decode(String out) throws IOException, InsufficientBitsLeftException{
		return _tree.decode(_symbolList, out, _symbols, _bitStream);
	}
	
	public List<Symbol> getList(){
		return _symbolList;
	}
	
}
