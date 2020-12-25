# 1、简介
Room 在 SQLite 上提供了一个抽象层，以便在充分利用SQLite的强大功能的同时，能够流畅地访问数据库。

处理大量结构化数据的应用可极大地受益于在本地保留这些数据。最常见的用例是缓存相关数据。这样，当设备无法访问网络时，用户仍可在离线状态下浏览相应内容。设备重新连接到网络后，用户发起的所有内容更改都会同步到服务器。

- Room使用DAO查询数据库
- 默认情况下，为了提高UI性能，Room不允许在主线程中执行查询数据库操作。LiveData在后台线程上异步更新数据。
- Room 提供SQLite语句编译时检查
- 自定义的Room类必须是抽象类且必须继承RoomDatabase
- 通常，在整个APP中，只需要一个Room database实例。

# 2、room的三个重要组件

- @Entity: 用来注解实体类， @Database 通过entities属性引用被 @Entity注解的类，并利用该类的所有字段作为表的列名来创建表。  
- @Dao： 用来注解一个接口或者抽象方法，该类的作用是提供访问数据库的方法。在使用 @Database注解的类中必须定一个不带参数的方法，这个方法返回使用 @Dao注解的类。
- @Database: 用来注解类，并且注解的类必须是继承自RoomDatabase的抽象类。该类主要作用是创建数据库和创建Daos（data access objects，数据访问对象）。

# 3、使用
### 导入room库

```gradle
allprojects {
    repositories {
        maven {url 'https://maven.google.com' }//添加moven
        google()
        jcenter()
        
    }
}
```

```gradle
//    导入room库
    def room_version = "1.1.1"
    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation 'android.arch.persistence.room:runtime:room_version'
    annotationProcessor 'android.arch.persistence.room:compiler:room_version'
```
### 1、创建实体类
@Entity注解的类，并利用该类的所有字段作为表的列名来创建表。

```java
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
```
### 2、创建接口定义访问数据库的方法

```java
@Dao
public interface UserDao {
    //查询表里面所有数据
    @Query("SELECT * FROM user")
    List<User> getAll();

    //通过id去查询
    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> findAllByIds(int[] userIds);
    
    //模糊查询：查找字段first_name包含指定内容的字段
    @Query("SELECT * FROM user WHERE first_name LIKE '%'|| (:str)||'%'")
    List<User> getLike(String str);
    
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
```

### 3、创建访问数据库的对象
该类主要作用是创建数据库和创建Daos（data access objects，数据访问对象）
```java
/**
 * @author: Luuuzi
 * @date: 2020-08-04
 * @description: 3.SQLite数据库之上的数据库层，负责处理以前使用SQLiteOpenHelper处理的普通任务。
 * exportSchema = false:去除警告 version版本号：版本号要>=1
 */
@Database(entities = {User.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
```

## 调用
==注意：通常，在整个APP中，只需要一个Room database实例(一般设置成单例)==
```java
/**
 * @author: Luuuzi
 * @date: 2020-08-04
 * @description: room的使用
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final String tag = MainActivity.class.getSimpleName();
    private EditText et_first_name;
    private EditText et_last_name;
    private EditText et_first_name2;
    private EditText et_last_name2;
    private TextView tv_content;
    AppDatabase db;
    private EditText et_uid1;
    private EditText et_uid2;
    private EditText et_uid3;
    private EditText et_last_name3;
    private EditText et_first_name3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建db对象，用于操作数据（通常，在整个APP中，只需要一个Room database实例。)
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name")//database-name:数据库文件名称
                .allowMainThreadQueries()//允许在主线程中查询
                .build();

        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_uid1 = findViewById(R.id.et_uid1);
        et_first_name2 = findViewById(R.id.et_first_name2);
        et_last_name2 = findViewById(R.id.et_last_name2);
        et_uid2 = findViewById(R.id.et_uid2);
        et_first_name3 = findViewById(R.id.et_first_name3);
        et_last_name3 = findViewById(R.id.et_last_name3);
        et_keyword = findViewById(R.id.et_keyword);
        et_uid3 = findViewById(R.id.et_uid3);
        tv_content = findViewById(R.id.tv_content);

        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_inquiry).setOnClickListener(this);
        findViewById(R.id.btn_inquiry2).setOnClickListener(this);
        findViewById(R.id.btn_updata).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                User user = new User(Integer.parseInt(et_uid1.getText().toString()), et_first_name.getText().toString(), et_last_name.getText().toString());
                db.userDao().inset(user);
                break;
            case R.id.btn_delete:
//                User user2 = new User(Integer.parseInt(et_uid2.getText().toString()), et_first_name2.getText().toString(), et_last_name2.getText().toString());
//                db.userDao().delete(user2);
                //测试批量删除
                User user44 = new User(4, "da", "da");
                User user55 = new User(5, "而且", "哦");
                User user58 = new User(58, "而且", "dfa");
                ArrayList<User> users2 = new ArrayList<>();
                users2.add(user44);
                users2.add(user55);
                users2.add(user58);
                int deletes = db.userDao().deletes(users2);
                Log.i(tag, "删除条数：" + deletes);
                break;
            case R.id.btn_updata:
//                User user3 = new User(Integer.parseInt(et_uid3.getText().toString()), et_first_name3.getText().toString(), et_last_name3.getText().toString());
//                db.userDao().update(user3);
                //测试批量更新
                Log.i(tag, "批量更新");
                User user4 = new User(2, "你飞而去好", "嘿嘿");
                User user5 = new User(3, "发儿", "是的");
                User user6 = new User(4, "你大法", "已看");
                ArrayList<User> users = new ArrayList<>();
                users.add(user4);
                users.add(user5);
                users.add(user6);
                int update = db.userDao().updates(users);
                Log.i(tag, "更新结果：" + update);
                break;
            case R.id.btn_inquiry:
                List<User> all = db.userDao().getAll();
                StringBuffer stringBuffer = new StringBuffer();
                for (User u : all) {
                    stringBuffer.append("").append(u).append(",").append("\n");
                }
                tv_content.setText(stringBuffer);
                break;
            case R.id.btn_inquiry2://模糊查询
                List<User> 发 = db.userDao().getLike(et_keyword.getText().toString());
                StringBuffer stringBuffer2 = new StringBuffer();
                for (User u : 发) {
                    stringBuffer2.append("").append(u).append(",").append("\n");
                }
                tv_content.setText(stringBuffer2);
                break;
        }
    }
}
```
# 4、其他用法
## 1、关于查询
模糊查询查询可以用like或glob,具体用法

