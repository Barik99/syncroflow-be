package vlad.mester.syncroflowbe.services;

public enum DataBase {

    URL("jdbc:postgresql://localhost:5432/syncroflowdb"),
    USERNAME("postgres"),
    PASSWORD("admin");

    private final String connectionDataBase;

    DataBase(String connectionDataBase) {
        this.connectionDataBase = connectionDataBase;
    }

    @Override
    public String toString() {
        return connectionDataBase;
    }
}
