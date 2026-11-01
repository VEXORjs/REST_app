package model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class GPUs {
    private final UUID id =  UUID.randomUUID();
    private float theoreticalPerformance;
    private BigDecimal price;
    private String name;
    private Date releaseDate;
    private String gpuDie;

    public GPUs(){
    }


    public UUID getId() {
        return id;
    }
    public float getTheoreticalPerformance() {
        return theoreticalPerformance;
    }
    public void setTheoreticalPerformance(float theoreticalPerformance) {
        this.theoreticalPerformance = theoreticalPerformance;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
    public String getGpuDie(){
        return gpuDie;
    }
    public void setGpuDie(String gpuDie){
        this.gpuDie = gpuDie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GPUs)) return false;
        GPUs gpu = (GPUs) o;
        return Objects.equals(id, gpu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
