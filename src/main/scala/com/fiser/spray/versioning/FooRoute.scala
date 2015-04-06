package com.fiser.spray.versioning

import akka.actor.{ActorPath, ActorLogging, Actor}
import akka.pattern.AskSupport
import akka.util.Timeout
import spray.httpx.marshalling.ToResponseMarshaller
import spray.routing.{Route, HttpService}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

trait FooRoute extends HttpService with AskSupport {

  implicit val timeout: Timeout
  implicit val fooActorPath: ActorPath
  implicit val ec: ExecutionContext

  /** This route supports foo v1 and foo v2 */
  val fooRoute =
    post {
      path("foo") {
        implicit ctx =>

          lazy val actorSelection = actorRefFactory.actorSelection(fooActorPath)

          def actComplete[T](implicit marshaller: ToResponseMarshaller[T]): Try[T] => Route = {
            case Success(v) => complete(v)
            case Failure(e) => complete(e)
          }

          /**
           * Here the version is determined as part of the Unmarshalling, as
           * per the Accept(MediaType)
           *
           * The actor that handles the request also matches on the different
           * version ( FooProtocol | FooProtocolV2 )
           *
           */

          // V1 of Hello
          (entity(as[FooProtocol.HelloFoo]) { cmd =>
            onComplete((actorSelection ? cmd).mapTo[FooProtocol.FooResponse]) {
              actComplete[FooProtocol.FooResponse]
            }
          } ~
            // V2 of Hello
            entity(as[FooProtocolV2.HelloFoo]) { cmd =>
              onComplete((actorSelection ? cmd).mapTo[FooProtocolV2.FooResponse]) {
                actComplete[FooProtocolV2.FooResponse]
              }
            })(ctx)
      }
    }
}

class FooActor extends Actor with ActorLogging {
  def receive: Receive = {
    // V1
    case msg@FooProtocol.HelloFoo(hi) =>
      sender ! FooProtocol.FooResponse(s"Hello back $hi")
    // V2
    case msg@FooProtocolV2.HelloFoo(hi, there) =>
      sender ! FooProtocolV2.FooResponse(s"Hello back $hi $there")
  }
}
