package by.innowise.task.entity;

import by.innowise.task.warehouse.FerryWarehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

public class LightCar extends AbstractCar implements Callable<Boolean> {
    private static final Logger logger = LogManager.getLogger();

    public LightCar(long id) {
        super(id, 6, 1700);
    }

    @Override
    public Boolean call() throws InterruptedException {
        FerryWarehouse warehouse = FerryWarehouse.getInstance();

        boolean permitted = warehouse.addToCarsQueue(this);
        if (!permitted) {
            logger.error("Light car {} didn't get permission to board", getId());
            return false;
        }

        logger.info("Light car {} received permission to board", getId());
        return true;
    }
}
