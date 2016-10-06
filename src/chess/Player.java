package chess;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;


/**
 * This is the Player Class
 * It provides the functionality of keeping track of all the users
 * Objects of this class is updated and written in the Game's Data Files after every Game
 *
 */
public class Player implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private Integer gamesplayed;
	private Integer gameswon;
	private static final String PLAYER_FILE_NAME = "chessgamedata.dat"; //$NON-NLS-1$
	private static final String USER_DIRECTORY = "user.dir"; //$NON-NLS-1$
	private static final String TEMPORARY_FILE = "tempfile.dat"; //$NON-NLS-1$


	public Player(String name) {
		this.name = name.trim();
		this.gamesplayed = new Integer(0);
		this.gameswon = new Integer(0);
	}
	
	public String getPlayerName()
	{
		return this.name;
	}
	
	public Integer getGamesPlayed()
	{
		return this.gamesplayed;
	}
	
	public Integer getGamesWon()
	{
		return this.gameswon;
	}
	
	public Integer getWinPercentage()
	{
		return new Integer((this.gameswon*100)/this.gamesplayed);
	}
	
	public void incrementGamesPlayed()
	{
		this.gamesplayed++;
	}
	
	public void incrementGamesWon()
	{
		this.gameswon++;
	}
	
	
	public static ArrayList<Player> fetch_players()         //Function to fetch the list of the players
	{
		Player tempplayer;
		ObjectInputStream input = null;
		ArrayList<Player> players = new ArrayList<Player>();
		try
		{
			File infile = new File(System.getProperty(USER_DIRECTORY)+ File.separator + PLAYER_FILE_NAME);
			input = new ObjectInputStream(new FileInputStream(infile));
			try
			{
				while(true)
				{
					tempplayer = (Player) input.readObject();
					players.add(tempplayer);
				}
			}
			catch(EOFException e)
			{
				input.close();
			}
		}
		catch (FileNotFoundException e)
		{
			players.clear();
			return players;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			try {
				if (null != input) {
					input.close();
				}

				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
			JOptionPane.showMessageDialog(null, "Unable to read the required Game files !!"); //$NON-NLS-1$
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Game Data File Corrupted !! Click Ok to Continue Builing New File"); //$NON-NLS-1$
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return players;
	}
	
	public void Update_Player()            //Function to update the statistics of a player
	{
		ObjectInputStream input = null;
		ObjectOutputStream output = null;
		Player temp_player;
		File inputfile=null;
		File outputfile=null;
		try
		{
			inputfile = new File(System.getProperty(USER_DIRECTORY)+ File.separator + PLAYER_FILE_NAME);
			outputfile = new File(System.getProperty(USER_DIRECTORY)+ File.separator + TEMPORARY_FILE);
		} catch (SecurityException e)
		{
			JOptionPane.showMessageDialog(null, "Read-Write Permission Denied !! Program Cannot Start"); //$NON-NLS-1$
			System.exit(0);
		} 
		boolean playerdonotexist;
		try
		{
			if((null != outputfile) && outputfile.exists() == false)
				outputfile.createNewFile();
			if((null != inputfile) && inputfile.exists()==false)
			{
					output = new ObjectOutputStream(new java.io.FileOutputStream(outputfile,true));
					output.writeObject(this);
			}
			else
			{
				input = new ObjectInputStream(new FileInputStream(inputfile));
				output = new ObjectOutputStream(new FileOutputStream(outputfile));
				playerdonotexist=true;
				try
				{
				while(true)
				{
					temp_player = (Player)input.readObject();
					if (temp_player.getPlayerName().equals(getPlayerName()))
					{
						output.writeObject(this);
						playerdonotexist = false;
					}
					else
						output.writeObject(temp_player);
				}
				}
				catch(EOFException e){
					input.close();
				}
				if(playerdonotexist) {
					output.writeObject(this);
				}
			}
			if (null != inputfile) {
				inputfile.delete();
			}

			output.close();
			File newf = new File(System.getProperty(USER_DIRECTORY)+ File.separator + PLAYER_FILE_NAME);
			if((null != outputfile) && outputfile.renameTo(newf) == false)
				System.out.println("File Renameing Unsuccessful"); //$NON-NLS-1$
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unable to read/write the required Game files !! Press ok to continue"); //$NON-NLS-1$
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Game Data File Corrupted !! Click Ok to Continue Builing New File"); //$NON-NLS-1$
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Apologies, we couldn't get you started. Please retstart"); //$NON-NLS-1$
		}
	}
}
