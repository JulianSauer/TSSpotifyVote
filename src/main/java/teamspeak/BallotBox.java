package teamspeak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BallotBox {

    /**
     * The table that stores the votes and the users who have been added to the vote lists
     */
    private Map<String, ArrayList<String>> ballots;

    /**
     * Creates an empty list
     */
    BallotBox() {
        ballots = new HashMap<>();

        System.out.println("Created BallotBox");        //TODO DEBUG
    }

    /**
     * Searches the ballots for the combination of user and vote category.
     *
     * @param client   User who voted for a category
     * @param voteType Kind of vote the user voted for
     * @return True, if the voted for the category
     */
    public boolean contains(String client, String voteType) {
        if (ballots.containsKey(voteType))
            return ballots.get(voteType).contains(client);
        return false;
    }

    /**
     * Adds the combination of user and vote to the table of ballots. Does not look for duplicates.
     *
     * @param voteType  Type of vote
     * @param client    User who voted
     * @param userCount Number of users in channel
     * @return True if the vote has passed
     */
    public boolean castVoteFor(String voteType, String client, int userCount) {

        if (!ballots.containsKey(voteType)) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(client);
            ballots.put(voteType, temp);
        } else
            ballots.get(voteType).add(client);

        System.out.println("Added user " + client + " to list");            //TODO DEBUG
        if (countVotesFor(voteType) == (int) Math.ceil(((float) userCount) / 2)) {
            clear(voteType);
            return true;
        }
        return false;
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
