package tags

import scalatags.Text.all.*

object BaseTag:
  def apply(content: Tag, footer: Tag): Tag =
    html(
      head(
        script(
          src := "https://unpkg.com/htmx.org@2.0.1",
          integrity := "sha384-QWGpdj554B4ETpJJC9z+ZHJcA/i59TyjxEPXiiUgN2WmTyV5OEZWCD6gQhgkdpB/",
          attr("crossorigin") := "anonymous",
        ),
        script(src := "https://cdn.tailwindcss.com"),
        link(
          href := "https://cdn.jsdelivr.net/npm/daisyui@2.6.0/dist/full.css",
          rel := "stylesheet",
          attr("type") := "text/css"
        ),
        tag("title")("Adam Månsson"),
      ),
      body(
        tag("main")(
          id := "content",
          content
        ),
        div(
          id := "footer",
          footer
        )
      )
    )
