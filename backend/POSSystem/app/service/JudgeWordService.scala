package service

/**
 * Created by garyLu on 8/22/15.
 */
object JudgeWordService {


  def judgeWord(word:String):Map[String,Any]={
    var number=""
    println(word)
    if(word.indexOf("受傷")>=0
      ||word.indexOf("偷竊")>=0
      ||word.indexOf("倒")>=0) {
      number = "119"
    }else if(word.indexOf("搶劫")>=0
      ||word.indexOf("殺")>=0){
      number="110"
    }else if(word.indexOf("過馬路")>=0)
      number="113"
    else
      number="0"
    println(number)
    return Map("code"->number)
  }
}
