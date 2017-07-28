package jp.ed.nnn.imagenetdownloader

import akka.actor.{Actor, Props}

import scala.io.Source

sealed trait SupervisorMessage
case object Start extends SupervisorMessage
case object Finished extends SupervisorMessage
case class ImageNetUrl(id: String, url: String, wnid: String) extends SupervisorMessage


class Supervisor(wordsFilePath: String, urlsFilePath: String) extends Actor {

    var wnidWordMap = Map[String, String]()

    override def receive = {

    case Start => {
      val wordsFileSource = Source.fromFile(wordsFilePath)
      wnidWordMap = wordsFileSource.getLines().map(s => {
        val strs = s.split("\t")
        (strs.head, strs.tail.mkString("\t"))
      }).toMap

      val urlsFileLoader = context.actorOf(Props[UrlsFileLoader])
      urlsFileLoader ! LoadUrlsFile(urlsFilePath)
    }
  }
}
