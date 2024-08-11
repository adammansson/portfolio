package tags

import scalatags.Text.all.*

object HeroTag:
  def apply(): Tag =
    div(
      cls := "hero min-h-screen",
      style := "background-image: url(https://cdn.pixabay.com/photo/2017/03/21/18/46/raven-2162966_960_720.jpg);",
      div(cls := "hero-overlay bg-opacity-80"),
      div(
        cls := "hero-content text-neutral-content text-center",
        div(
          cls := "max-w-md",
          h1(
            cls := "text-5xl font-bold",
            "Adam Månsson",
          ),
          ul(
            cls := "mt-4 flex justify-between",
            li(
              a(
                cls := "btn btn-ghost",
                href := "https://github.com/adammansson",
                "Github"
              ),
            ),
            li(
              div(
                cls := "btn btn-ghost",
                attr("hx-get") := "/repos",
                attr("hx-push-url") := "true",
                attr("hx-target") := "#content",
                attr("hx-swap") := "innerHTML",
                "Repos"
              ),
            ),
            li(
              a(
                cls := "btn btn-ghost",
                href := "https://linkedin.com/in/adam-månsson-663b1912b",
                "LinkedIn"
              ),
            ),
          ),
        ),
      ),
    )
