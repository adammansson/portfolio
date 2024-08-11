import cats.*
import cats.effect.*
import com.comcast.ip4s.*
import doobie.*
import doobie.util.log.*
import org.http4s.client.Client
import org.http4s.dsl.io.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.*
import org.http4s.implicits.*
import org.http4s.scalatags.*
import org.http4s.server.{Router, Server}
import org.http4s.{HttpApp, HttpRoutes, Request}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import services.RepoService
import tags.*

val config: AppConfig = Config.load()

implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

val clientResource: Resource[IO, Client[IO]] =
  EmberClientBuilder
    .default[IO]
    .build

val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
  driver = config.db.driver,
  url = config.db.url,
  user = config.db.user,
  password = config.db.password,
  logHandler = Some(le => logger.info(le.sql))
)

def httpApp(client: Client[IO], xa: Transactor[IO]): HttpApp[IO] = Router(
  "/" -> portfolioService(),
  "/repos" -> RepoService(client, xa)
).orNotFound

def portfolioService(): HttpRoutes[IO] = HttpRoutes.of[IO] {
  case GET -> Root =>
    Ok(BaseTag(HeroTag(), FooterTag()))
}

def serverResource(httpApp: HttpApp[IO]): Resource[IO, Server] =
  val host = Ipv4Address.fromString(config.http.host).getOrElse(ipv4"0.0.0.0")
  val port = Port.fromString(config.http.port).getOrElse(port"8080")
  
  EmberServerBuilder
    .default[IO]
    .withHost(host)
    .withPort(port)
    .withHttpApp(httpApp)
    .build

object Main extends IOApp.Simple:

  override def run: IO[Unit] =
    val resources: Resource[IO, Unit] = for {
      client <- clientResource
      - <- serverResource(httpApp(client, xa))
    } yield ()

    resources.use(_ => IO.never).as(ExitCode.Success)
