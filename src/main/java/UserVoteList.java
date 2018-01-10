import java.util.ArrayList;
import java.util.HashMap;

public class UserVoteList {
	
	/**
	 * the table that stores the votes and the users who have been added to the vote lists
	 */
	private HashMap<String,ArrayList<String>> table;
	
	/**
	 * creates an empty list
	 */
	UserVoteList() {
		table = new HashMap<String,ArrayList<String>>();

		System.out.println("Created UserVoteList");
	}
	
	/**
	 * searches the table for the combination of user and vote
	 * @param user one part of the combination to be looked after
	 * @param vote one part of the combination to be looked after
	 * @return true, if the combination of user and vote can be found
	 */
	public boolean contains(String user,String vote) {
		return !table.isEmpty() && table.get(vote).contains(user);
	}
	
	/**
	 * adds the combination of user and vote to the table. Does not look for duplicates
	 * @param user the user to be stored in the table
	 * @param vote the vote to be stored in the table
	 */
	public void add(String user, String vote) {
		
		if(!table.containsKey(vote)) {
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(user);
			table.put(vote,temp);
		}
		else
			table.get(vote).add(user);

		System.out.println("Added user " + user + " to list");			//DEBUG
	}
	
	/**
	 * counts the number of users who have been added to this vote list
	 * @param vote the category to count users
	 * @return the count of votes 
	 */
	public int size(String vote) {
		return table.get(vote).size();
	}

	/**
	 * creates a list of all users who have been added to this vote list
	 * @param vote the category to list users
	 * @return a list of all users that have been added to this vote list
	 */
	public ArrayList<String> get(String vote) {
		return table.get(vote);
	}
	
	/**
	 * clears the specific vote list
	 * @param vote the list to be cleared
	 */
	public void clear(String vote) {
		table.remove(vote);
	}
	
	/**
	 * clears the entire list. Every entry will be removed
	 */
	public void clearAll() {
		table.clear();
	}
	
}
