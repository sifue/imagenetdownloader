package jp.ed.nnn.imagenetdownloader

import akka.actor.{ActorSystem, Inbox, Props}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends App {
  val wordsFilePath = "/Users/soichiro_yoshimura/Desktop/ImageUrls/words.txt"
  val urlsFilePath = "/Users/soichiro_yoshimura/Desktop/ImageUrls/fall11_urls.txt"

  val system = ActorSystem("imagenetdownloader")
  val inbox = Inbox.create(system)
  implicit val sender = inbox.getRef()

  val supervisor = system.actorOf(Props(classOf[Supervisor], wordsFilePath, urlsFilePath))
  supervisor ! Start

  Await.ready(system.terminate(), Duration.Inf)
}
