package my.dnd.app.model;

public class Item {
	
	private String name;
	private String type;
	private boolean isMagic;
	private double weight;
	private String rarity;
	private String text;
	private int amount;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isMagic() {
		return isMagic;
	}
	public void setMagic(boolean isMagic) {
		this.isMagic = isMagic;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getRarity() {
		return rarity;
	}
	public void setRarity(String rarity) {
		this.rarity = rarity;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
