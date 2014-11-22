package com.chimpler.caterpillar.util

import com.ning.http.client.{HttpResponseStatus, AsyncHandler, AsyncCompletionHandler, Response}
import com.ning.http.client.cookie.Cookie
import dispatch.{StatusCode, Req, Http, url => dispatchUrl}
import org.jboss.netty.handler.codec.http.{DefaultHttpResponse, HttpMethod}
import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object HttpClient {
  val http = Http.configure(
    _ setFollowRedirects true
      setRequestTimeoutInMs 10000
      setConnectionTimeoutInMs 10000
      setAllowPoolingConnection true
      setMaximumConnectionsTotal 100
      setCompressionEnabled true
      setMaxRequestRetry 3
      setMaximumConnectionsPerHost 20
  )

  def fetchUrl(url: String, httpMethod: HttpMethod = HttpMethod.GET,
               referer: Option[String] = None,
               cookies: Seq[Cookie] = Seq.empty): Future[HttpResponse] = {
    // TODO: take care of cookies
    val req = dispatchUrl(url).setMethod(httpMethod.getName)
    http(req > OkOrNotModifiedHandler) map {
      response =>
        val headers = response.getHeaders().iterator().asScala.map {
          entry => (entry.getKey, entry.getValue.asScala.toList)
        }.toMap
        HttpResponse(url, response.getStatusCode, response.getResponseBody, headers)
    }
  }

  // accept Ok and 304s (Not Modified)
  // the handler is not thread-safe, therefore a def not a val
  def OkOrNotModifiedHandler = new AsyncCompletionHandler[Response] with AsyncHandler[Response] {

    def onCompleted(response: Response) = response

    override def onStatusReceived(status: HttpResponseStatus) =
    // 2xx ok, 304, content not changed (etag), 301 permanent redirection
      if (status.getStatusCode / 100 == 2 || status.getStatusCode == 301 || status.getStatusCode == 304)
        super.onStatusReceived(status)
      else
        throw StatusCode(status.getStatusCode)
  }
}

case class HttpResponse(url: String, statusCode: Int, content: String, headers: Map[String, List[String]]= Map.empty)