package chessBishop;
import java.util.HashMap;


public class NBishops {
	
	private int dimension;
	private int board[][];
	private int nums = 1;
	private int combNum;
	private int fullCombNum;
	Combination[] combinations;
	Combination[] maxCombinations;
	private HashMap<Integer,Integer>positionMap = new HashMap<Integer,Integer>(); 
	private int count = 0;
	private int countFull = 0;

	
	public NBishops(int dim){
		dimension = dim;
		board =  new int[dimension][dimension];
		setCombinationNum(dimension);
		combinations = new Combination[combNum];
		maxCombinations = new Combination[fullCombNum];
		
	}
	
	public int getCombNum(){
		return combNum;
	}
	public int getFullCombNum(){
		return fullCombNum;
	}
	
	/*
	 * Odredjuje broj minimalnih i potpunih kombinacija
	 * 
	 */
	public void setCombinationNum(int dim){
		
		switch(dim){
		
			case 1:
				combNum = 1;
				fullCombNum = 1;
				break;
			case 2:
				combNum = 2;
				fullCombNum = 2;
				break;
			case 3:
				combNum = 5;
				fullCombNum = 3;
				break;
			case 4:
				combNum = 24;
				fullCombNum = 15;
				break;
			case 5:
				combNum = 125;
				fullCombNum = 27;
				break;
			default:
				combNum = 1000;
				fullCombNum = 50;
		}
		
	}
	
	public void setdimension(int dimension){
		this.dimension = dimension;
	}
	
	public int getDimension(){
		
		return dimension;
	}
	
	/*
	 * Incijalizuje tablu i nizove kombinacija
	 * 
	 */
	public void initBoard(){
		
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++)
				board[i][j] = 0;
	
		for(int i = 0; i < combinations.length;i++)
			combinations[i] = new Combination(dimension);
		
