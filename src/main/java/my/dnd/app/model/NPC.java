package my.dnd.app.model;

import java.util.ArrayList;
import java.util.List;

public class NPC {

	private int ac;
	private int hp;
	private String classname;
	private String subclass;
	private String race;
	private String gender;
	private int speed;
	private List<String> feats = new ArrayList<>();
	private int strength;
	private int dexterity;
	private int constitution;
	private int intelligence;
	private int wisdom;
	private int charisma;
	private int attackStat; // 0 = str, 1 = dex, 2 = con???, 3 = int, 4 = wis, 5 = cha
	private List<Item> inventory = new ArrayList<>();
	private int level;
	private int age;
	private int hitDice;
	
	@Override
	public String toString() {
		String s = "NPC[AC: "+ac+", HP: "+hp+", Level: "+level+", Class: "+classname
				+" ("+subclass+"), Race: "+race+", Age: "+age+", STR: "+strength
				+", DEX: "+dexterity+", CON: "+constitution+", INT: "+intelligence
				+", WIS: "+wisdom+", CHA: "+charisma+", Speed: "+speed+", Feats: "
				+feats+", Inventory: [";
		for(Item i : inventory) {
			s += i.getName() + " ("+i.getAmount()+"), ";
		}
		s = s.substring(0, s.length()-2);
		s += "]]";
		return s;
	}

	public int getAc() {
		return ac;
	}

	public void setAc(int ac) {
		this.ac = ac;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}
	
	public String getSubclass() {
		return subclass;
	}

	public void setSubclass(String subclass) {
		this.subclass = subclass;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public List<String> getFeats() {
		return feats;
	}

	public void setFeats(List<String> feats) {
		this.feats = feats;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getDexterity() {
		return dexterity;
	}

	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}

	public int getConstitution() {
		return constitution;
	}

	public void setConstitution(int constitution) {
		this.constitution = constitution;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}

	public int getWisdom() {
		return wisdom;
	}

	public void setWisdom(int wisdom) {
		this.wisdom = wisdom;
	}

	public int getCharisma() {
		return charisma;
	}

	public void setCharisma(int charisma) {
		this.charisma = charisma;
	}

	public int getAttackStat() {
		return attackStat;
	}

	public void setAttackStat(int attackStat) {
		this.attackStat = attackStat;
	}

	public List<Item> getInventory() {
		return inventory;
	}

	public void setInventory(List<Item> inventory) {
		this.inventory = inventory;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getHitDice() {
		return hitDice;
	}

	public void setHitDice(int hitDice) {
		this.hitDice = hitDice;
	}
}
