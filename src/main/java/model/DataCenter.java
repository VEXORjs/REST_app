package model;

import java.util.Objects;
import java.util.UUID;

public class DataCenter {
    private final UUID id =  UUID.randomUUID();
    private String name;
    private String location;
    private double area;
    private int powerCapacity;

    public DataCenter() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getPowerCapacity() {
        return powerCapacity;
    }

    public void setPowerCapacity(int powerCapacity) {
        this.powerCapacity = powerCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataCenter)) return false;
        DataCenter that = (DataCenter) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}