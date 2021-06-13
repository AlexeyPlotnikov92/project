package test;

public class Main {
    public static void main(String[] args) {
        Test test = new Test();
        if (!test.loadDriver()) return;
        if (!test.getConnection()) return;

        test.createTable();
        test.fillTable();
        test.printTable();
        test.closeConnection();
    }
}
