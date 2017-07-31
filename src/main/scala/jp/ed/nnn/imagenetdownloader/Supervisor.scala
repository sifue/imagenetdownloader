package jp.ed.nnn.imagenetdownloader

import java.io.IOException
import java.util.concurrent.TimeUnit

import akka.actor.{Actor, Props}
import akka.routing.{ActorRefRoutee, Broadcast, RoundRobinRoutingLogic, Router}
import okhttp3._

import scala.io.Source

sealed trait SupervisorMessage
case object Start extends SupervisorMessage
case object Finished extends SupervisorMessage
case class DownloadSuccess(tmpFilePath: String, imageNetUrl: ImageNetUrl) extends SupervisorMessage
case class DownloadFailure(e: Throwable, imageNetUrl: ImageNetUrl) extends SupervisorMessage

class Supervisor(config: Config) extends Actor {

  val client = new OkHttpClient.Builder()
    .connectTimeout(1, TimeUnit.SECONDS)
    .writeTimeout(1, TimeUnit.SECONDS)
    .readTimeout(1, TimeUnit.SECONDS)
    .build()

  var originalSender = Actor.noSender

  var successCount = 0
  var failureCount = 0

  override def receive = {

    case Start => {
      originalSender = sender()

      val wordsFileSource = Source.fromFile(config.wordsFilePath)
      val wnidWordMap = wordsFileSource.getLines().map(s => {
        val strs = s.split("\t")
        (strs.head, strs.tail.mkString("\t"))
      }).toMap

      val urlsFileLoader = context.actorOf(Props(new UrlsFileLoader(config.urlsFilePath)))
      val router = {
        val downloaders = Vector.fill(config.numOfDownloader) {
          ActorRefRoutee(context.actorOf(
            Props(new ImageFileDownloader(
              config.outputDirPath,
              client,
              wnidWordMap,
              urlsFileLoader
            ))))
        }
        Router(RoundRobinRoutingLogic(), downloaders)
      }

      router.route(Broadcast(DownloadImage), self)
    }

    case DownloadSuccess(path, imageNetUrl) => {
      successCount += 1
      printConsole()
    }

    case DownloadFailure(e, imageNetUrl) => {
      failureCount += 1
    }

    case Finished => originalSender ! Finished

  }

  private[this] def printConsole(): Unit = {
    val total = successCount + failureCount
    println(s"total: ${total}, successCount: ${successCount}, failureCount: ${failureCount}")
  }

}
