package com.example.demo005_room.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @author: Luuuzi
 * @date: 2020-08-04
 * @description: 3.SQLite数据库之上的数据库层，负责处理以前使用SQLiteOpenHelper处理的普通任务。
 * exportSchema = false:去除警告 version版本号
 */
@Database(entities = {User.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
