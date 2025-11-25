package by.innowise.task.warehouse;

import by.innowise.task.entity.AbstractCar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FerryWarehouse {
    private static final Logger logger = LogManager.getLogger();

    private final ReentrantLock lock = new ReentrantLock(true);
    private final LinkedList<WaitingCar> carsQueue = new LinkedList<>();

    private FerryWarehouse() {}

    private static class SingletonHelper {
        private static final FerryWarehouse INSTANCE = new FerryWarehouse();
    }

    public static FerryWarehouse getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public class WaitingCar {
        final AbstractCar car;
        boolean allowed = false;
        final Condition condition;

        WaitingCar(AbstractCar car, Condition condition) {
            this.car = car;
            this.condition = condition;
        }

        public AbstractCar getCar() {
            return car;
        }
    }

    public boolean addToCarsQueue(AbstractCar car) throws InterruptedException {
        lock.lock();
        try {
            Condition condition = lock.newCondition();
            WaitingCar waitingCar = new WaitingCar(car, condition);

            carsQueue.addLast(waitingCar);
            TimeUnit.SECONDS.sleep(1);
            logger.info("Car {} was add to the queue", car.getId());

            while (!waitingCar.allowed) {
                condition.await();
            }

            return true;
        } finally {
            lock.unlock();
        }
    }

    public WaitingCar getCarFromQueue() {
        lock.lock();
        try {
            return carsQueue.pollFirst();
        } finally {
            lock.unlock();
        }
    }

    public void backToQueue(WaitingCar waitingCar) {
        lock.lock();
        try {
            carsQueue.addFirst(waitingCar);
        } finally {
            lock.unlock();
        }
    }

    public void allowCar(WaitingCar waitingCar) {
        lock.lock();
        try {
            waitingCar.allowed = true;
            waitingCar.condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
