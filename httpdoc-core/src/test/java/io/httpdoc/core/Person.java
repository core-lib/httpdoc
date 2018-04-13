package io.httpdoc.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 15:53
 **/
public class Person {
    /**
     * 性别
     * MALE: 男性
     * FEMALE: 女性
     */
    private Gender gender;
    private int age;
    private String name;
    private Person father;
    private Date birthday;
    private List<Person[]> children;
    private String[] tags;
    private Map<String, Value> attribute;

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getFather() {
        return father;
    }

    public void setFather(Person father) {
        this.father = father;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Person[]> getChildren() {
        return children;
    }

    public void setChildren(List<Person[]> children) {
        this.children = children;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public Map<String, Value> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, Value> attribute) {
        this.attribute = attribute;
    }
}
