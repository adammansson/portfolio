import pureconfig.*
import pureconfig.generic.semiauto.*

case class HttpConfig(host: String, port: String)

case class DBConfig(driver: String, url: String, user: String, password: String)

case class AppConfig(http: HttpConfig, db: DBConfig)

given ConfigReader[AppConfig] = deriveReader

object Config:
  def load(): AppConfig = ConfigSource.default.loadOrThrow[AppConfig]