package org.soichiro.imagenetdownloader

case class Config (wordsFilePath: String,
                   urlsFilePath: String,
                   outputDirPath: String,
                   numOfDownloader: Int)