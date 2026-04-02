package component;

public interface DataSource {
    void writeData(String data);

    String readData();
}
