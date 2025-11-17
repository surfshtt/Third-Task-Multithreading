package by.innowise.task.warehouse;

import by.innowise.task.entity.AbstractCar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FerryOperator implements Callable<Integer> {
    private static final Logger logger = LogManager.getLogger();

    private static final int AREA_AMOUNT = 300;
    private static final int WEIGHT_AMOUNT = 15000;

    private static int totalCarsCount = 0;

    @Override
    public Integer call() throws InterruptedException {
        FerryWarehouse warehouse = FerryWarehouse.getInstance();

        int curretnArea = 0;
        int currentWeight = 0;
        int currentCarsCount = 0;

        int waitingTimeTicker = 0;

        TimeUnit.SECONDS.sleep(1);
        logger.debug("Ferry operator started");
        TimeUnit.SECONDS.sleep(1);

        while (true) {
            FerryWarehouse.WaitingCar waitingCar = warehouse.getCarFromQueue();

            if (waitingCar == null) {
                TimeUnit.SECONDS.sleep(1);
                waitingTimeTicker++;

                if(waitingTimeTicker >= 5){
                    logger.info("Ferry swam away with {} cars because of long waiting",currentCarsCount);
                    logger.debug("Total cars transported {}", totalCarsCount);
                    return totalCarsCount;
                }

                continue;
            }
            waitingTimeTicker = 0;

            AbstractCar car = waitingCar.getCar();
            if (AREA_AMOUNT >= curretnArea + car.getAreaAmount()
                    && WEIGHT_AMOUNT >= currentWeight + car.getWeightAmount()) {

                warehouse.allowCar(waitingCar);

                TimeUnit.SECONDS.sleep(1);
                logger.debug("Car {} boarding now . . .", car.getId());
                TimeUnit.SECONDS.sleep(2);
                logger.info("Car {} successfully boarded", car.getId());
                TimeUnit.SECONDS.sleep(1);

                curretnArea += car.getAreaAmount();
                currentWeight += car.getWeightAmount();

                currentCarsCount++;
                totalCarsCount++;

            } else {
                warehouse.backToQueue(waitingCar);
                break;
            }
        }

        logger.warn("Ferry was successfully loaded with {} cars", currentCarsCount);
        TimeUnit.SECONDS.sleep(3);
        logger.debug("Ferry unloads . . . ");
        TimeUnit.SECONDS.sleep(3);

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new FerryOperator());

        return totalCarsCount;
    }
}
