package alamgir_CSCI201_Assignment2;

import java.util.List;

public class Stocks {
	
	private List<Datum> data = null;
	
	public List<Datum> getData() {
	return data;
	}
	
	public void setData(List<Datum> data) {
	this.data = data;
	}
	
	public Datum getDatabyIndex(int index)
	{
		return data.get(index);
	}
	
}
