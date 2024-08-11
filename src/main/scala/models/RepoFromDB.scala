package models

import java.time.LocalDateTime

case class RepoFromDB(
                       id: Int,
                       name: String,
                       html_url: String,
                       description: String,
                       created_at: String,
                       pushed_at: String,
                       timestamp: LocalDateTime
                     ):
  val converted: RepoFromAPI =
    RepoFromAPI(
      id,
      name,
      html_url,
      description,
      created_at,
      pushed_at
    )
