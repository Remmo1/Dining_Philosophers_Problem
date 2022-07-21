package z4;

import java.util.Random;
import java.util.concurrent.Semaphore;

class DiningPhilosophers {
    private static final int NUMBER_OF_PHILOSOPHERS = 5;
    private static final int MEDITATING_TIME = 1000;
    private static final int EATING_TIME = 1000;

    private static final Stick[] sticks = new Stick[NUMBER_OF_PHILOSOPHERS];
    private static final Philosopher[] philosophers = new Philosopher[NUMBER_OF_PHILOSOPHERS];

    private static final Random random = new Random();
    private static final Semaphore hotelPorter = new Semaphore(NUMBER_OF_PHILOSOPHERS - 1, true);


    static class Philosopher extends Thread {
        private final Stick left, right;

        Philosopher(int number) {
            super("Philosopher " + (number + 1));
            right = sticks[number];
            left = sticks[(number + 1) % NUMBER_OF_PHILOSOPHERS];
        }


        void eat() throws InterruptedException {
            hotelPorter.acquire();
            left.take();
            right.take();

            System.out.println(getName() + " is eating");
            sleep(random.nextInt(EATING_TIME));
            System.out.println(getName() + " will be meditating");

            left.release();
            right.release();
            hotelPorter.release();
        }

        @Override
        public void run() {
            int TRY_TO_EAT = 3;
            try {
                while (TRY_TO_EAT > 0) {
                    System.out.println(getName() + " is meditating");
                    sleep(random.nextInt(MEDITATING_TIME));
                    System.out.println(getName() + " will eat");
                    eat();
                    TRY_TO_EAT--;
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        for (int i = 0; i < NUMBER_OF_PHILOSOPHERS; i++)
            sticks[i] = new Stick();

        for (int i = 0; i < NUMBER_OF_PHILOSOPHERS; i++)
            philosophers[i] = new Philosopher(i);

        for (Philosopher p :
                philosophers) {
            p.start();
        }
    }
}