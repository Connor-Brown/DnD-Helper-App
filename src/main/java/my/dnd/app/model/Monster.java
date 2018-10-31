package my.dnd.app.model;

public class Monster {
	
	private String name;
	private String size;
	private String type;
	private String alignment1;
	private String alignment2;
	private int ac;
	private int averageHp;
	private int actualHp;
	private int numDice;
	private int diceSize;
	private int bonusHp;
	private double cr;
	private boolean isLegendary;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAlignment1() {
		return alignment1;
	}
	public void setAlignment1(String alignment1) {
		this.alignment1 = alignment1;
	}
	public String getAlignment2() {
		return alignment2;
	}
	public void setAlignment2(String alignment2) {
		this.alignment2 = alignment2;
	}
	public int getAc() {
		return ac;
	}
	public void setAc(int ac) {
		this.ac = ac;
	}
	public int getAverageHp() {
		return averageHp;
	}
	public void setAverageHp(int averageHp) {
		this.averageHp = averageHp;
	}
	public int getActualHp() {
		return actualHp;
	}
	public void setActualHp(int actualHp) {
		this.actualHp = actualHp;
	}
	public int getNumDice() {
		return numDice;
	}
	public void setNumDice(int numDice) {
		this.numDice = numDice;
	}
	public int getDiceSize() {
		return diceSize;
	}
	public void setDiceSize(int diceSize) {
		this.diceSize = diceSize;
	}
	public int getBonusHp() {
		return bonusHp;
	}
	public void setBonusHp(int bonusHp) {
		this.bonusHp = bonusHp;
	}
	public double getCr() {
		return cr;
	}
	public void setCr(double cr) {
		this.cr = cr;
	}
	public boolean isLegendary() {
		return isLegendary;
	}
	public void setLegendary(boolean isLegendary) {
		this.isLegendary = isLegendary;
	}
	
}
