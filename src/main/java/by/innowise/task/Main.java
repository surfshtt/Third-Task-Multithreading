package by.innowise.task;

import by.innowise.task.entity.LightCar;
import by.innowise.task.warehouse.FerryOperator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> ferryFuture = executor.submit(new FerryOperator());

        List<Future<Boolean>> carResults = new ArrayList<>();

        int i = 0;
        while (carResults.size() != 20) {
            carResults.add(executor.submit(new LightCar(i)));
            i++;
        }
        for (Future<Boolean> carResult : carResults) {
            carResult.get();
        }

        Integer loadedCars = ferryFuture.get();
        logger.info("Ferry loaded cars: {}", loadedCars);

        executor.shutdown();
    }
}
