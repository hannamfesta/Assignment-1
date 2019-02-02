package io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.math.*;

public class HuffmanEncoder {
	
	private List<Symbol> _symbolList;
	private List<SymbolNode> _symbolProbList;
	private int[] _symbolsRead;
	
	public HuffmanEncoder(InputStream stream) throws IOException{
		
		_symbolList = new ArrayList<Symbol>();
		_symbolProbList = new ArrayList<SymbolNode>();
		for(int i=0; i<256; i++){
			Symbol temp = new Symbol();
			temp.setSymbol(i);
			_symbolList.add(temp);
			_symbolProbList.add(new SymbolNode(temp));
		}
		
		int[] counts = new int[256];
		BigDecimal[] Counts = new BigDecimal[256];
		int total = stream.available();
		_symbolsRead = new int[total];
		BigDecimal Total = new BigDecimal(total);
		int i=0;
		while(i<total){
			_symbolsRead[i] = stream.read();
			for(int j=0; j<256; j++){
				if(_symbolsRead[i]==j){
					counts[j]++;
				}
			}
			i++;
		}
		
		for(int j=0;j<256;j++){
			Counts[j] = new BigDecimal(counts[j]);
		}
		
		for(int j=0; j<256; j++){
			_symbolProbList.get(j).setProb(Counts[j].divide(Total,50, RoundingMode.HALF_UP));
		}
		
		//probabilities sorted smallest to largest 
		Collections.sort(_symbolProbList);
		
	}
	
	public OutputStream encode(String out) throws IOException{
		OutputStream output = new FileOutputStream(out);
		OutputStreamBitSink bitstream = new OutputStreamBitSink(output);
		
		//assign lengths to _symbolList using probability ordering from _symbolProbList
		/* while loop - go while the list has >1 symbol in it 
		 * 		remove 2 nodes with lowest prob - give them common parent with sum of probs then add that to list and re-sort
		 * 			node with lower prob or index becomes left child, other is right child
		 * 				- internal nodes will always have 2 children  
		 * 				- until you reach leaf - length of children added +1 from parent 
		 * 	then assign lengths to the _symbolList based on their symbol 
		 * 	sort _symbolList based on lengths 
		 * */
		
		List <SymbolNode> leaves = new ArrayList<SymbolNode>();
		while(_symbolProbList.size()>1){
			SymbolNode leftChild = _symbolProbList.remove(0);
			if(leftChild.getLeaf()){
				leaves.add(leftChild);
			}
			SymbolNode rightChild = _symbolProbList.remove(0);
			if(rightChild.getLeaf()){
				leaves.add(rightChild);
			}
			SymbolNode current = new SymbolNode(leftChild.getProb().add(rightChild.getProb(), new MathContext(50, RoundingMode.HALF_UP)), rightChild, leftChild, Math.max(leftChild.getHeight(),rightChild.getHeight())+1);
			_symbolProbList.add(current);
			rightChild.setParent(current);
			leftChild.setParent(current); 
			rightChild.setHasPar(true);
			leftChild.setHasPar(true);
			
			current.incrementLengths();
			
			Collections.sort(_symbolProbList);
		}

		
		//setting symbolList lengths by what symbol it is
		for(int i=0; i<256; i++){
			_symbolList.get(leaves.get(i).getSymbol()).setLength(leaves.get(i).getLength());
			_symbolList.get(leaves.get(i).getSymbol()).setProb(leaves.get(i).getProb());
		}
		
		//sort symbols by lengths 
		Collections.sort(_symbolList);

		//send _symbolList into canonical tree to construct codes
		CanonicalTree tree = new CanonicalTree(_symbolList);

		//send lengths to output - have to rearrange so its in symbol order
		for(int i=0; i<256; i++){
			Symbol temp = _symbolList.get(i);
			for(int j=0; j<256; j++){
				if(_symbolList.get(j).getSymbol()==i){
					_symbolList.set(i, _symbolList.get(j));
					_symbolList.set(j, temp);
				}
			}
		}
		
		for(int i=0; i<256; i++){
			bitstream.write(_symbolList.get(i).getLength(), 8);
		}
		
		//amount of symbols sent to input
		bitstream.write(_symbolsRead.length, 32);
		
		//compressed entropy calculation
		double compressedEnt = 0;
		for(int j=0; j<256; j++){
			if(_symbolList.get(j).getProb().compareTo(new BigDecimal(0))==1){
				compressedEnt += _symbolList.get(j).getProb().doubleValue()*_symbolList.get(j).getLength();
			}
		}
		System.out.println("New Compressed Entropy = " + compressedEnt + " bits/symbol");
		
		//write codes to output in order based on the symbol found 
		for(int i=0; i<_symbolsRead.length; i++){
			for(int j=0; j<256; j++){
				if(_symbolsRead[i] == _symbolList.get(j).getSymbol()){
					bitstream.write(_symbolList.get(j).getCode(), _symbolList.get(j).getLength());
				}
			}		
		}
		
		bitstream.padToWord(); //pads the rest of the output if codewords do not fill to word length 
		return output;
	}
}
