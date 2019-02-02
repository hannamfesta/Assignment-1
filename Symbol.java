package io;

import java.math.BigDecimal;

public class Symbol implements Comparable {
	private int _length;
	private int _symbol;
	private int _code;
	private BigDecimal _prob;
	
	public Symbol(){
		_length = 0;
		_symbol = 0;
		_code = 0;
		_prob = new BigDecimal(0);
	}
	
	public int compareTo(Object o) {
		Symbol s = (Symbol)o;
			if(this._length<s._length){
				return -1;
			} else if(this._length>s._length){
				return 1;
			}else {
				if(this._symbol<s._symbol){
					return -1;
				}else{
					return 1;
				}
			}
			
	}
	
	public int getLength(){
		return _length;
	}
	
	public void setLength(int x){
		_length = x;
	}
	
	public int getSymbol(){
		return _symbol;
	}
	
	public void setSymbol(int x){
		_symbol = x;
	}
	
	public int getCode(){
		return _code;
	}
	
	public void setCode(int x){
		_code = x;
	}
	
	public BigDecimal getProb(){
		return _prob;
	}
	
	public void setProb(BigDecimal x){
		_prob = x;
	}
}
