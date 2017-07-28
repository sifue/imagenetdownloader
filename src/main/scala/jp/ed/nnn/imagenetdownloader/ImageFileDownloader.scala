package jp.ed.nnn.imagenetdownloader

import java.io.{File, IOException}
import java.nio.file.{Files, Paths, StandardOpenOption}

import akka.actor.Actor
import okhttp3._

case class DownloadImage(imageNetUrl: ImageNetUrl)
class DownloadFailException extends IOException

class ImageFileDownloader(outputDirPath: String,
                          client: OkHttpClient,
                          wnidWordMap: Map[String, String]) extends Actor {

  val jpegMediaType = MediaType.parse("image/jpeg")

  override def receive = {
    case DownloadImage(imageNetUrl) => {

      val originalSender = sender()
      val request = new Request.Builder()
        .url(imageNetUrl.url)
        .build()

      client.newCall(request).enqueue(new Callback {
        override def onFailure(call: Call, e: IOException): Unit = {
          originalSender ! DownloadFailure(e, imageNetUrl)
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
            Files.write(tmpFilePath, response.body().bytes(), StandardOpenOption.WRITE)
            originalSender ! DownloadSuccess(downloadFile.getAbsolutePath, imageNetUrl)

          } else {
            originalSender ! DownloadFailure(new DownloadFailException, imageNetUrl)
          }
        }
      })
    }
  }

}
