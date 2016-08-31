package org.quickjedis.test;

public class UserInfo {

	private String name;
	private int age;
	private Boolean isMale;

	public UserInfo(String name, int age, Boolean ismale) {
		this.name = name;
		this.age = age;
		this.isMale = ismale;
		System.out.println("UserInfo Create");
	}

	public UserInfo() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Boolean getIsMale() {
		return isMale;
	}

	public void setIsMale(Boolean ismale) {
		this.isMale = ismale;
	}

}
