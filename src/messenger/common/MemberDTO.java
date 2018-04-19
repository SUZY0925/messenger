package messenger.common;

import java.util.ArrayList;

public class MemberDTO {
	private String id, passwd, name, alias, loc, sex, age, birth, phone;
	private ArrayList<String> idA, nameA, aliasA;
	
	public ArrayList<String> getIdA() {
		return idA;
	}

	public void setIdA(ArrayList<String> idA) {
		this.idA = idA;
	}

	public ArrayList<String> getNameA() {
		return nameA;
	}

	public void setNameA(ArrayList<String> nameA) {
		this.nameA = nameA;
	}

	public ArrayList<String> getAliasA() {
		return aliasA;
	}

	public void setAliasA(ArrayList<String> aliasA) {
		this.aliasA = aliasA;
	}

	public MemberDTO() {
	}
	
	public MemberDTO(String id, String passwd, String name, String loc, String sex, String age, String birth,
			String phone) {
		this.id = id;
		this.passwd = passwd;
		this.name = name;
		this.loc = loc;
		this.sex = sex;
		this.age = age;
		this.birth = birth;
		this.phone = phone;
	}

	public MemberDTO(String id, String passwd, String name, String alias, String loc, String sex, String age,
			String birth, String phone) {
		this.id = id;
		this.passwd = passwd;
		this.name = name;
		this.alias = alias;
		this.loc = loc;
		this.sex = sex;
		this.age = age;
		this.birth = birth;
		this.phone = phone;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}

	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[MemberDTO] \r\n")
		.append("id :"+id+"\r\n")
		.append("passwd :"+passwd+"\r\n")
		.append("name :"+name+"\r\n")
		.append("loc :"+loc+"\r\n")
		.append("age :"+age+"\r\n")
		.append("birth :"+birth+"\r\n")
		.append("phone :"+phone+"\r\n");
		System.out.println(sb);
		return sb.toString();
	}

}