package by.innowise.task.entity;

import by.innowise.task.warehouse.FerryWarehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

public class HeavyCar extends AbstractCar implements Callable<Boolean> {
    private static final Logger logger = LogManager.getLogger();

    public HeavyCar(long id) {
        super(id, 12, 3500);
    }

    @Override
    public Boolean call() throws InterruptedException {
        FerryWarehouse warehouse = FerryWarehouse.getInstance();

        boolean permitted = warehouse.addToCarsQueue(this);
        if (!permitted) {
            logger.error("Heavy car {} didn't get permission to board", getId());
            return false;
        }

        logger.info("Heavy car {} received permission to board", getId());
        return true;
    }
}
