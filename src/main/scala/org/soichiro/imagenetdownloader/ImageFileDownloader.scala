package org.soichiro.imagenetdownloader

import java.io.{File, IOException}
import java.nio.file.{Files, Paths, StandardOpenOption}

import akka.actor.{Actor, ActorRef}
import okhttp3._

import scala.util.{Failure, Success, Try}

sealed trait ImageFileDownloaderMessage
case object DownloadImage extends ImageFileDownloaderMessage
case class ImageNetUrl(id: String, url: String, wnid: String) extends ImageFileDownloaderMessage

class DownloadFailException extends IOException

class ImageFileDownloader(outputDirPath: String,
                          client: OkHttpClient,
                          wnidWordMap: Map[String, String],
                          urlsFileLoader: ActorRef
                         ) extends Actor {

  val jpegMediaType = MediaType.parse("image/jpeg")
  var originalSender = Actor.noSender

  override def receive = {

    case DownloadImage => {
      if(sender() != self) originalSender = sender()
      urlsFileLoader ! LoadUrlsFile
    }

    case Finished => originalSender ! Finished

    case imageNetUrl: ImageNetUrl => {
      val request = new Request.Builder()
        .url(imageNetUrl.url)
        .build()

      client.newCall(request).enqueue(new Callback {
        override def onFailure(call: Call, e: IOException): Unit = {
          originalSender ! DownloadFailure(e, imageNetUrl)
          downloadNext()
        }

        override def onResponse(call: Call, response: Response): Unit = {
          if (response.isSuccessful
              && jpegMediaType == response.body().contentType()) {
            val dir = new File(new File(outputDirPath),
              imageNetUrl.wnid + "-" + wnidWordMap(imageNetUrl.wnid))
            dir.mkdir()

            val downloadFile = File.createTempFile(
              imageNetUrl.id + "_",
              ".jpg",
              dir)

            val tmpFilePath = Paths.get(downloadFile.getAbsolutePath)
            Try {
              Files.write(tmpFilePath, response.body().bytes(), StandardOpenOption.WRITE)
            } match {
              case Success(v) => {
                originalSender ! DownloadSuccess(downloadFile.getAbsolutePath, imageNetUrl)
              }
              case Failure(e) =>  {
                downloadFile.delete()
                originalSender ! DownloadFailure(e, imageNetUrl)
              }
            }

          } else {
            originalSender ! DownloadFailure(new DownloadFailException, imageNetUrl)
          }

          downloadNext()
          response.close()
        }
      })
    }
  }

  private[this] def downloadNext(): Unit = self ! DownloadImage

}
