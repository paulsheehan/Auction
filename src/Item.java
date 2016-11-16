
public class Item {
	
	private String name;
	private float price;
	private String brand;
	
	public Item(String name, float price, String brand) {
		setName(name);
		setPrice(price);
		setBrand(brand);
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public String toString() {
		String itemInfo = "";
		double seconds;
		seconds = System.currentTimeMillis()/1000.0;
		itemInfo = getName() + " " + "$" + getPrice() + " " + "by " + getBrand() + " TIME: " + seconds;
		
		return itemInfo;
	}
}