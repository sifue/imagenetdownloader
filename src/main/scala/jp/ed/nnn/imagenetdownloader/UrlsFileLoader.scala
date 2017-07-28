package jp.ed.nnn.imagenetdownloader

import akka.actor.Actor

import scala.io.Source

sealed trait UrlsFileLoaderMessage
case class LoadUrlsFile(urlsFilePath: String) extends UrlsFileLoaderMessage

class UrlsFileLoader extends Actor {

  override def receive = {
    case LoadUrlsFile(urlsFilePath) =>
      val urlsFileSource = Source.fromFile(urlsFilePath)
      urlsFileSource.getLines().foreach(line => {
        val strs = line.split("\t")
        val id = strs.head
        val url = strs.tail.mkString("\t")
        val wnid = id.split("_").head



      })
  }
}
