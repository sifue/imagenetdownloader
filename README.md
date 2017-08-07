# ImageNetDownloader

Image File Downloader for [ImageNet](http://image-net.org/) with multi-thread and http connection pool.
Using Akka Actor and OkHttp.

# How to use

Checkout repository and install Java8.
Edit `application.conf` for your use.

```
app {
  wordsFilePath = "/Users/soichiro_yoshimura/Desktop/ImageUrls/words.txt"
  urlsFilePath = "/Users/soichiro_yoshimura/Desktop/ImageUrls/fall11_urls.txt"
  outputDirPath = "/Users/soichiro_yoshimura/Desktop/imagenet_download"
  numOfDownloader = 2000
}
```

And execute this command line.

```
$ java -jar imagenetdownloader-assembly-1.0.jar

```

# LICENSE
ISC LICENSE