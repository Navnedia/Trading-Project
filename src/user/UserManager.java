package user;

import exceptions.InvalidOperationException;
import tradable.OrderDTO;
import exceptions.InvalidArgumentException;
import exceptions.NullArgumentException;

import java.util.HashMap;

public final class UserManager {

    /** Singleton class instance. */
    private static UserManager instance;
    /** Store Users mapping the user ID to the User. */
    private final HashMap<String, User> users;

    public static UserManager getInstance() {
        // Create a new instance if the singleton instance doesn't already exist:
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private UserManager() { users = new HashMap<>(); }

    public void init(String[] userIds) throws NullArgumentException, InvalidArgumentException {
        if (userIds == null) { throw new NullArgumentException("Invalid userIds array argument: null"); }
        for (String id: userIds) { // Create all the new users and store them in the map.
            users.put(id, new User(id));
        }
    }

    public User getUser(String id) throws NullArgumentException {
        if (id == null) { throw new NullArgumentException("Invalid user id argument: null"); }
        return users.get(id);
    }

    public User getRandomUser() throws InvalidOperationException {
        if (this.users.size() == 0) { throw new InvalidOperationException("UserManager is empty with no users to return"); }
        User[] users = this.users.values().toArray(new User[0]); // Get all the users in an array.
        return users[(int)(Math.random() * users.length)]; // Pick a random user from the array.
    }

    public void addToUser(String userId, OrderDTO order) throws NullArgumentException, InvalidArgumentException {
        if (userId == null) { throw new NullArgumentException("Invalid userId argument: null"); }
        if (order == null) { throw new NullArgumentException("Invalid OrderDTO argument: null"); }

        // Get the user by id and add the new order.
        User user = users.get(userId);
        if (user != null) { // Double-check the user exists first.
            user.addOrder(order);
        } else {
            throw new InvalidArgumentException("Invalid userId argument: user id does not exist");
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("Users:"); // Add users section header.
        // If there are no users, then use empty placeholder.
        if (users.size() == 0) { return out.append(" <Empty>\n").toString(); }
        for (User u : users.values()) { // Add all order summaries.
            out.append("\n\n").append(u);
        }

        return out.toString();
    }
}
