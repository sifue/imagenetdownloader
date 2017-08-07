package org.soichiro.imagenetdownloader

import java.io.File

import akka.actor.{ActorSystem, Inbox, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App {

  val configFilePath = if(args.length > 0) args(0) else "application.conf"
  private[this] val conf = ConfigFactory.parseFile(new File(configFilePath)).getConfig("app")
  val wordsFilePath = conf.getString("wordsFilePath")
  val urlsFilePath = conf.getString("urlsFilePath")
  val outputDirPath = conf.getString("outputDirPath")
  val numOfDownloader = conf.getInt("numOfDownloader")
  val config = Config(
    wordsFilePath,
    urlsFilePath,
    outputDirPath,
    numOfDownloader)

  val system = ActorSystem("imagenetdownloader")
  val inbox = Inbox.create(system)
  implicit val sender = inbox.getRef()

  val supervisor = system.actorOf(Props(new Supervisor(config)))
  supervisor ! Start

  inbox.receive(100.days)
  Await.ready(system.terminate(), Duration.Inf)
  println("Finished.")
}
