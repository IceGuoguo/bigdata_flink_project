package com.baizhi.spark

import com.baizhi.model.HistoryData
import com.fasterxml.jackson.databind.ObjectMapper
import redis.clients.jedis.Jedis

object RedisReader extends Serializable {
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
   def readFromRedis(key:String):HistoryData={
     val objectMapper=new ObjectMapper()
     val jsonValue = jedis.get(key)
     objectMapper.readValue(jsonValue,classOf[HistoryData])
   }
}
