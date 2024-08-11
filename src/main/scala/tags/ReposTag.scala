package tags

import models.RepoFromAPI
import scalatags.Text.all.*

object ReposTag:
  def apply(repos: Seq[RepoFromAPI]): Tag =
    div(
      cls := "min-h-screen",
      div(
        cls := "navbar justify-center bg-base-100",
        h1(
          cls := "navbar-center text-4xl",
          "Repos"
        ),
      ),
      div(
        if repos.nonEmpty then
          cls := "p-4",
        div(
          cls := "grid grid-cols-3 gap-4 bg-stone-500 p-4 rounded-lg",
          repos.map(RepoTag(_)),
        )
      )
    )
