package my.dnd.app.model;

import java.util.List;

public class Spell {
	
	private String name;
	private int level;
	private String school;
	private String duration;
	private String range;
	private String castingTime;
	private List<String> classes;
	private String text;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getCastingTime() {
		return castingTime;
	}
	public void setCastingTime(String castingTime) {
		this.castingTime = castingTime;
	}
	public List<String> getClasses() {
		return classes;
	}
	public void setClasses(List<String> classes) {
		this.classes = classes;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
