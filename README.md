# ImageNetDownloader

Image File Downloader for [ImageNet](http://image-net.org/) with multi-threaded and http connection pool.
Using Akka Actor and OkHttp.

# Get Urls and Words

[Download Image URLs](http://image-net.org/download-imageurls)

### Image Urls
- [http://www.image-net.org/imagenet_data/urls/imagenet_fall11_urls.tgz](http://www.image-net.org/imagenet_data/urls/imagenet_fall11_urls.tgz)
- [http://image-net.org/imagenet_data/urls/imagenet_winter11_urls.tgz](http://image-net.org/imagenet_data/urls/imagenet_winter11_urls.tgz)
- [http://image-net.org/imagenet_data/urls/imagenet_spring10_urls.tgz](http://image-net.org/imagenet_data/urls/imagenet_spring10_urls.tgz)
- [http://image-net.org/imagenet_data/urls/imagenet_fall09_urls.tgz](http://image-net.org/imagenet_data/urls/imagenet_fall09_urls.tgz)

### Words
- [http://www.image-net.org/archive/words.txt](http://www.image-net.org/archive/words.txt)

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