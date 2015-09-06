package controllers

import Utils.TokenUtil
import _root_.Utils.TokenUtil
import _root_.model.Token
import model.Token
import play.api.mvc._
import play.api.libs.json._
import service.JudgeWordService

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def help(major:String,mirror:String,helpString:String)=Action{ implicit  request=>
    println(s"""${helpString} ${major} ${mirror}""")
    val map=JudgeWordService.judgeWord(helpString)
    val code=map.get("code")
   /* if(TokenUtil.getToken("eI-w_tOuv-g:APA91bF2918tYhJNz9mQG3IPupK6HPBzp8owzyCo6OcFjRFp7Ayb_ApealXNmpjtn45aBt3GlGA5HwRuqZWNwo5cXdV9HZmzTYf2RdIIKyxVXHcna33eADaiKHninTQE9yESfnHW7zK9")!=null) {
      println("remove:eI-w_tOuv-g")
      TokenUtil.removeToken("eI-w_tOuv-g:APA91bF2918tYhJNz9mQG3IPupK6HPBzp8owzyCo6OcFjRFp7Ayb_ApealXNmpjtn45aBt3GlGA5HwRuqZWNwo5cXdV9HZmzTYf2RdIIKyxVXHcna33eADaiKHninTQE9yESfnHW7zK9")

    }else{
      TokenUtil.removeToken("frPqRmWpvco:APA91bFKM3qR8qvF2pPyG8YqhKIkKBD6qpF1nuiwmuwVVVwq_LTMZUosoM4loge9XUSGti390wZTiF66cCm3Qvz43hx6TNVkkwkYra7HtR-Wi4M8L6GN1t3OKsg2fHCFUOMful7uzzRL")
      println("remove:frPqRmWpvco")
    }*/

    TokenUtil.sendToken(map.get("code").toString())
    val jsonString=
      s"""
        |{
        |   "result":"OK","type": "${code}"
        |}
      """.stripMargin
      println(jsonString)
    Ok(Json.parse(jsonString))
  }
  def now(major:String,mirror:String,tokenID:String)=Action{ implicit  request=>
    val jsonString=
      """
        |{
        |   "result":"OK"
        |}
      """.stripMargin
    Ok(Json.parse(jsonString))
  }

  def register(tokenName:String)=Action{ implicit  request=>
    val token=new Token(tokenName,tokenName,"-1","-1")
    TokenUtil.persistToken(token)
    println("tokenName:"+tokenName)
    println("tokenId:"+token.id)
    val jsonString=
      s"""
         |{
         |   "result":"OK","token_id":"${token.id}"
                                                    |}
      """.stripMargin
    Ok(Json.parse(jsonString))
  }

  def notification()=Action{ implicit request=>
    Ok("test")

  }
}