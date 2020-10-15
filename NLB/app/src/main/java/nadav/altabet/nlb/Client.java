package nadav.altabet.nlb;

public class Client {

    private static User currentUser;
    private static String UID = "";

    public static String getUID() {
        return UID;
    }

    public static void setUID(String UID) {
        Client.UID = UID;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Client.currentUser = currentUser;
    }
}
