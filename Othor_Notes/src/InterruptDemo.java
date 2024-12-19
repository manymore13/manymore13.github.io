package src;

class InterruptDemo extends Thread {

    @Override
    public void run() {
        super.run();
        while (true) {
            if (isInterrupted()) {
                System.out.println("interruted");
                break;
            }
            System.out.println(Thread.currentThread().isInterrupted());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();

                interrupt();
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        InterruptDemo threadDemo = new InterruptDemo();
        threadDemo.start();
        Thread.sleep(5000);
        threadDemo.interrupt();
    }
}
