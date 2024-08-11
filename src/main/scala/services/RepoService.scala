package services

import cats.effect.*
import cats.implicits.*
import doobie.*
import doobie.implicits.*
import io.circe.*
import io.circe.generic.auto.*
import models.{RepoFromAPI, RepoFromDB}
import org.http4s.*
import org.http4s.circe.*
import org.http4s.circe.CirceEntityDecoder.*
import org.http4s.client.Client
import org.http4s.dsl.io.*
import org.http4s.headers.*
import org.http4s.implicits.*
import org.http4s.scalatags.*
import org.typelevel.log4cats.Logger
import repositories.RepoRepository
import tags.{BaseTag, FooterTag, ReposTag}

import java.time.LocalDateTime

object RepoService:
  private val reposRequest: Request[IO] = Request[IO](
    method = Method.GET,
    headers = Headers(
      Accept(MediaType("application", "vnd.github+json")),
      ("X-GitHub-Api-Version", "2022-11-28"),
    ),
    uri = uri"https://api.github.com/users/adammansson/repos",
  )

  def apply(client: Client[IO], xa: Transactor[IO])(implicit logger: Logger[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case request@GET -> Root =>
      for {
        reposFromDB <- RepoRepository.selectAll.transact(xa).attempt.flatMap {
          case Right(repos) =>
            logger.info("Successfully got repos from DB") *>
              IO.pure(repos)
          case Left(error) =>
            logger.error(s"Failed to get repos from DB: ${error.getMessage}") *>
              IO.pure(Seq.empty[RepoFromDB])
        }

        reposResponse <-
          if shouldUpdateDB(reposFromDB) then
            for {
              reposFromAPI <- getFromAPI(client)
              _ <- updateDB(reposFromAPI).transact(xa)
              _ <- logger.info("Successfully updated DB")
            } yield reposFromAPI
          else
            logger.info("Responding with repos from DB") *>
              IO.pure(reposFromDB.map(_.converted))

        reposTag <- IO.pure(ReposTag(reposResponse))

        response <-
          if isHTMX(request.headers) then
            Ok(reposTag)
          else
            Ok(BaseTag(reposTag, FooterTag()))

      } yield response
  }

  private def shouldUpdateDB(repos: Seq[RepoFromDB], minutesToLive: Int = 15)(implicit logger: Logger[IO]): Boolean =
    repos.isEmpty || repos.exists(_.timestamp.isBefore(LocalDateTime.now().minusMinutes(minutesToLive)))

  private def getFromAPI(client: Client[IO])(implicit logger: Logger[IO]): IO[Seq[RepoFromAPI]] =
    client.expect[Seq[RepoFromAPI]](reposRequest).attempt.flatMap {
      case Right(repos) =>
        logger.info("Successfully got repos from API") *>
          IO.pure(repos)
      case Left(error) =>
        logger.error(s"Failed to get repos from API: ${error.getMessage}") *>
          IO.pure(Seq.empty[RepoFromAPI])
    }

  private def updateDB(repos: Seq[RepoFromAPI]): ConnectionIO[Seq[RepoFromAPI]] =
    for {
      _ <- RepoRepository.truncate()
      _ <- RepoRepository.insertMany(repos)
    } yield repos

  private def isHTMX(headers: Headers): Boolean =
    headers.get("HX-Request".ci).isDefined
