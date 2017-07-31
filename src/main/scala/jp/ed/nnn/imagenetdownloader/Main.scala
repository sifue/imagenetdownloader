package jp.ed.nnn.imagenetdownloader

import java.util.logging.{Level, Logger}

import akka.actor.{ActorSystem, Inbox, Props}
import okhttp3.OkHttpClient

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App {
  val wordsFilePath = "/Users/soichiro_yoshimura/Desktop/ImageUrls/words.txt"
  val urlsFilePath = "/Users/soichiro_yoshimura/Desktop/ImageUrls/fall11_urls.txt"
  val outputDirPath = "/Users/soichiro_yoshimura/Desktop/imagenet_download"
  val numOfDownloader = 200

  val system = ActorSystem("imagenetdownloader")
  val inbox = Inbox.create(system)
  implicit val sender = inbox.getRef()

  val supervisor = system.actorOf(Props(classOf[Supervisor],
    Config(
      wordsFilePath,
      urlsFilePath,
      outputDirPath,
      numOfDownloader
    )))
  supervisor ! Start

  inbox.receive(100.days)
  Await.ready(system.terminate(), Duration.Inf)
}