- LIKE  
LIKE用来匹配通配符指定模式的文本值。如果搜索表达式与模式表达式匹配，LIKE 运算符将返回真（true），也就是 1。这里有两个通配符与 LIKE 运算符一起使用,百分号（%）代表零个、一个或多个数字或字符。下划线（_）代表一个单一的数字或字符。这些符号可以被组合使用。


```
1、查找字段A以AAA开头的任意值
　　select * from table_name where 字段A like 'AAA%'
2、查找字段A任意位置包含AAA的任意值
　　select * from table_name where 字段A like '%AAA%'
3、查找字段A第二位和第三位为 AA 的任意值
　　select *from table_name where 字段A like '_AA%'
4、查找字段A以 A 开头，且长度至少为 3 个字符的任意值
　　select * from table_name where 字段A like 'A_%_%'
5、查找字段A以 A 结尾的任意值
　　select *from table_name where 字段A like '%A'
6、查找字段A第二位为 A，且以 B 结尾的任意值
　　select *from table_name where 字段A like '_A%B'
7、查找字段A长度为 5 位数，且以 A 开头以 B 结尾的任意值(A,B中间三个下划线)
　　select *from table_name where 字段A like 'A___B'
```


- GLOB  
SQLite 的 GLOB 运算符是用来匹配通配符指定模式的文本值。如果搜索表达式与模式表达式匹配，GLOB 运算符将返回真（true），也就是 1。与 LIKE 运算符不同的是，GLOB 是大小写敏感的，对于下面的通配符，它遵循 UNIX 的语法。  
星号（*）代表零个、一个或多个数字或字符。问号（?）代表一个单一的数字或字符。这些符号可以被组合使用。

```
1、查找字段A以AAA开头的任意值
　　select * from table_name where 字段A GLOB 'AAA*'
2、查找字段A任意位置包含AAA的任意值
　　select * from table_name where 字段A GLOB '*AAA*'
3、查找字段A第二位和第三位为 AA 的任意值
　　select *from table_name where 字段A GLOB '?AA*'
4、查找字段A以 A 开头，且长度至少为 3 个字符的任意值
　　select * from table_name where 字段A GLOB 'A?*?*'
5、查找字段A以 A 结尾的任意值
　　select *from table_name where 字段A GLOB '*A'
6、查找字段A第二位为 A，且以 B 结尾的任意值
　　select *from table_name where 字段A GLOB '?A*B'
7、查找字段A长度为 5 位数，且以 A 开头以 B 结尾的任意值(A,B中间三个下划线)
　　select *from table_name where 字段A GLOB 'A???B'
```


在room中，真接写 LIKE '% :key %'或者 "LIKE '%"+:key+“% '"都有问题，正确写法如下：
@Query("SELECT * FROM tb_use WHERE Name LIKE '%' || :name || '%')


```
//模糊查询：查找字段first_name包含指定内容的字段
@Query("SELECT * FROM user WHERE first_name LIKE '%'|| (:str)||'%'")
List<User> getLike(String str);
```
==注意：用双竖杠去拼接，而不是加号==

```
在SQL中的SELECT语句中，可使用一个特殊的操作符来拼接两个列。根据你所使用的DBMS，此操作符可用加号（+）或两个竖杠（||）表示。在MySQL和MariaDB中，必须使用特殊的函数。
说明：是+还是||？
Access和SQL Server使用+号。DB2、Oracle、PostgreSQL、SQLite和Open Office Base使用||。详细请参阅具体的DBMS文档。
看来SQL的知识点也要懂点啊！

```



