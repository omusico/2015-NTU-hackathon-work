package Utils

import model.Token

import scala.collection.mutable
import scala.collection.mutable._

/**
 * Created by KungChienHao on 2015/8/22.
 */
object TokenUtil {
  var tokenMap= new HashMap[String,Token]
  val pushSever=new GcmRestServer("AIzaSyAHCKhJuHKTDDwpCT2UxCNu7tZwe6QxvMI")
  def persistToken(token:Token):Map[String,Token]=tokenMap.+=((token.id,token))
  def removeToken(tokenID:String): Unit =tokenMap - tokenID
  def getToken(tokenName:String):Token=tokenMap(tokenName)
  def sendToken(code:String)={
    val ids=tokenMap.map{
      case (k,v)=> k
    }.toList

    pushSever.send(ids, scala.collection.immutable.Map(
      "type" -> code,
      "major" -> "1",
      "mirror"->"1"
    ))
  }



}
