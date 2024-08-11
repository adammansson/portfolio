package tags

import scalatags.Text.all.*

object FooterTag:
  def apply(): Tag =
    footer(
      cls := "footer footer-center bg-base-300 text-base-content p-4",
      tag("aside")(
        p("Copyright 2024 - All right reserved by Adam Månsson")
      )
    )
