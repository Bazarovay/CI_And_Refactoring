package chess;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pieces.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * @author Ashish Kedia and Adarsh Mohata
 *
 */


/**
 * This is the Main Class of our project.
 * All GUI Elements are declared, initialized and used in this class itself.
 * It is inherited from the JFrame Class of Java's Swing Library. 
 * 
 */

public class Main extends JFrame implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	//Variable Declaration
	private static final int BOARD_HEIGHT = 700;
	private static final int BOARD_WIDTH = 1110;
	private static Rook wr01,wr02,br01,br02;
	private static Knight wk01,wk02,bk01,bk02;
	private static Bishop wb01,wb02,bb01,bb02;
	private static Pawn wp[],bp[];
	private static Queen wq,bq;
	private static King wk,bk;
	private ChessboardCell c,previous;
	private int chance=0;
	private ChessboardCell boardState[][];
	private ArrayList<ChessboardCell> destinationlist = new ArrayList<ChessboardCell>();
	private Player White=null,Black=null;
	private JPanel board=new JPanel(new GridLayout(8,8));
	private JPanel wdetails=new JPanel(new GridLayout(3,3));
	private JPanel bdetails=new JPanel(new GridLayout(3,3));
	private JPanel wcombopanel=new JPanel();
	private JPanel bcombopanel=new JPanel();
	private JPanel controlPanel,WhitePlayer,BlackPlayer,temp,displayTime,showPlayer,time;
	private JSplitPane split;
	private JLabel label,mov;
	private static JLabel CHNC;
	private Time timer;
	static Main chessMainboard;
	private boolean selected=false,end=false;
	private Container content;
	private ArrayList<Player> wplayer,bplayer;
	private ArrayList<String> Wnames=new ArrayList<String>();
	private ArrayList<String> Bnames=new ArrayList<String>();
	private JComboBox<String> wcombo,bcombo;
	private String wname=null,bname=null,winner=null;
	static String move = null;
	private Player tempPlayer;
	private JScrollPane wscroll,bscroll;
	private String[] WNames={},BNames={};
	private JSlider timeSlider;
	private BufferedImage image;
	private Button start,wselect,bselect,WNewPlayer,BNewPlayer;
	static int timeRemaining=60;
	public static void main(String[] args){
	
	//variable initialization
	wr01=new Rook("WR01","White_Rook.png",0); //$NON-NLS-1$ //$NON-NLS-2$
	wr02=new Rook("WR02","White_Rook.png",0); //$NON-NLS-1$ //$NON-NLS-2$
	br01=new Rook("BR01","Black_Rook.png",1); //$NON-NLS-1$ //$NON-NLS-2$
	br02=new Rook("BR02","Black_Rook.png",1); //$NON-NLS-1$ //$NON-NLS-2$
	wk01=new Knight("WK01","White_Knight.png",0); //$NON-NLS-1$ //$NON-NLS-2$
	wk02=new Knight("WK02","White_Knight.png",0); //$NON-NLS-1$ //$NON-NLS-2$
	bk01=new Knight("BK01","Black_Knight.png",1); //$NON-NLS-1$ //$NON-NLS-2$
	bk02=new Knight("BK02","Black_Knight.png",1); //$NON-NLS-1$ //$NON-NLS-2$
	wb01=new Bishop("WB01","White_Bishop.png",0); //$NON-NLS-1$ //$NON-NLS-2$
	wb02=new Bishop("WB02","White_Bishop.png",0); //$NON-NLS-1$ //$NON-NLS-2$
	bb01=new Bishop("BB01","Black_Bishop.png",1); //$NON-NLS-1$ //$NON-NLS-2$
	bb02=new Bishop("BB02","Black_Bishop.png",1); //$NON-NLS-1$ //$NON-NLS-2$
	wq=new Queen("WQ","White_Queen.png",0); //$NON-NLS-1$ //$NON-NLS-2$
	bq=new Queen("BQ","Black_Queen.png",1); //$NON-NLS-1$ //$NON-NLS-2$
	wk=new King("WK","White_King.png",0,7,3); //$NON-NLS-1$ //$NON-NLS-2$
	bk=new King("BK","Black_King.png",1,0,3); //$NON-NLS-1$ //$NON-NLS-2$
	wp=new Pawn[8];
	bp=new Pawn[8];
	for(int i=0;i<8;i++)
	{
		wp[i]=new Pawn("WP0"+(i+1),"White_Pawn.png",0); //$NON-NLS-1$ //$NON-NLS-2$
		bp[i]=new Pawn("BP0"+(i+1),"Black_Pawn.png",1); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	//Setting up the board
	chessMainboard = new Main();
	chessMainboard.setVisible(true);	
	chessMainboard.setResizable(false);
	}
	
	//Constructor
	private Main()
    {
		timeRemaining=60;
		this.timeSlider = new JSlider();
		move="White"; //$NON-NLS-1$
		this.wname=null;
		this.bname=null;
		this.winner=null;
		this.board=new JPanel(new GridLayout(8,8));
		this.wdetails=new JPanel(new GridLayout(3,3));
		this.bdetails=new JPanel(new GridLayout(3,3));
		this.bcombopanel=new JPanel();
		this.wcombopanel=new JPanel();
		this.Wnames=new ArrayList<String>();
		this.Bnames=new ArrayList<String>();
		this.board.setMinimumSize(new Dimension(800,700));
		ImageIcon img = new ImageIcon(this.getClass().getResource("icon.png")); //$NON-NLS-1$
		this.setIconImage(img.getImage());
		
		//Time Slider Details
		this.timeSlider.setMinimum(1);
		this.timeSlider.setMaximum(15);
		this.timeSlider.setValue(1);
		this.timeSlider.setMajorTickSpacing(2);
		this.timeSlider.setPaintLabels(true);
		this.timeSlider.setPaintTicks(true);
		this.timeSlider.addChangeListener(new TimeChange());
		
		
		//Fetching Details of all Players
		this.wplayer= Player.fetch_players();
		Iterator<Player> witr=this.wplayer.iterator();
		while(witr.hasNext())
			this.Wnames.add(witr.next().getPlayerName());
				
		this.bplayer= Player.fetch_players();
		Iterator<Player> bitr=this.bplayer.iterator();
		while(bitr.hasNext())
			this.Bnames.add(bitr.next().getPlayerName());
	    this.WNames=this.Wnames.toArray(this.WNames);	
		this.BNames=this.Bnames.toArray(this.BNames);
		
		ChessboardCell cell;
		this.board.setBorder(BorderFactory.createLoweredBevelBorder());
		pieces.Piece P;
		this.content=getContentPane();
		setSize(BOARD_WIDTH,BOARD_HEIGHT);
		setTitle("Chess"); //$NON-NLS-1$
		this.content.setBackground(Color.black);
		this.controlPanel=new JPanel();
		this.content.setLayout(new BorderLayout());
		this.controlPanel.setLayout(new GridLayout(3,3));
		this.controlPanel.setBorder(BorderFactory.createTitledBorder(null, "Statistics", TitledBorder.TOP,TitledBorder.CENTER, new Font("Lucida Calligraphy",Font.PLAIN,20), Color.ORANGE)); //$NON-NLS-1$ //$NON-NLS-2$
		
		//Defining the Player Box in Control Panel
		this.WhitePlayer=new JPanel();
		this.WhitePlayer.setBorder(BorderFactory.createTitledBorder(null, "White Player", TitledBorder.TOP,TitledBorder.CENTER, new Font("times new roman",Font.BOLD,18), Color.RED)); //$NON-NLS-1$ //$NON-NLS-2$
		this.WhitePlayer.setLayout(new BorderLayout());
		
		this.BlackPlayer=new JPanel();
		this.BlackPlayer.setBorder(BorderFactory.createTitledBorder(null, "Black Player", TitledBorder.TOP,TitledBorder.CENTER, new Font("times new roman",Font.BOLD,18), Color.BLUE)); //$NON-NLS-1$ //$NON-NLS-2$
	    this.BlackPlayer.setLayout(new BorderLayout());
		
	    JPanel whitestats=new JPanel(new GridLayout(3,3));
		JPanel blackstats=new JPanel(new GridLayout(3,3));
		this.wcombo=new JComboBox<String>(this.WNames);
		this.bcombo=new JComboBox<String>(this.BNames);
		this.wscroll=new JScrollPane(this.wcombo);
		this.bscroll=new JScrollPane(this.bcombo);
		this.wcombopanel.setLayout(new FlowLayout());
		this.bcombopanel.setLayout(new FlowLayout());
		this.wselect=new Button("Select"); //$NON-NLS-1$
		this.bselect=new Button("Select"); //$NON-NLS-1$
		this.wselect.addActionListener(new SelectHandler(0));
		this.bselect.addActionListener(new SelectHandler(1));
		this.WNewPlayer=new Button("New Player"); //$NON-NLS-1$
		this.BNewPlayer=new Button("New Player"); //$NON-NLS-1$
		this.WNewPlayer.addActionListener(new Handler(0));
		this.BNewPlayer.addActionListener(new Handler(1));
		this.wcombopanel.add(this.wscroll);
		this.wcombopanel.add(this.wselect);
		this.wcombopanel.add(this.WNewPlayer);
		this.bcombopanel.add(this.bscroll);
		this.bcombopanel.add(this.bselect);
		this.bcombopanel.add(this.BNewPlayer);
		this.WhitePlayer.add(this.wcombopanel,BorderLayout.NORTH);
		this.BlackPlayer.add(this.bcombopanel,BorderLayout.NORTH);
		whitestats.add(new JLabel("Name   :")); //$NON-NLS-1$
		whitestats.add(new JLabel("Played :")); //$NON-NLS-1$
		whitestats.add(new JLabel("Won    :")); //$NON-NLS-1$
		blackstats.add(new JLabel("Name   :")); //$NON-NLS-1$
		blackstats.add(new JLabel("Played :")); //$NON-NLS-1$
		blackstats.add(new JLabel("Won    :")); //$NON-NLS-1$
		this.WhitePlayer.add(whitestats,BorderLayout.WEST);
		this.BlackPlayer.add(blackstats,BorderLayout.WEST);
		this.controlPanel.add(this.WhitePlayer);
		this.controlPanel.add(this.BlackPlayer);
		
		setPiecesPosition();
		
		this.showPlayer=new JPanel(new FlowLayout());  
		this.showPlayer.add(this.timeSlider);
		JLabel setTime=new JLabel("Set Timer(in mins):");  //$NON-NLS-1$
		this.start=new Button("Start"); //$NON-NLS-1$
		this.start.setBackground(Color.black);
		this.start.setForeground(Color.white);
	    this.start.addActionListener(new START());
		this.start.setPreferredSize(new Dimension(120,40));
		setTime.setFont(new Font("Arial",Font.BOLD,16)); //$NON-NLS-1$
		this.label = new JLabel("Time Starts now", JLabel.CENTER); //$NON-NLS-1$
		  this.label.setFont(new Font("SERIF", Font.BOLD, 30)); //$NON-NLS-1$
	      this.displayTime=new JPanel(new FlowLayout());
	      this.time=new JPanel(new GridLayout(3,3));
	      this.time.add(setTime);
	      this.time.add(this.showPlayer);
	      this.displayTime.add(this.start);
	      this.time.add(this.displayTime);
	      this.controlPanel.add(this.time);
		this.board.setMinimumSize(new Dimension(800,700));
		
		//The Left Layout When Game is inactive
		this.temp=new JPanel(){
			private static final long serialVersionUID = 1L;
			     
			@Override
		    public void paintComponent(Graphics g) {
				  try {
			          Main.this.image = ImageIO.read(this.getClass().getResource("clash.jpg")); //$NON-NLS-1$
			       } catch (IOException ex) {
			            System.out.println("not found"); //$NON-NLS-1$
			       }
			   
				g.drawImage(Main.this.image, 0, 0, null);
			}         
	    };

		this.temp.setMinimumSize(new Dimension(800,700));
		this.controlPanel.setMinimumSize(new Dimension(285,700));
		this.split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,this.temp, this.controlPanel);
		
	    this.content.add(this.split);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

	private void setPiecesPosition() {
		ChessboardCell cell;
		pieces.Piece P;
		this.boardState=new ChessboardCell[8][8];
		for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {	
				P=null;
				if(i==0&&j==0) {
					P=br01;
				} else if(i==0&&j==7) {
					P=br02;
				} else if(i==7&&j==0) {
					P=wr01;
				} else if(i==7&&j==7) {
					P=wr02;
				} else if(i==0&&j==1) {
					P=bk01;
				} else if (i==0&&j==6) {
					P=bk02;
				} else if(i==7&&j==1) {
					P=wk01;
				} else if (i==7&&j==6) {
					P=wk02;
				} else if(i==0&&j==2) {
					P=bb01;
				} else if (i==0&&j==5) {
					P=bb02;
				} else if(i==7&&j==2) {
					P=wb01;
				} else if(i==7&&j==5) {
					P=wb02;
				} else if(i==0&&j==3) {
					P=bk;
				} else if(i==0&&j==4) {
					P=bq;
				} else if(i==7&&j==3) {
					P=wk;
				} else if(i==7&&j==4) {
					P=wq;
				} else if(i==1) {
				P=bp[j];
				} else if(i==6) {
					P=wp[j];
				}
				cell=new ChessboardCell(i,j,P);
				cell.addMouseListener(this);
				this.board.add(cell);
				this.boardState[i][j]=cell;
			}
		}
	}
	
	// A function to change the chance from White Player to Black Player or vice verse
	// It is made public because it is to be accessed in the Time Class
	public void changechance()
	{
		if (this.boardState[getKing(this.chance).getx()][getKing(this.chance).gety()].ischeck())
		{
			this.chance^=1;
			gameend();
		}
		if(this.destinationlist.isEmpty()==false)
			cleandestinations(this.destinationlist);
		if(this.previous!=null)
			this.previous.deselect();
		this.previous=null;
		this.chance^=1;
		if(!this.end && this.timer!=null)
		{
			this.timer.reset();
			this.timer.start();
			this.showPlayer.remove(CHNC);
			if(Main.move == "White") { //$NON-NLS-1$
				Main.move = "Black"; //$NON-NLS-1$
			} else {
				Main.move = "White"; //$NON-NLS-1$
			}
			CHNC.setText(Main.move);
			this.showPlayer.add(CHNC);
		}
	}
	
	//A function to retrieve the Black King or White King
	private King getKing(int color)
	{
		if (color==0) {
			return wk;
		}
		
			return bk;
	}
	
	//A function to clean the highlights of possible destination cells
    private void cleandestinations(ArrayList<ChessboardCell> destlist)      //Function to clear the last move's destinations
    {
    	ListIterator<ChessboardCell> it = destlist.listIterator();
    	while(it.hasNext())
    		it.next().removepossibledestination();
    }
    
    //A function that indicates the possible moves by highlighting the Cells
    private void highlightdestinations(ArrayList<ChessboardCell> destlist)
    {
    	ListIterator<ChessboardCell> it = destlist.listIterator();
    	while(it.hasNext())
    		it.next().setpossibledestination();
    }
    
    
  //Function to check if the king will be in danger if the given move is made
    private boolean isKingInDanger(ChessboardCell fromcell,ChessboardCell tocell)
    {
    	ChessboardCell newboardstate[][] = new ChessboardCell[8][8];
    	for(int i=0;i<8;i++)
    		for(int j=0;j<8;j++)
    		{	try { newboardstate[i][j] = new ChessboardCell(this.boardState[i][j]);} catch (CloneNotSupportedException e){e.printStackTrace(); System.out.println("There is a problem with cloning !!"); }} //$NON-NLS-1$
    	
    	if(newboardstate[tocell.x][tocell.y].getpiece()!=null)
			newboardstate[tocell.x][tocell.y].removePiece();
    	
		newboardstate[tocell.x][tocell.y].setPiece(newboardstate[fromcell.x][fromcell.y].getpiece());
		if(newboardstate[tocell.x][tocell.y].getpiece() instanceof King)
		{
			((King)(newboardstate[tocell.x][tocell.y].getpiece())).setx(tocell.x);
			((King)(newboardstate[tocell.x][tocell.y].getpiece())).sety(tocell.y);
		}
		newboardstate[fromcell.x][fromcell.y].removePiece();
		if (((King)(newboardstate[getKing(this.chance).getx()][getKing(this.chance).gety()].getpiece())).isindanger(newboardstate)==true) {
			return true;
		}

		return false;
    }
    
    //A function to eliminate the possible moves that will put the King in danger
    private ArrayList<ChessboardCell> filterdestination (ArrayList<ChessboardCell> destlist, ChessboardCell fromcell)
    {
    	ArrayList<ChessboardCell> newlist = new ArrayList<ChessboardCell>();
    	ChessboardCell newboardstate[][] = new ChessboardCell[8][8];
    	ListIterator<ChessboardCell> it = destlist.listIterator();
    	int x,y;
    	while (it.hasNext())
    	{
    		for(int i=0;i<8;i++)
        		for(int j=0;j<8;j++)
        		{	try { newboardstate[i][j] = new ChessboardCell(this.boardState[i][j]);} catch (CloneNotSupportedException e){e.printStackTrace();}}
    		
    		ChessboardCell tempc = it.next();
    		if(newboardstate[tempc.x][tempc.y].getpiece()!=null)
    			newboardstate[tempc.x][tempc.y].removePiece();
    		newboardstate[tempc.x][tempc.y].setPiece(newboardstate[fromcell.x][fromcell.y].getpiece());
    		x=getKing(this.chance).getx();
    		y=getKing(this.chance).gety();
    		if(newboardstate[fromcell.x][fromcell.y].getpiece() instanceof King)
    		{
    			((King)(newboardstate[tempc.x][tempc.y].getpiece())).setx(tempc.x);
    			((King)(newboardstate[tempc.x][tempc.y].getpiece())).sety(tempc.y);
    			x=tempc.x;
    			y=tempc.y;
    		}
    		newboardstate[fromcell.x][fromcell.y].removePiece();
    		if ((((King)(newboardstate[x][y].getpiece())).isindanger(newboardstate)==false))
    			newlist.add(tempc);
    	}
    	return newlist;
    }
    
    //A Function to filter the possible moves when the king of the current player is under Check 
    private ArrayList<ChessboardCell> incheckfilter (ArrayList<ChessboardCell> destlist, ChessboardCell fromcell, int color)
    {
    	ArrayList<ChessboardCell> newlist = new ArrayList<ChessboardCell>();
    	ChessboardCell newboardstate[][] = new ChessboardCell[8][8];
    	ListIterator<ChessboardCell> it = destlist.listIterator();
    	int x,y;
    	while (it.hasNext())
    	{
    		for(int i=0;i<8;i++)
        		for(int j=0;j<8;j++)
        		{	try { newboardstate[i][j] = new ChessboardCell(this.boardState[i][j]);} catch (CloneNotSupportedException e){e.printStackTrace();}}
    		ChessboardCell tempc = it.next();
    		if(newboardstate[tempc.x][tempc.y].getpiece()!=null)
    			newboardstate[tempc.x][tempc.y].removePiece();
    		newboardstate[tempc.x][tempc.y].setPiece(newboardstate[fromcell.x][fromcell.y].getpiece());
    		x=getKing(color).getx();
    		y=getKing(color).gety();
    		if(newboardstate[tempc.x][tempc.y].getpiece() instanceof King)
    		{
    			((King)(newboardstate[tempc.x][tempc.y].getpiece())).setx(tempc.x);
    			((King)(newboardstate[tempc.x][tempc.y].getpiece())).sety(tempc.y);
    			x=tempc.x;
    			y=tempc.y;
    		}
    		newboardstate[fromcell.x][fromcell.y].removePiece();
    		if ((((King)(newboardstate[x][y].getpiece())).isindanger(newboardstate)==false))
    			newlist.add(tempc);
    	}
    	return newlist;
    }
    
    //A function to check if the King is check-mate. The Game Ends if this function returns true.
    public boolean checkmate(int color)
    {
    	ArrayList<ChessboardCell> dlist;
    	for(int i=0;i<8;i++)
    	{
    		for(int j=0;j<8;j++)
    		{
    			if (this.boardState[i][j].getpiece()!=null && this.boardState[i][j].getpiece().getcolor()==color)
    			{
    				dlist=this.boardState[i][j].getpiece().move(this.boardState, i, j);
    				dlist=incheckfilter(dlist,this.boardState[i][j],color);
    				if(dlist.size()!=0)
    					return false;
    			}
    		}
    	}
    	return true;
    }
	
    
    @SuppressWarnings("deprecation")
	private void gameend()
    {
    	cleandestinations(this.destinationlist);
    	this.displayTime.disable();
    	this.timer.countdownTimer.stop();
    	if(this.previous!=null) {
    		this.previous.removePiece();
    	}
    	if(this.chance==0) {
    		this.White.incrementGamesWon();
			this.White.Update_Player();
			this.winner=this.White.getPlayerName();
		} else {
			this.Black.incrementGamesWon();
			this.Black.Update_Player();
			this.winner=this.Black.getPlayerName();
		}
		JOptionPane.showMessageDialog(this.board,"Checkmate!!!\n"+this.winner+" wins"); //$NON-NLS-1$ //$NON-NLS-2$
		this.WhitePlayer.remove(this.wdetails);
		this.BlackPlayer.remove(this.bdetails);
		this.displayTime.remove(this.label);
		
		this.displayTime.add(this.start);
		this.showPlayer.remove(this.mov);
		this.showPlayer.remove(CHNC);
		this.showPlayer.revalidate();
		this.showPlayer.add(this.timeSlider);
		
		this.split.remove(this.board);
		this.split.add(this.temp);
		this.WNewPlayer.enable();
		this.BNewPlayer.enable();
		this.wselect.enable();
		this.bselect.enable();
		this.end=true;
		chessMainboard.disable();
		chessMainboard.dispose();
		chessMainboard = new Main();
		chessMainboard.setVisible(true);
		chessMainboard.setResizable(false);
    }
    
    //These are the abstract function of the parent class. Only relevant method here is the On-Click Fuction
    //which is called when the user clicks on a particular cell
	@Override
	public void mouseClicked(MouseEvent arg0){
		// TODO Auto-generated method stub
		this.c=(ChessboardCell)arg0.getSource();
		if (this.previous==null)
		{
			if(this.c.getpiece()!=null)
			{
				if(this.c.getpiece().getcolor()!=this.chance)
					return;
				this.c.select();
				this.previous=this.c;
				this.destinationlist.clear();
				this.destinationlist=this.c.getpiece().move(this.boardState, this.c.x, this.c.y);
				if(this.c.getpiece() instanceof King)
					this.destinationlist=filterdestination(this.destinationlist,this.c);
				else
				{
					if(this.boardState[getKing(this.chance).getx()][getKing(this.chance).gety()].ischeck())
						this.destinationlist = new ArrayList<ChessboardCell>(filterdestination(this.destinationlist,this.c));
					else if(this.destinationlist.isEmpty()==false && isKingInDanger(this.c,this.destinationlist.get(0)))
						this.destinationlist.clear();
				}
				highlightdestinations(this.destinationlist);
			}
		}
		else
		{
			if(this.c.x==this.previous.x && this.c.y==this.previous.y)
			{
				this.c.deselect();
				cleandestinations(this.destinationlist);
				this.destinationlist.clear();
				this.previous=null;
			}
			else if(this.c.getpiece()==null||this.previous.getpiece().getcolor()!=this.c.getpiece().getcolor())
			{
				if(this.c.ispossibledestination())
				{
					if(this.c.getpiece()!=null)
						this.c.removePiece();
					this.c.setPiece(this.previous.getpiece());
					if (this.previous.ischeck())
						this.previous.removecheck();
					this.previous.removePiece();
					if(getKing(this.chance^1).isindanger(this.boardState))
					{
						this.boardState[getKing(this.chance^1).getx()][getKing(this.chance^1).gety()].setcheck();
						if (checkmate(getKing(this.chance^1).getcolor()))
						{
							this.previous.deselect();
							if(this.previous.getpiece()!=null)
								this.previous.removePiece();
							gameend();
						}
					}
					if(getKing(this.chance).isindanger(this.boardState)==false)
						this.boardState[getKing(this.chance).getx()][getKing(this.chance).gety()].removecheck();
					if(this.c.getpiece() instanceof King)
					{
						((King)this.c.getpiece()).setx(this.c.x);
						((King)this.c.getpiece()).sety(this.c.y);
					}
					changechance();
					if(!this.end)
					{
						this.timer.reset();
						this.timer.start();
					}
				}
				if(this.previous!=null)
				{
					this.previous.deselect();
					this.previous=null;
				}
				cleandestinations(this.destinationlist);
				this.destinationlist.clear();
			}
			else if(this.previous.getpiece().getcolor()==this.c.getpiece().getcolor())
			{
				this.previous.deselect();
				cleandestinations(this.destinationlist);
				this.destinationlist.clear();
				this.c.select();
				this.previous=this.c;
				this.destinationlist=this.c.getpiece().move(this.boardState, this.c.x, this.c.y);
				if(this.c.getpiece() instanceof King)
					this.destinationlist=filterdestination(this.destinationlist,this.c);
				else
				{
					if(this.boardState[getKing(this.chance).getx()][getKing(this.chance).gety()].ischeck())
						this.destinationlist = new ArrayList<ChessboardCell>(filterdestination(this.destinationlist,this.c));
					else if(this.destinationlist.isEmpty()==false && isKingInDanger(this.c,this.destinationlist.get(0)))
						this.destinationlist.clear();
				}
				highlightdestinations(this.destinationlist);
			}
		}
		if(this.c.getpiece()!=null && this.c.getpiece() instanceof King)
		{
			((King)this.c.getpiece()).setx(this.c.x);
			((King)this.c.getpiece()).sety(this.c.y);
		}
	}
    
    //Other Irrelevant abstract function. Only the Click Event is captured.
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub		
	}
	
	
	class START implements ActionListener
	{

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		if(Main.this.White==null||Main.this.Black==null)
			{JOptionPane.showMessageDialog(Main.this.controlPanel, "Fill in the details"); //$NON-NLS-1$
			return;}
		Main.this.White.incrementGamesPlayed();
		Main.this.White.Update_Player();
		Main.this.Black.incrementGamesPlayed();
		Main.this.Black.Update_Player();
		Main.this.WNewPlayer.disable();
		Main.this.BNewPlayer.disable();
		Main.this.wselect.disable();
		Main.this.bselect.disable();
		Main.this.split.remove(Main.this.temp);
		Main.this.split.add(Main.this.board);
		Main.this.showPlayer.remove(Main.this.timeSlider);
		Main.this.mov=new JLabel("Move:"); //$NON-NLS-1$
		Main.this.mov.setFont(new Font("Comic Sans MS",Font.PLAIN,20)); //$NON-NLS-1$
		Main.this.mov.setForeground(Color.red);
		Main.this.showPlayer.add(Main.this.mov);
		CHNC=new JLabel(move);
		CHNC.setFont(new Font("Comic Sans MS",Font.BOLD,20)); //$NON-NLS-1$
		CHNC.setForeground(Color.blue);
		Main.this.showPlayer.add(CHNC);
		Main.this.displayTime.remove(Main.this.start);
		Main.this.displayTime.add(Main.this.label);
		Main.this.timer=new Time(Main.this.label);
		Main.this.timer.start();
	}
	}
	
	class TimeChange implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent arg0)
		{
			timeRemaining=Main.this.timeSlider.getValue()*60;
		}
	}
	
	
	class SelectHandler implements ActionListener
	{
		private int color;
		
		SelectHandler(int i)
		{
			this.color=i;
		}
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO Auto-generated method stub
				Main.this.tempPlayer=null;
				String n=(this.color==0)?Main.this.wname:Main.this.bname;
				JComboBox<String> jc=(this.color==0)?Main.this.wcombo:Main.this.bcombo;
				JComboBox<String> ojc=(this.color==0)?Main.this.bcombo:Main.this.wcombo;
				ArrayList<Player> pl=(this.color==0)?Main.this.wplayer:Main.this.bplayer;
				//ArrayList<Player> otherPlayer=(color==0)?bplayer:wplayer;
				ArrayList<Player> opl=Player.fetch_players();
				if(opl.isEmpty())
					return;
				JPanel det=(this.color==0)?Main.this.wdetails:Main.this.bdetails;
				JPanel PL=(this.color==0)?Main.this.WhitePlayer:Main.this.BlackPlayer; 
				if(Main.this.selected==true)
					det.removeAll();
				n=(String)jc.getSelectedItem();
				Iterator<Player> it=pl.iterator();
				Iterator<Player> oit=opl.iterator();
				while(it.hasNext())
				{	
					Player p=it.next();
					if(p.getPlayerName().equals(n))
						{Main.this.tempPlayer=p;
						break;}
				}
				while(oit.hasNext())
				{	
					Player p=oit.next();
					if(p.getPlayerName().equals(n))
						{opl.remove(p);
						break;}
				}
				
				if(Main.this.tempPlayer==null)
					return;
				if(this.color==0)
					Main.this.White=Main.this.tempPlayer;
				else
					Main.this.Black=Main.this.tempPlayer;
				Main.this.bplayer=opl;
				ojc.removeAllItems();
				for (Player s:opl)
					ojc.addItem(s.getPlayerName());
				det.add(new JLabel(" "+Main.this.tempPlayer.getPlayerName())); //$NON-NLS-1$
				det.add(new JLabel(" "+Main.this.tempPlayer.getGamesPlayed())); //$NON-NLS-1$
				det.add(new JLabel(" "+Main.this.tempPlayer.getGamesWon())); //$NON-NLS-1$
				
				PL.revalidate();
				PL.repaint();
				PL.add(det);
				Main.this.selected=true;
			}
			
		}
		
		
		
		class Handler implements ActionListener{
			private int color;
			Handler(int i)
			{
				this.color=i;
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String n=(this.color==0)?Main.this.wname:Main.this.bname;
				JPanel j=(this.color==0)?Main.this.WhitePlayer:Main.this.BlackPlayer;
				ArrayList<Player> N=Player.fetch_players();
				Iterator<Player> it=N.iterator();
				JPanel det=(this.color==0)?Main.this.wdetails:Main.this.bdetails;
				n=JOptionPane.showInputDialog(j,"Enter your name"); //$NON-NLS-1$
					
					if(n!=null)
					{
					
					while(it.hasNext())
					{
						if(it.next().getPlayerName().equals(n))
						{JOptionPane.showMessageDialog(j,"Player exists"); //$NON-NLS-1$
						return;}
					}
			
						if(n.length()!=0)
						{Player tem=new Player(n);
						tem.Update_Player();
						if(this.color==0)
							Main.this.White=tem;
						else
							Main.this.Black=tem;
						}
						else return;
					}
				else
					return;
				det.removeAll();
				det.add(new JLabel(" "+n)); //$NON-NLS-1$
				det.add(new JLabel(" 0")); //$NON-NLS-1$
				det.add(new JLabel(" 0")); //$NON-NLS-1$
				j.revalidate();
				j.repaint();
				j.add(det);
				Main.this.selected=true;
			}
			}	 
}