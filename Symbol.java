package io;

import java.math.BigDecimal;

public class Symbol implements Comparable {

	protected int _length;
	protected int _symbol;
	protected int _code;
	protected BigDecimal _prob;
	
	public Symbol(){
		_length = 0;
		_symbol = 0;
		_code = 0;
		_prob = new BigDecimal(0);
	}
	
	public int compareTo(Object o) {
		Symbol s = (Symbol)o;
		if(this._length!=0){
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
		}else{
			if(this._prob.compareTo(s._prob)==-1){
				return -1;
			}else if(this._prob.compareTo(s._prob)==1){
				return 1;
			}else{
				if(this._symbol<s._symbol){
					return -1;
				}else{
					return 1;
				}
			}
		}
	}
	
	public int getLength(){
		return _length;
	}
}
