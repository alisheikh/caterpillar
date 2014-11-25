package com.chimpler.caterpillar.actor

import akka.actor.{ActorLogging, Props, Actor}
import akka.routing.{RoundRobinRoutingLogic, Router, ActorRefRoutee}
import com.chimpler.caterpillar.CrawlUrl
import com.chimpler.caterpillar.component.{MongoRawDataStore, DefaultDownloader}
import scala.concurrent.ExecutionContext.Implicits.global

class DownloadActor extends Actor with ActorLogging {
  // TODO: inject it
  val downloader = new DefaultDownloader()
  val rawDataStore = new MongoRawDataStore()

  lazy val extractorActor = context.actorOf(Props[ExtractActor])

  override def receive: Receive = {
    case crawlUrl:CrawlUrl =>
      downloader.download(crawlUrl) map {
        crawlData =>
          rawDataStore.save(crawlData) map {
            _ =>
              extractorActor ! crawlData
          }
      }

  }
}