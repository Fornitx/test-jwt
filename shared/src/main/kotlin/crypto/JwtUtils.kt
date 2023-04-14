package crypto

object JwtUtils {
    const val JSON = """
        {
          "sub": "12345",
          "name": "John Gold",
          "admin": true
        }
    """

    fun printJwt(jwt: String) {
        println("JWT: ${jwt.length}")
        println(jwt)
    }
}