package models

import io.circe.*

case class RepoFromAPI(
                        id: Int,
                        name: String,
                        html_url: String,
                        description: String,
                        created_at: String,
                        pushed_at: String,
                      )

implicit val repoDecoder: Decoder[RepoFromAPI] = (c: HCursor) => for {
  id <- c.downField("id").as[Int]
  name <- c.downField("name").as[String]
  html_url <- c.downField("html_url").as[String]
  description <- c.downField("description").as[String]
  created_at <- c.downField("created_at").as[String]
  pushed_at <- c.downField("pushed_at").as[String]
} yield RepoFromAPI(id, name, html_url, description, created_at, pushed_at)
