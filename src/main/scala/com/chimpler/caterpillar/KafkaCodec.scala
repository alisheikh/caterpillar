package com.chimpler.caterpillar

import kafka.serializer.{Encoder, Decoder}
import kafka.utils.VerifiableProperties
import com.novus.salat
import com.novus.salat.global._
import org.apache.logging.log4j.core.util.Charsets

// encode and decode logs in JSON (in this tuto for readability purpose) but it would be better to consider something like AVRO or protobuf)
class CrawlUrlDecoder(props: VerifiableProperties = null) extends Decoder[CrawlUrl] {
  def fromBytes(bytes: Array[Byte]): CrawlUrl = {
    salat.grater[CrawlUrl].fromJSON(new String(bytes, Charsets.UTF_8))
  }
}

class CrawlUrlEncoder(props: VerifiableProperties = null) extends Encoder[CrawlUrl] {
  def toBytes(crawlUrl: CrawlUrl): Array[Byte] = {
    salat.grater[CrawlUrl].toCompactJSON(crawlUrl).getBytes(Charsets.UTF_8)
  }
}