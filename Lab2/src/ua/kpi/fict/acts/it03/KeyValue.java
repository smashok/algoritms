package ua.kpi.fict.acts.it03;

import javafx.beans.property.SimpleStringProperty;

public class KeyValue {
    private SimpleStringProperty key = new SimpleStringProperty();
    private SimpleStringProperty value = new SimpleStringProperty();

    KeyValue(String key, String value)
    {
        this.key.set(key);
        this.value.set(value);
    }

    public String getKey() {
        return key.get();
    }

    public SimpleStringProperty keyProperty() {
        return key;
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public String getValue() {
        return value.get();
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
}
