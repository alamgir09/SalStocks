package alamgir_CSCI201_Assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;

import com.google.gson.*;


public class StockScheduler {
	
	static Stocks stocks;
	
	static List<Book> book = new ArrayList<>();
	
	static Map<String, Semaphore> stockMap = new HashMap<String, Semaphore>();
	
	static List<Integer> balance = new ArrayList<Integer>();
	
	static Date start;
	
	static DateFormat simple;
	
	
	// constructor initializing everything 
	public StockScheduler()
	{
		stocks = null;
		balance = Collections.synchronizedList(new ArrayList<Integer>());
		stockMap = Collections.synchronizedMap(new HashMap<String, Semaphore>());
	}
	
	// the thing that checks for balance, get and set balance cause it'll be shared
	
	// referenced from this https://www.java67.com/2015/08/how-to-load-data-from-csv-file-in-java.html
	
	// csv parser
	public static void csvParser(Scanner input, Scanner sc)
	{
		//Scanner input = new Scanner(System.in);
		
		String fileName = "";
		
		boolean notParsed = true;
		
		while (notParsed)
		{
			try
			{
				notParsed = false;
				System.out.println("What is the name of the file containing the schedule information? ");
				fileName = input.nextLine();
				File file = new File(fileName);
				//Scanner sc = new Scanner(file);
				sc = new Scanner(file);
				
				while (sc.hasNext())
				{
					String[] fullLine = sc.nextLine().split(",");
					Book singleBook = createBook(fullLine);
					book.add(singleBook);
				}
				
				//System.out.println(stocks.getDatabyIndex(0).getName());
				//sc.close();
			}
			catch (FileNotFoundException exc)
			{
				System.err.println("The file " + fileName + " couldn't be found");
				System.out.println("");
				notParsed = true;
			}
		}
		System.out.println("");
		//input.close();
	}
	
	public static Book createBook(String[] metaData)
	{
		int startTime = Integer.parseInt(metaData[0]);
		String ticker = metaData[1];
		int tradeAmount = Integer.parseInt(metaData[2]);
		int stockPrice = Integer.parseInt(metaData[3]);
		
		return new Book(startTime, ticker, tradeAmount, stockPrice);
	}
	
	// json parser
	public static void jsonParser(Scanner input, Scanner sc)
	{
		//Stocks stocks = null;
		
		//Scanner input = new Scanner(System.in);
		
		String fileName = "";
		
		boolean notParsed = true;
		
		while (notParsed)
		{
			try 
			{
				notParsed = false;
				System.out.println("What is the name of the file containing company information? ");
				fileName = input.nextLine();
				File file = new File(fileName);
				//Scanner sc = new Scanner(file);
				sc = new Scanner(file);
				String temp = "";
				while (sc.hasNext())
				{
					temp += sc.nextLine();
				}
				
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				stocks = gson.fromJson(temp,Stocks.class);
				//System.out.println(stocks.getDatabyIndex(0).getName());
				//sc.close();
				
				// parse through stocks to see if anything is null or not (getName())
				
				for (Datum company : stocks.getData())
				{
					if (company.getName().equals(""))
					{
						System.err.println("Name is missing");
						System.out.println("");
						notParsed = true;
					}
					else if (company.getTicker().equals(""))
					{
						System.err.println("Ticker is missing");
						System.out.println("");
						notParsed = true;
					}
					else if (company.getStartDate().equals(""))
					{
						System.err.println("Start Date is missing");
						System.out.println("");
						notParsed = true;
					}
					else if (company.getDescription().equals(""))
					{
						System.err.println("Description is missing");
						System.out.println("");
						notParsed = true;
					}
					else if (company.getExchangeCode().equals(""))
					{
						System.err.println("Exchange Code is missing");
						System.out.println("");
						notParsed = true;
					}
					
				}
				
			}
			catch (JsonSyntaxException exc)
			{
				System.err.println("The file " + fileName + " isn't formatted properly");
				System.out.println("");
				notParsed = true;
				
			}
			catch (FileNotFoundException exc)
			{
				System.err.println("The file " + fileName + " couldn't be found");
				System.out.println("");
				notParsed = true;
			}
		}
		//System.out.println("The file has been properly read");
		System.out.println("");
		
		//input.close();
	}
	
	public static void balanceParser(Scanner input, Scanner sc)
	{
		boolean notParsed = true;
		while (notParsed)
		{
			try
			{
				notParsed = false;
				System.out.println("What is the initial balance?");
				balance.add(0,input.nextInt());
				if (balance.get(0) < 0)
				{
					System.out.println("That is not a valid option.");
					System.out.print("\n");
					input.nextLine();
					notParsed = true;
				}
			}
			catch (InputMismatchException exception)
			{
				System.out.println("That is not a valid option.");
				System.out.print("\n");
				input.nextLine();
				notParsed = true;
			}
		}
		System.out.println("");
		System.out.println("Initial balance: " + balance);
	}
	
	public static int getBalance(int balance)
	{
		return balance;
	}
	
	public static synchronized void setBalance(int balance)
	{
		StockScheduler.balance.set(0, balance);
	}
	
	// ends program
	public static void printBalance()
	{
		
		System.out.println("Current balance after trade: " + balance.get(0));
		
	}
	
	// main method - starts program after constructor
	public static void main(String[] args)
	{
		//Stocks stocks = null;
		
		Scanner input = new Scanner(System.in);
		Scanner sc = new Scanner(System.in);
		
		
		
		jsonParser(input, sc);
		//System.out.println("The name of the company is " + stocks.getDatabyIndex(0).getName());
		
		csvParser(input, sc);
		//System.out.println("The name of the first ticker is " + book.get(0).getTicker());
		
		balanceParser(input, sc);
		
		// milliseconds
		
		// Creating date format
		DateFormat simple = new SimpleDateFormat("[HH:mm:ss:SSS] ");
		simple.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		// Creating date from milliseconds
		// using Date() constructor
		Date start = new Date();
		
		// Formatting Date according to the
		// given format
		
//		Date result = new Date();
//		Long dateNew = result.getTime()-start.getTime();
//		System.out.println(simple.format(dateNew));
		
		// iterate through tickers in object to find the number of stock brokers and initialize semaphores
		
		for (Datum company : stocks.getData())
		{
			Semaphore sem = new Semaphore(Integer.parseInt(company.getStockBrokers()));
			stockMap.put(company.getTicker(), sem);
			
		}
		ExecutorService executors = Executors.newCachedThreadPool();
		
		stockThread thread = null;
		
		// iterate through book object, and then call trade threads
		for (Book company : book)
		{
			
			thread = new stockThread(stockMap.get(company.getTicker()), company.getTicker(), company.getTradeAmount(), balance, company.getStockPrice(), start);
			executors.execute(thread);
		}
		
		executors.shutdown();
		while(!executors.isTerminated())
		{
			Thread.yield();
		}
		
		System.out.println("All Trades completed!");
		
		input.close();
		sc.close();
		// the idea is one semaphore per stock ticker
	}

}
