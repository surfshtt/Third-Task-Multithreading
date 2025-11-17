package by.innowise.task.entity;

public abstract class AbstractCar {
    private long id;
    private int areaAmount;
    private int weightAmount;

    public AbstractCar(long id, int spaceAmount, int weightAmount) {
        this.id = id;
        this.areaAmount = spaceAmount;
        this.weightAmount = weightAmount;
    }

    public long getId() {
        return id;
    }

    public int getAreaAmount() {
        return areaAmount;
    }

    public int getWeightAmount() {
        return weightAmount;
    }
}
