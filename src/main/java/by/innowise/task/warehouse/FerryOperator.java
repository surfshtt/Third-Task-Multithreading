package by.innowise.task.warehouse;

import by.innowise.task.entity.AbstractCar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class FerryOperator implements Callable<Integer> {

    private static final Logger logger = LogManager.getLogger();

    // ёмкость платформы
    private static final int AREA_AMOUNT = 300;
    private static final int WEIGHT_AMOUNT = 15000;

    @Override
    public Integer call() throws InterruptedException {
        FerryWarehouse warehouse = FerryWarehouse.getInstance();

        int totalArea = 0;
        int totalWeight = 0;
        int carsCount = 0;

        TimeUnit.SECONDS.sleep(3);
        logger.debug("Ferry warehouse is ready");

        while (true) {
            FerryWarehouse.WaitingCar waitingCar = warehouse.getCarFromQueue();

            if (waitingCar == null) {
                TimeUnit.SECONDS.sleep(1);
                continue;
            }

            AbstractCar car = waitingCar.getCar();

            if (AREA_AMOUNT >= totalArea + car.getAreaAmount()
                    && WEIGHT_AMOUNT >= totalWeight + car.getWeightAmount()) {

                warehouse.allowCar(waitingCar);

                TimeUnit.SECONDS.sleep(1);
                logger.debug("Car {} boarding now . . .", car.getId());
                TimeUnit.SECONDS.sleep(2);
                logger.info("Car {} successfully boarded", car.getId());
                TimeUnit.SECONDS.sleep(1);

                totalArea += car.getAreaAmount();
                totalWeight += car.getWeightAmount();
                carsCount++;

            } else {
                warehouse.backToQueue(waitingCar);
                break;
            }
        }

        logger.warn("Ferry was successfully loaded with {} cars", carsCount);
        return carsCount;
    }
}
