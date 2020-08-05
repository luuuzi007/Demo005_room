package com.example.demo005_room.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * @author: Luuuzi
 * @date: 2020-08-04
 * @description: 2.即数据访问接口。可以将SQL查询语句与方法相关联。 @Dao注解一定要加上
 */

@Dao
public interface UserDao {
    //查询表里面所有数据
    @Query("SELECT * FROM user")
    List<User> getAll();

    //通过id去查询
    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> findAllByIds(int[] userIds);

    //插入
    @Insert
    void insetAll(List<User> users);

    //插入
    @Insert
    void inset(User user);

    //删除(根据主键去删除)
    @Delete
    void delete(User user);

    /**
     * 批量删除
     * @param users
     * @return 删除条数
     */
    @Delete
    int deletes(List<User> users);
    /**
     * 单条更新
     *
     * @param user 更新内容，根据uId去修改
     * @return 更新成功条数，更新一条，返回1 更新2条返回2
     */
    @Update
    int update(User user);

    /**
     * 批量更新
     *
     * @param users 更新内容，根据uId去修改
     * @return 更新成功条数，更新一条，返回1 更新2条返回2
     */
    @Update
    int updates(List<User> users);
}
