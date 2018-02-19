package chessBishop;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.*;
import javax.swing.border.*;

import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class ChessGUI {
	
	private static int dim;
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] chessBoardSquares = new JButton[15][15];
    private Image bishopImage;
    private Image[] bishop = new Image[2];
    private JPanel chessBoard;
    private static final String COLS = "ABCDEFGHIJKLMNO";
    public static final int QUEEN = 0, KING = 1,
            ROOK = 2, KNIGHT = 3, BISHOP = 4, PAWN = 5;
    
    public static final int BLACK = 0, WHITE = 1;
    NBishops nb = new NBishops(dim);
    int combNum = nb.getCombNum();
    static int position = -2;
    boolean maxSolutionFlag = false;
    int comb[][];
    int localCombination[][];
    ChessGUI() {
    	
        initializeGui();
        nb.initBoard();
		if (nb.solveNQUtil(0)) {
	        System.out.println("Solution does not exist");
	        return ;
	    }
		
		if (nb.solveNQUtilRev(nb.getDimension()-1)){
			System.out.println("Solution does not exist");
			return ;
			}
		
	  	
    	if (nb.solveNQUtilFull( 0)){
	        System.out.println("Solution does not exist");
	        return ;
	    }
    	
 
    }
    
    /*
     *  osnovne GUI postavke, slike, pozadine, layout
     *  definisanje event metoda
     * 
     */

    public final void initializeGui() {
    	
        createImages();
        GridBagLayout toolbarLayout = new GridBagLayout();
        gui.setBorder(new EmptyBorder(6, 6, 6, 6));
        JToolBar tools = new JToolBar();
        tools.setFloatable(true);
        tools.setLayout( toolbarLayout );
        gui.add(tools, BorderLayout.PAGE_START);
        tools.addSeparator();
        tools.addSeparator();
        Action firstPositionAction = new AbstractAction("Prva kombinacija") {

            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e) {
                setupFirstConf();
            }
        };
        tools.add(firstPositionAction);
        tools.addSeparator();
        
        
        /*
         * 
         * ove metode se korise za reakciju na klik tastera
         * 
         */
        Action previousPositionAction = new AbstractAction("Prethodna kombinacija") {

            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e) {
                setupPreviousConf();
            }
        };
        tools.add(previousPositionAction);
        tools.addSeparator();
        Action nextPositionAction = new AbstractAction("Sledeca kombinacija") {

            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e) {
                setupNextConf();
            }
        };
        tools.add(nextPositionAction);
        tools.addSeparator();
        
        Action lastPositionAction = new AbstractAction("Poslednja kombinacija") {

            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e) {
                setupLastConf();
            }
        };
        tools.add(lastPositionAction);
        tools.addSeparator();
        
        Action fullPositionAction = new AbstractAction("Prikazi maksimalno resenje") {

            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e) {
                setupMaxConf();
            }
        };
        tools.add(fullPositionAction);
        tools.addSeparator();
        Action fullPreviousPositionAction = new AbstractAction("Prethodno max resenje") {

            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e) {
                setupPrevMaxConf();
            }
        };
        tools.add(fullPreviousPositionAction);
        tools.addSeparator();
        
        Action fullNextPositionAction = new AbstractAction("Sledece max resenje") {

            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e) {
                setupNextMaxConf();
            }
        };
        tools.add(fullNextPositionAction);
        tools.addSeparator();
        

        chessBoard = new JPanel(new GridLayout(0, dim)) {

            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			/**
             * Override the preferred size to return the largest it can, in
             * a square shape.  Must (must, must) be added to a GridBagLayout
             * as the only component (it uses the parent as a guide to size)
             * with no GridBagConstaint (so it is centered).
             */
           @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Dimension prefSize = null;
                Component c = getParent();
                if (c == null) {
                    prefSize = new Dimension(
                            (int)d.getWidth(),(int)d.getHeight());
                } else if (c!=null &&
                        c.getWidth()>d.getWidth() &&
                        c.getHeight()>d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }
                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();
                int s = (w>h ? h : w);
                return new Dimension(s,s);
            }
        };

        Color ochre = new Color(204,119,34);
        chessBoard.setBackground(ochre);
        JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.setBackground(ochre);
        boardConstrain.add(chessBoard);
        gui.add(boardConstrain);

        /*
         * postavljanje polja - crna i bela i pravljenje slika
         * 
         */
        for (int ii = 0; ii < dim; ii++) {
            for (int jj = 0; jj < dim; jj++) {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(40, 40));
                // our chess pieces are 64x64 px in size, so we'll
                // 'fill this in' using a transparent icon..
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                if ((jj + ii) % 2 == 0) {
                    b.setBackground(Color.WHITE);
                } else {
                    b.setBackground(Color.BLACK);
                }
                chessBoardSquares[jj][ii] = b;
            }
        }

        //upisivanje imena kolona
        
        for (int ii = 0; ii < dim; ii++) {
            chessBoard.add(
                    new JLabel(COLS.substring(ii, ii + 1),
                    SwingConstants.CENTER));
        }
        
        //postavka elemenata na tablu
        for (int ii = 0; ii < dim; ii++) {
            for (int jj = 0; jj < dim; jj++) {
                switch (ii) {
                    case 0:
                       //chessBoard.add(new JLabel("" + (15-(ii + 1))));
                    	//chessBoard.add(new JLabel("" + (15-(jj + 1)),
                                //SwingConstants.CENTER));
                    default:
                        chessBoard.add(chessBoardSquares[jj][ii]);
                }
            }
            
        }
        
    }
    
    public final JComponent getGui() {
        return gui;
    }

    /*
     * metoda koja generise slike
     */
    private final void createImages() {
        try {
            
            //ovo izmeni,po mogucstvu stavi relativnu putanju
            BufferedImage bi = ImageIO.read(new File("/home/nenadsi/Eclipse_java/BishopApp/src/images/memI0.png"));
            bishop[0] = bi.getSubimage(256, 0, 64, 64);
            bishop[1] = bi.getSubimage(256, 64, 64, 64);
            bishopImage = bishop[1];
           
            
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * ova metoda cisti polje - uklanja figure sa table, pri promeni kombinacije
     */
    
    public void clearIcons(int field[][]){
    	int row,col;
    	for(int i = 0; i < field.length; i++){
    		
    		row = field[i][0];
    		col = field[i][1];
    		chessBoardSquares[col][row].setIcon(null);
    			
    	}
    }
    
    /*
     * 
     * metoda koja postavlja figure na tablu
     * 
     */
    void placeBishops(){
    	int row,col;
    	if (position >= 0 && position < combNum){
    		
    		comb = nb.getCombinations()[position].getCombination();
    	
    		for (int i = 0; i < comb.length; i++) {
    			row = comb[i][0];
    			col = comb[i][1];
    			chessBoardSquares[col][row].setIcon(new ImageIcon(
                    bishopImage));
    		}
    	}	
    	else if (position < 0) {
    		localCombination = nb.getMaxCombinations()[Math.abs(position)-1].getCombination();
    		for (int i = 0; i < localCombination.length; i++) {
       		 row = localCombination[i][0];
       		 col = localCombination[i][1];
                chessBoardSquares[col][row].setIcon(new ImageIcon(
                		bishopImage));
       	 	}
       	
    	}	
    		
    	
    }
    
    
    private final void setupFirstConf() {
    	
    	if(position < 0 && maxSolutionFlag) clearIcons(localCombination);
    	if(position >= 0  && position < combNum) clearIcons(comb);
    	maxSolutionFlag = false;
    	position = 0;
    	System.out.println("Pozicija:"+position);
    	placeBishops();
       
  
       
    }
    private final void setupPreviousConf() {
    	
    	
    	if(position >= 0  && position < combNum) clearIcons(comb);
    	if (position > 0 && position < combNum){    		
    		position--;
    		
    	}
    	placeBishops();
    	System.out.println("Pozicija:"+position);
    }
    private final void setupNextConf(){
    	
    	
    	if(position >= 0  && position < combNum) clearIcons(comb);
    	if (position >=0 && position < combNum-1){
       		position++;
    		
    	}
    	placeBishops();
    	System.out.println("Pozicija:"+position);
    }
    private final void setupLastConf(){
    	if(position < 0 && maxSolutionFlag) clearIcons(localCombination);
    	if(position >= 0  && position < combNum) clearIcons(comb);
    	position = combNum-1;
    	maxSolutionFlag = false;
    	placeBishops();
    	System.out.println("Pozicija:"+position);
    } 
    private final void setupMaxConf(){
    	
 
    	
    	if(position >= 0  && position < combNum) clearIcons(comb);
    	if (position < 0 && maxSolutionFlag) clearIcons(localCombination);
    	position = -1;
    	maxSolutionFlag = true;
    	System.out.println("Pozicija:"+position);
    	placeBishops();
    	 
    	
    } 

    private final void setupPrevMaxConf(){
    	
    	if(position <-1 && position >= -nb.getFullCombNum()){
    		clearIcons(localCombination);
    		position++;
		
    	}
    	System.out.println("Pozicija:"+position);
    	placeBishops();
		
    }
    
    
    private final void setupNextMaxConf(){
    	
    	if(position < 0 && position > -nb.getFullCombNum()){
    		
    		clearIcons(localCombination);
    		position--;
    		
    	}
    	System.out.println("Pozicija:"+position);
    	placeBishops();
    	
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
            	Object[] possibilities = {1, 2, 3,4,5,6,7,8,9,10,11,12,13,14,15};
            	JFrame frame = null;
				int input = (Integer)JOptionPane.showInputDialog(
            	                    frame,
            	                    "Uneti dimenzije table",
            	                    "Dimenzija table",            	                    
            	                    JOptionPane.PLAIN_MESSAGE,
            	                    null,
            	                    possibilities,
            	                    1);

				dim = input;
                ChessGUI cg = new ChessGUI();
                
                JFrame f = new JFrame("Lovci - BishopApp");
                f.add(cg.getGui());
         
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);
                f.pack();
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }
        };

        SwingUtilities.invokeLater(r);
    }
}
