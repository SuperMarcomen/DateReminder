public class Main {
    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.createTable();

        //Start
        System.out.println("Daily Reminder");
        databaseManager.check();
        System.out.println();
        databaseManager.askWhatToDo();
    }
}
