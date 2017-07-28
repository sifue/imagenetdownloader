package jp.ed.nnn.imagenetdownloader

case class Config (wordsFilePath: String,
                   urlsFilePath: String,
                   outputDirPath: String,
                   numOfDownloader: Int)