		if (dimension == 1) maxCombinations[0] = new Combination(1);
		else{
			for(int i = 0; i < maxCombinations.length;i++)
				maxCombinations[i] = new Combination(2*dimension-2);
		}
	}
	
	public void initBoard(int table[][]){
		
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++)
				table[i][j] = 0;
	}
	


	/*
	 * ova metoda vraca true ako je pozicija lovca bezbedna - ako nema lovca koja moze da ga pojede
	 */
	public boolean isSafe(int row, int col,int b[][]){
		int row_pos,col_pos;
		if (row >= dimension || col >= dimension)
			return false;
		for(row_pos = row, col_pos = col; row_pos >= 0 && col_pos >= 0; row_pos--, col_pos--)
			if (b[row_pos][col_pos] == 1)				
				return false;
		
		for(row_pos = row, col_pos = col; row_pos < dimension && col_pos < dimension; row_pos++, col_pos++)
			if (b[row_pos][col_pos] == 1)				
				return false;
		
		for(row_pos = row, col_pos = col; row_pos >=  0 && col_pos < dimension; row_pos--, col_pos++)
			if (b[row_pos][col_pos] == 1)				
				return false;
		
		for(row_pos = row, col_pos = col; row_pos < dimension && col_pos >= 0; row_pos++, col_pos--)
			if (b[row_pos][col_pos] == 1 )				
				return false;
		
		return true;	
		
		
	}
	
	/*
	 * 
	 * Pomocne funkcije za kopiranje nizova u kombinacije i
	 * kopiranje nizova
	 */
	void copymaxCombinations(int board[][],int iter){
		int count = 0;
		for(int i = 0; i < dimension;i++)
			for(int j = 0; j < dimension;j++)
				if(board[i][j] == 1) maxCombinations[iter].appendPos(count++,i,j);
		
	}
	
	
	void copy(int a[][],int b[][]){
		
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension;j++)
				a[i][j] = b[i][j];
	}
	
	/*
	 * Printing metode
	 * 
	 */
	public void printSolution(int arr[][]){
		System.out.println("\n");
		System.out.println("Resenje br. " + nums++);
		for(int i = 0; i < dimension; i++){
			for(int j = 0; j < dimension; j++)
				System.out.print(arr[i][j] + " ");
			System.out.println("");
		}	
		
	}
	
	public void printCombinations(){
		for(int i = 0; i < combinations.length;i++){
			combinations[i].printCombination();
			System.out.println();
		}
	}
	
	
	void printFullComb(){
		
		for(int i = 0; i < maxCombinations.length; i++){
			maxCombinations[i].printCombination();
			System.out.println("");
		}
			
	}
	
	/*
	 * Proverava da li je kombinacija ponovljena
	 * 
	 */
	public boolean isUnique(int matrix[][]){
		
		int copy[][] = new int[dimension][dimension];
		for(int i = 0; i < dimension; i++){
			copy[0][i] = 1;
			copy[dimension-1][i] = 1;
			
		}
		
		copy[dimension-1][0] = copy[dimension-1][dimension-1] = 0;
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++)
				if(matrix[i][j] != copy[i][j]) 
					return true;

		
		return false;
		
		
	}
	/*
	 * Dopunjuje minimalno resenje do maksimalnog
	 * 
	 */
	public void updateFull(){
		int arr[][] = new int[dimension][dimension];
		initBoard(arr);
		copy(arr,board);
		if (dimension < 3){
			copymaxCombinations(arr,countFull);
			maxCombinations[countFull].getFromMatrix(arr);
			countFull++;
			return;
		}
		int count = 0;
		for(int i = dimension-1; i >= 0; i--)
			for(int j = dimension-1; j >= 0; j--)
				if (isSafe(i,j,arr)) {
					count++;
					arr[i][j] = 1;
					if (count == dimension-2 && (isUnique(arr) || countFull == 0)){
						copymaxCombinations(arr,countFull);
						maxCombinations[countFull].getFromMatrix(arr);
						countFull++;
						return;
					}
				}	
	}
	
	/*
	 * Pomocne getter metode
	 */
	public Combination[] getMaxCombinations(){
		
		return maxCombinations;
		
	}

	public Combination[] getCombinations(){
		
		return combinations;

	}
	
	
	
	/*
	 * Resava postavljanje lovaca - prvu polovinu od combNum kombinacija
	 */
	boolean solveNQUtil(int col)
	{
	    /* ako su sve kolone pregledane, to je kraj,
	    vrati true */
	    if (col == dimension ){
	    	
	    	
	    	combinations[count].getFromHashMap(positionMap);
	    	count++;
	    	
	        return true;
	    }
	 
	    /* probaj za sve redove u trenutnu kolonu da postavis */
	    /*prolaz kroz celu tablu, dok ne prodje celu tablu ili ne skupi 
	    combNum/2 resenja; krecemo otpocetka*/
	    for (int i = 0; i < dimension && count < combNum/2; i++)
	    {
	        /* ako moze da se postavi na ovu poziciju */
	        if ( isSafe(i, col,board) ){
	            /* postavi lovca na board[i][col] */
	            board[i][col] = 1;
	            //sacuvaj tu poziciju
	            positionMap.put(col, i);
	            
	            /* rekurzivno ispitaj za sledecu kolonu */
	            solveNQUtil(col + 1) ;
	 
	           
	 
	            /* Ako postavljanje na board[i][col]
	            ne vodi ka resenju, vrati se i skini lovca sa pozicije
	            board[i][col]*/
            board[i][col] = 0; // BACKTRACK
            positionMap.remove(col);
            
	        }
	    }
	 
	    /* Ako lovca ne mozemo da postavimo ni u jedan red-kolonu, vrati false */
	    return false;
	}
	
	
	/*
	 * Ovo postavlja drugu polovinu od combNum kombinacija 
	 * - analogno metodi gore
	 */
	public boolean solveNQUtilRev(int col)
	{
	    
	    if (col ==  -1){
	   
	    	combinations[combNum-count+combNum/2-1].getFromHashMap(positionMap);
	    	count++;
	        return true;
	    }
	 
	   /*prolaz kroz celu tablu, dok ne prodje celu tablu ili ne skupi svij
	    combNum resenja; krecemo otpozadi*/
	    for (int i = dimension-1; i >= 0 && count < combNum; i--)
	    {
	        
	        if ( isSafe(i, col,board) ){
	            
	            board[i][col] = 1;
	            positionMap.put(col, i);
	            solveNQUtilRev(col - 1) ;
	 
	            
	            board[i][col] = 0; // BACKTRACK
	            positionMap.remove(col);
           
	        }
	    }
	 
	    
	    return false;
	}
	
	/*
	 * Pronalazenje maks resenja
	 */
	public boolean solveNQUtilFull(int col)
	{
	    /* ako je stigao do poslednje kolone, nasao je min postavku
	    dopuni do potpune i vrati true */
	    if (col == dimension ){
	    	updateFull();
	        return true;
	    }
	 
	    /*prolaz kroz celu tablu, dok ne prodje celu tablu ili ne skupi svih
	    fullCombNum resenja*/
	    for (int i = 0; i < dimension && countFull < fullCombNum; i++)
	    {
	  
	        if ( isSafe(i, col,board) ){
	            board[i][col] = 1;
	            solveNQUtilFull(col + 1) ;

            board[i][col] = 0; // BACKTRACK

	        }
	    }
	 

	    return false;
	}
	
	
	
	
}
