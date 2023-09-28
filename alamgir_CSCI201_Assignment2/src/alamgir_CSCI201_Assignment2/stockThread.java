package alamgir_CSCI201_Assignment2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.*;

public class stockThread extends Thread{

	private Semaphore sem;

	private String ticker;

	private int quantity;
	
	private int stockPrice;

	static int currBalance;
	
	private Date start;
	
	
	public stockThread(Semaphore sem, String ticker, int quantity, List<Integer> currBalance, int stockPrice, Date start)
	{
		this.sem = sem;
		this.ticker = ticker;
		this.quantity = quantity;
//		stockThread.currBalance = currBalance;
		this.stockPrice = stockPrice;
		this.start = start;
		
	}
	
	public static synchronized void setBalance(int currBalance) throws InterruptedException
	{
		StockScheduler.setBalance(currBalance);
	}
		
	
	public void run()
	{
//		synchronized (this)
//		{
	
			DateFormat simple = new SimpleDateFormat("[HH:mm:ss:SSS] ");
			simple.setTimeZone(TimeZone.getTimeZone("UTC"));
			int temp;
			try {
				sem.acquire();
				// selling
				String transaction = "";
				if (quantity < 0)
				{
					Date result = new Date();
					Long dateNew = result.getTime()-start.getTime();
					temp = quantity*-1;
					System.out.println(simple.format(dateNew)+ "Starting sale of " + temp + " stocks of " + ticker);
					Thread.sleep(3000);
					transaction = "sale";
					int currBalance = StockScheduler.balance.get(0) - stockPrice*quantity;
			//					currBalance += stockPrice*quantity;
					StockScheduler.setBalance(currBalance);
					// output the current balance + time stamp
					
				}
				// buying
				else {
					Date result = new Date();
					Long dateNew = result.getTime()-start.getTime();
					temp = quantity;
					System.out.println(simple.format(dateNew)+ "Starting purchase of " + quantity + " stocks of " + ticker);
					Thread.sleep(2000);
					transaction = "purchase";
					int currBalance = StockScheduler.balance.get(0) - stockPrice*quantity;
			//					currBalance -= stockPrice*quantity;
					if (currBalance < 0)
					{
						System.out.println("Transaction failed due to insufficient balance. Unsuccessful purchase of " + temp + " stocks of " + ticker);
						return;
					}else
					{
						StockScheduler.setBalance(currBalance);
					}
				}
				Date result = new Date();
				Long dateNew = result.getTime()-start.getTime();
				sem.release();
				System.out.println(simple.format(dateNew) + "Finished " + transaction + " of " + temp + " of " + ticker);
				StockScheduler.printBalance();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
//	}
}
