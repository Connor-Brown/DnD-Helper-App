package my.dnd.app.model;

public class MonsterHolder {
	
	private Monster monster;
	private int count;
	
	public MonsterHolder() {}
	public MonsterHolder(Monster monster, int count) {
		this.monster = monster;
		this.count = count;
	}
	
	public Monster getMonster() {
		return monster;
	}
	public void setMonster(Monster monster) {
		this.monster = monster;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void increaseCount() {
		count++;
	}
	public void decreaseCount() {
		if(count <= 1)
			count = 0;
		else
			count--;
	}
	
}
