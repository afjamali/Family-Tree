/**
Afshin Jamali
Java 2572
12/02/11
Assignment 7
FamilyMember.java
*/


import java.util.ArrayList;

public class FamilyMember {
	
	// Declare variables
	private String name, spouse, nationality, state;
	private int age;
	private ArrayList<String> children = new ArrayList<String>();
	
	// Default no-arg constructor
	public FamilyMember() {
		
	}
	
	// Constructor with name
	public FamilyMember(String name) {
		this.name = name;
	}
	
	// Constructor with name, age, nationality, and state
	public FamilyMember(String name, int age, String spouse, String nationality, String state) {
		this.name = name;
		this.age = age;
		this.spouse = spouse;
		this.nationality = nationality;
		this.state = state;
	}
	
	// Setters
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSpouse(String spouse) {
		this.spouse = spouse;
	}
	
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public void setChildren(String child) {			
		children.add(child);		
	}

	// Getters
	public String getName() {
		return name;		
	}
	
	public String getSpouse() {
		return spouse;
	}
	
	public String getNationality() {
		return nationality;
	}
	
	public String getState() {
		return state;
	}
	
	public int getAge() {
		return age;
	}
	
	public ArrayList<String> getChildren() {
		return children;
	}
	
	public int getNumChildren() {
		return children.size();
	}
	
	public String toString() {
		return String.format(
				"\nName:" + "\t" + name + 
				"\nAge:" + "\t" + age + 
				"\nSpouse:" + "\t" + spouse +
				"\nChildren:" + "\t" + children + 
				"\nNationality:" + "\t" + nationality +
				"\nResidence:" + "\t" + state + "\n");
	}
}
