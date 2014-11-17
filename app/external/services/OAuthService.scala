package external.services

import play.api.libs.json.JsValue
import play.api.libs.oauth.RequestToken
import scala.concurrent.Promise


trait OAuthService {

  def retrieveRequestToken(callbackUrl: String): Promise[(String, RequestToken)]

  def registeredUserProfile(token: RequestToken, authVerifier: String): Promise[JsValue]
}
