package com.baizhi.spark

import com.baizhi.model.HistoryData
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.SerializationUtils
import redis.clients.jedis.Jedis

object RedisSink extends Serializable {
   lazy val jedis:Jedis=createJedis()
   Runtime.getRuntime.addShutdownHook(new Thread(){
     override def run(): Unit = {
       //关闭资源
       jedis.close()
     }
   })
   private def createJedis():Jedis={
     new Jedis("CentOSA",6379)
   }
   def writeToRedis(key:String,value:Object):Unit={
     //val valueBytes = SerializationUtils.serialize(value.asInstanceOf[java.io.Serializable])
     val objectMapper=new ObjectMapper()

     jedis.set(key,objectMapper.writeValueAsString(value))
   }
}
