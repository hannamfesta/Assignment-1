package io;

import java.math.BigDecimal;

public class SymbolNode extends Symbol {
		private SymbolNode _parent;
		private SymbolNode _rightChild; 
		private SymbolNode _leftChild;
		private boolean _leaf;
		private boolean _hasParent;
		private int _height;
		
		public SymbolNode(BigDecimal p, SymbolNode r, SymbolNode l, int height){
			super();
			_rightChild = r;
			_leftChild = l;
			_leaf = false;
			this.setProb(p);
			_hasParent = false;
			_height = height;
		}
		
		public SymbolNode(Symbol s){
			super();
			this.setSymbol(s.getSymbol());
			this.setProb(s.getProb());
			_leaf = true;
			_hasParent = false;
			_height = 0;
		}
		
		public boolean incrementLengths(){
			if(this._leaf){
				this.setLength(this._parent.getLength()+1);
				return true;
			}
			
			if(this._hasParent){
				this.setLength(this._parent.getLength()+1);
			}
				this._leftChild.incrementLengths();
				this._rightChild.incrementLengths();
				return true;
		}
		
		public int compareTo(Object o) {
			SymbolNode s = (SymbolNode)o;
			if(this.getProb().compareTo(s.getProb())==-1){
				return -1;
			}else if(this.getProb().compareTo(s.getProb())==1){
				return 1;
			}else{
				//break ties by height
				if(this._height>s._height){
					return 1;
				}else if(this._height<s._height){
					return -1;
				}
				return 0;
			}
		}
		
		public SymbolNode getParent(){
			return _parent;
		}
		
		public void setParent(SymbolNode s){
			_parent = s; 
		}
		
		public SymbolNode getRight(){
			return _rightChild;
		}
		
		public void setRight(SymbolNode s){
			_rightChild = s;
		}
		
		public SymbolNode getLeft(){
			return _leftChild;
		}
		
		public void setLeft(SymbolNode s){
			_leftChild = s;
		}
		
		public boolean getLeaf(){
			return _leaf;
		}
		
		public void setLeaf(boolean l){
			_leaf = l;
		}
		
		public boolean getHasPar(){
			return _hasParent;
		}
		
		public void setHasPar(boolean h){
			_hasParent = h;
		}
		
		public int getHeight(){
			return _height;
		}
		
		public void setHeight(int x){
			_height = x;
		}
		
}
