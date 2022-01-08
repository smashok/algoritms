package ua.kpi.fict.acts.it03;

import java.io.IOException;

public interface IndexInterface {

    void set(String key, String value) throws IOException;

    void delete(String key) throws IOException;

    String get(String key) throws IOException;
}
