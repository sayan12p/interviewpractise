import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Interview {

    public static void main(String[] args) {
        System.out.println("hello");
        Thread smsthread=new Thread(new SMSThreadRunnable());
        Thread emailthread=new Thread(new EmailThreadRunnable());
        FutureTask<String> etatask=new FutureTask<String>(new EtaCalculator("blr"));
        Thread etathread=new Thread(etatask);
        System.out.println("task started");
        smsthread.start();
        System.out.println("task 1 ongoing");
        emailthread.start();
        System.out.println("task 2 ongoing");
        etathread.start();
       EmailService.mainexecute();
        try{
            smsthread.join();
            emailthread.join();
            String eta=etatask.get();
            System.out.println("ETA is " + eta);
            System.out.println("all tasks done");
        }catch (InterruptedException e){

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

class EtaCalculator implements Callable{

    private final String location;

    public EtaCalculator(String location) {
        this.location = location;
    }

    @Override
    public String call() throws Exception {
        System.out.println("[" +Thread.currentThread().getName()+ "] Calculating ETA to: "+location );
        Thread.sleep(20);
        return "ETA to " +location+" :20 mintutes";
    }
}

class EmailThreadRunnable implements Runnable {

    @Override
    public void run() {
        try{
            Thread.sleep(30);
            System.out.println("Email sent using Thread");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class SMSThreadRunnable implements Runnable {

    @Override
    public void run() {
        try{
            Thread.sleep(30);
            System.out.println("SMS sent using Thread");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


class EmailService{

    private static  final ExecutorService executor= Executors.newFixedThreadPool(10);

    public static void sendEmail(String receipient){
        executor.execute(()->{
            System.out.println("Sending email to "+ receipient+ " on "+  Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
                }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
            System.out.println("Email sent to : " +  receipient);
        });
    }

    public static void mainexecute(){
        for(int i=1;i<25;i++)
            sendEmail("user"+ i + "gmail.com");
        executor.shutdown();
    }
}
//Atomic operations
class PurchaseAtomicCounter{
    private final AtomicInteger likes=new AtomicInteger(0);

    public void increamentlikes(){
        int prev,next;
        do{
            prev=likes.get();
            next=prev+1;
        }while(!likes.compareAndSet(prev,next));
    }

    public int getcount(){
        return likes.get();
    }
}