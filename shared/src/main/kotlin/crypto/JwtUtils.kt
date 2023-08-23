package crypto

object JwtUtils {
    const val JSON = """
        {
          "sub": "12345",
          "name": "John Gold",
          "admin": true
        }
    """

    val MAP = mapOf(
        "sub" to 12345,
        "name" to "John Gold",
        "admin" to true,
    )

    fun printJwt(jwt: String) {
        println("JWT %d:%n\t%s".format(jwt.length, jwt))
    }
}
