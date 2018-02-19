package chessBishop;
import java.util.HashMap;


public class Combination {
	
	int len;
	int combination[][];
	
	public Combination(int len){
		
		this.len = len;
		combination = new int[this.len][2];
		
	}
	
	public void setLength(int length){
		this.len = length;	
		
	}
	
	public int getLength(){
		return this.len;
		
	}
	
	public int[][] getCombination(){
		return combination;	
	}
	
	public void appendPos(int pos,int rowVal,int colVal){
		
		if (pos < len){
			combination[pos][0] = rowVal;
			combination[pos][1] = colVal;
		}	
	}
	
	

	public void getFromHashMap(HashMap<Integer,Integer> hm){
		int row,col;
		for(int key:hm.keySet()){
			col = key;
			row = hm.get(key);
			combination[col][0] = row;
			combination[col][1] = col;	
		}
		
	}
	
	public void getFromMatrix(int matrix[][]){
    	
    	int pos = 0;
    	for(int i = 0; i < matrix.length; i++)
    		for(int j= 0; j < matrix.length; j++)
    			if (matrix[i][j] == 1) {
    				combination[pos][0] = i;
    				combination[pos][1] = j;
    				pos++;
    			}	
    }
	
	void printCombination(){
		for(int i = 0; i < this.len; i++){
			for (int j = 0; j < 2; j++)
				System.out.print(combination[i][j] + " ");
			System.out.println("");
		}
		
	}
	
      

}
