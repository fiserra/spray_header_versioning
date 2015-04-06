package com.fiser.spray.versioning

import spray.http.HttpEntity
import spray.httpx.marshalling.Marshaller
import spray.httpx.unmarshalling.Unmarshaller
import com.fiser.spray.versioning.VersionMediaTypes._

object FooProtocol {
  sealed trait FooActorMessage
  case class HelloFoo(hi: String) extends FooActorMessage
  case class FooResponse(helloBackTo: String) extends FooActorMessage

  import org.json4s.native.Serialization.{read, write => swrite}

  implicit val FooUnmarshaller: Unmarshaller[HelloFoo] =
    Unmarshaller[HelloFoo](`application/vnd.ww.v1.foo+json`) {
      case HttpEntity.NonEmpty(contentType, data) =>
        read[HelloFoo](data.asString)
    }

  implicit val FooMarshaller: Marshaller[HelloFoo] =
    Marshaller.of[HelloFoo](`application/vnd.ww.v1.foo+json`) { (value, contentType, ctx) =>
      ctx.marshalTo(HttpEntity(contentType, swrite(value)))
    }
  implicit val HelloBackUnmarshaller: Unmarshaller[FooResponse] =
    Unmarshaller[FooResponse](`application/vnd.ww.v1.foo+json`) {
      case HttpEntity.NonEmpty(contentType, data) =>
        read[FooResponse](data.asString)
    }

  implicit val HelloBackMarshaller: Marshaller[FooResponse] =
    Marshaller.of[FooResponse](`application/vnd.ww.v1.foo+json`) { (value, contentType, ctx) =>
      ctx.marshalTo(HttpEntity(contentType, swrite(value)))
    }
}