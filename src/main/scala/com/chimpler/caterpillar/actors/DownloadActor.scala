package com.chimpler.caterpillar.actors

import akka.actor.{Props, Actor}
import akka.routing.{RoundRobinRoutingLogic, Router, ActorRefRoutee}
import com.chimpler.caterpillar.CrawlUrl
import com.chimpler.caterpillar.component.{DefaultDownloader}
import scala.concurrent.ExecutionContext.Implicits.global

class DownloadActor extends Actor {
  val downloader = new DefaultDownloader()

  lazy val extractorActor = context.actorOf(Props[ExtractActor])

  override def receive: Receive = {
    case crawlUrl:CrawlUrl =>
      downloader.download(crawlUrl) map{
        crawlData => extractorActor ! crawlData
      }

  }
}