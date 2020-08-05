package com.example.demo005_room.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author: Luuuzi
 * @date: 2020-08-04
 * @description: 1.创建Entity
 */
//@Entity(tableName = "user") 指定表名
@Entity
public class User {
    //主键 autoGenerate = true:自动生成
    @PrimaryKey
    public int uId;
    @ColumnInfo(name = "first_name")
    public String firstName;
    @ColumnInfo(name = "last_name")
    public String lastName;

//    public User(int uId) {
//        this.uId = uId;
//    }

    public User(int uId, String firstName, String lastName) {
        this.uId=uId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "uId=" + uId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
