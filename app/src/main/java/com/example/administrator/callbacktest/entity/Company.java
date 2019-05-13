package com.example.administrator.callbacktest.entity;

public class Company {
    private String id ;
    private String name;

    public Company(){}
    public Company(String id , String name){
        this.id =id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age){

    }
    public int getAge(){
            return 23;
    }
    @Override
    public String toString() {
        return "name:"+name+"___  id:"+id;

    }
}
