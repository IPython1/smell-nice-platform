package com.sky.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author:杰杰睡不醒
 * @Date:2024/7/28 12:13
 * @Description:
 **/
//@SpringBootTest
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate() {
        System.out.println(redisTemplate);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        HashOperations hashOperations = redisTemplate.opsForHash();
        ListOperations listOperations = redisTemplate.opsForList();
        SetOperations setOperations = redisTemplate.opsForSet();
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

    }
    //操作字符串类型的数据
    @Test
    public void testString(){
        //set get setex setnx
        redisTemplate.opsForValue().set("city","北京");
        String city = (String)redisTemplate.opsForValue().get("city");
        System.out.println(city);
        redisTemplate.opsForValue().set("code","1234",3, TimeUnit.MINUTES);//第3个参数是时间，第4个参数是时间单位
        redisTemplate.opsForValue().setIfAbsent("lock","1");
        redisTemplate.opsForValue().setIfAbsent("lock","2");

    }
    //操作哈希类型的数据
    @Test
    public void testHash(){
        //hset hget hkeys hvals
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("100","name","tom"); //相当于hset
        hashOperations.put("100","age","20");
        String name = (String)hashOperations.get("100", "name"); //相当于hget
        System.out.println(name);
        Set keys = hashOperations.keys("100"); //相当于hkeys
        System.out.println(keys);
        List values = hashOperations.values("100"); //相当于hvals
        System.out.println(values);
        hashOperations.delete("100","age");//相当于hdel

    }
    //列表
    @Test
    public void testList(){
        //Lpush lrange rpop llen
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPushAll("mylist","a","b","c"); //lpush多个value
        listOperations.leftPush("mylist","d"); //lpush单个value
        List mylist = listOperations.range("mylist",0,-1); //lrange
        System.out.println(mylist);
        listOperations.rightPop("mylist"); //rpop
        Long size = listOperations.size("mylist"); //llen
        System.out.println(size);
    }
    //集合
    @Test
    public void testSet(){
        //sadd smembers scard sinter sunion srem
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add("set1","a","b","c","d"); //sadd
        setOperations.add("set2","a","b","x","y");
        Set members = setOperations.members("set1"); //smembers
        System.out.println(members);
        Long size = setOperations.size("set1"); //scard
        System.out.println(size);
        Set intersect = setOperations.intersect("set1","set2"); //sinter取交集
        System.out.println(intersect);
        Set union = setOperations.union("set1","set2"); //sunion取并集
        System.out.println(union);
        setOperations.remove("set1","a","b"); //srem
    }
    //有序集合
    @Test
    public void testZset(){
        //zadd zrange zincrby zrem
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("zset1","a",10); //zadd
        zSetOperations.add("zset2","b",12);
        zSetOperations.add("zset1","c",9);
        Set zset1 = zSetOperations.range("zset1",0,-1); //zrange
        System.out.println(zset1);
        zSetOperations.incrementScore("zset1","c",10); //zincrby
        zSetOperations.remove("zset1","a","b"); //zrem
    }
    //通用命令操作
    @Test
    public void testCommon(){
        //keys exists type del
        Set keys = redisTemplate.keys("*"); //keys
        System.out.println(keys);
        Boolean name = redisTemplate.hasKey("name"); //exists
        Boolean set1 = redisTemplate.hasKey("set1");
        for(Object key : keys){
            DataType type = redisTemplate.type(key); //type
            System.out.println(type.name());
        }
        redisTemplate.delete("mylist"); //del
    }

}
