import java.util.ArrayList;
import java.util.HashMap;

public class BallotBox {

    /**
     * The table that stores the votes and the users who have been added to the vote lists
     */
    private HashMap<String, ArrayList<String>> ballots;

    /**
     * Creates an empty list
     */
    BallotBox() {
        ballots = new HashMap<String, ArrayList<String>>();

        System.out.println("Created BallotBox");
    }

    /**
     * Searches the ballots for the combination of user and vote category.
     *
     * @param user User who voted for a category
     * @param voteType Kind of vote the user voted for
     * @return True, if the voted for the category
     */
    public boolean contains(String user, String voteType) {
        return !ballots.isEmpty() && ballots.get(voteType).contains(user);
    }

    /**
     * Adds the combination of user and vote to the table of ballots. Does not look for duplicates.
     *
     * @param voteType Type of vote
     * @param user User who voted
     */
    public void castVoteFor(String voteType, String user) {

        if (!ballots.containsKey(voteType)) {
            ArrayList<String> temp = new ArrayList<String>();
            temp.add(user);
            ballots.put(voteType, temp);
        } else
            ballots.get(voteType).add(user);

        System.out.println("Added user " + user + " to list");            //DEBUG
    }

    /**
     * Counts the number of users who have been added to this vote list
     *
     * @param voteType Category to count votes for
     * @return Number of votes
     */
    public int countVotesFor(String voteType) {
        return ballots.get(voteType).size();
    }

    /**
     * Returns a list of all users who have been added to this vote list
     *
     * @param voteType Category to list users
     * @return List of all users that have been added to this vote list
     */
    public ArrayList<String> get(String voteType) {
        return ballots.get(voteType);
    }

    /**
     * Clears the specific votes
     *
     * @param voteType the list to be cleared
     */
    public void clear(String voteType) {
        ballots.remove(voteType);
    }

    /**
     * Clears the entire list
     */
    public void clearAll() {
        ballots.clear();
    }

}
