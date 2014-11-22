package com.chimpler.caterpillar.component

import com.chimpler.caterpillar.{CrawlData, CrawlUrl}
import com.chimpler.caterpillar.util.{HttpResponse, HttpClient}
import org.jboss.netty.handler.codec.http.HttpMethod
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class DefaultDownloader extends Downloader {
  override def download(crawlUrl: CrawlUrl): Future[CrawlData] = {
    HttpClient.fetchUrl(crawlUrl.url, HttpMethod.GET) map {
      case HttpResponse(url, statusCode, content, headers) => CrawlData(crawlUrl, statusCode, content, headers)
    }
  }
}