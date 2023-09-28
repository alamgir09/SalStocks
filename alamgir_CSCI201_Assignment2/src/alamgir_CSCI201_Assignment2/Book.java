package alamgir_CSCI201_Assignment2;

public class Book {
	
	private int startTime;
	
	private String ticker;
	
	private int tradeAmount;
	
	private int stockPrice;
	
	public Book(int startTime, String ticker, int tradeAmount, int stockPrice)
	{
		this.startTime = startTime;
		this.ticker = ticker;
		this.tradeAmount = tradeAmount;
		this.stockPrice = stockPrice;
	}
	
	public int getStartTime() {
	return startTime;
	}
		
	public void setStartTime(int startTime) {
	this.startTime = startTime;
	}
	
	public String getTicker() {
	return ticker;
	}
	
	public void setTicker(String ticker) {
	this.ticker = ticker;
	}
	
	public int getTradeAmount() {
	return tradeAmount;
	}
	
	public void setTradeAmount(int tradeAmount) {
	this.tradeAmount = tradeAmount;
	}
	
	public int getStockPrice() {
	return stockPrice;
	}
	
	public void setStockPrice(int stockPrice) {
	this.stockPrice = stockPrice;
	}
}
