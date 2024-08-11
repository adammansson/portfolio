package tags

import models.RepoFromAPI
import scalatags.Text.all.*

object RepoTag:
  def apply(repo: RepoFromAPI): Tag =
    div(
      cls := "card card-compact bg-base-200 text-primary-content",
      div(
        cls := "card-body",
        h2(
          cls := "text-lg",
          repo.name,
        ),
        p(repo.pushed_at),
        div(
          cls := "card-actions justify-end",
          a(
            cls := "btn",
            href := repo.html_url,
            "Visit"
          )
        )
      )
    )