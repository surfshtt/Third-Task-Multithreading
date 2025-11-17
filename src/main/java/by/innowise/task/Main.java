package by.innowise.task;

import by.innowise.task.entity.HeavyCar;
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

        ExecutorService ferryExecutor = Executors.newCachedThreadPool();
        ferryExecutor.submit(new FerryOperator());

        ExecutorService carsExecutor = Executors.newCachedThreadPool();
        List<Future<Boolean>> carResults = new ArrayList<>();

        int i = 1;
        while (carResults.size() != 12) {
            if(i%2 == 0) {
                carResults.add(carsExecutor.submit(new LightCar(i)));
            }
            else{
                carResults.add(carsExecutor.submit(new HeavyCar(i)));
            }

            i++;
        }
        for (Future<Boolean> carResult : carResults) {
            carResult.get();
        }

        ferryExecutor.shutdown();
    }
}
