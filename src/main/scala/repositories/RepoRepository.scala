package repositories

import doobie.*
import doobie.implicits.*
import doobie.postgres.implicits.*
import models.{RepoFromAPI, RepoFromDB}

object RepoRepository:
  def insertMany(repos: Seq[RepoFromAPI]): ConnectionIO[Int] =
    Update[RepoFromAPI]("INSERT INTO repos (id, name, html_url, description, created_at, pushed_at) VALUES (?, ?, ?, ?, ?, ?)")
      .updateMany(repos)

  def selectAll: ConnectionIO[List[RepoFromDB]] =
    sql"SELECT id, name, html_url, description, created_at, pushed_at, timestamp FROM repos"
      .query[RepoFromDB]
      .to[List]

  def truncate(): ConnectionIO[Int] =
    sql"TRUNCATE TABLE repos"
      .update
      .run
