import crypto.JwtUtils
import crypto.KeyUtils
import crypto.KeyUtils.parsePrivate
import crypto.KeyUtils.parsePublic
import crypto.KeyUtils.toBase64
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.SignatureAlgorithm
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.security.KeyFactory
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Jjwt {
    private fun sign(privateKeyBase64: String, algorithm: String): String {
        KeyUtils.printPrivate(privateKeyBase64)
        val privateKey = KeyFactory.getInstance(algorithm).parsePrivate(privateKeyBase64)

        val jwt = Jwts.builder()
            .claims(JwtUtils.MAP)
            .audience()
            .add("A")
            .add("B")
            .and()
            .signWith(privateKey)
            .compact()
        JwtUtils.printJwt(jwt)
        return jwt
    }

    private fun parse(token: String, publicKeyBase64: String): Jws<Claims> {
        KeyUtils.printPublic(publicKeyBase64)
        return Jwts.parser().keyLocator { header ->
            val algorithm = header.algorithm
            val signatureAlgorithm = Jwts.SIG.get()[algorithm] as SignatureAlgorithm
            val keyFactoryAlgorithm = if (signatureAlgorithm.id.startsWith("RS")) {
                "RSA"
            } else if (signatureAlgorithm.id.startsWith("ES")) {
                "EC"
            } else {
                throw RuntimeException("Unsupported signature algorithm ${signatureAlgorithm.id}")
            }
            KeyFactory.getInstance(keyFactoryAlgorithm).parsePublic(publicKeyBase64)
        }.build().parseSignedClaims(token)
    }

    @ParameterizedTest
    @ValueSource(strings = ["RS256", "ES256"])
    fun test(algorithm: String) {
        val keyPair = (Jwts.SIG.get()[algorithm] as SignatureAlgorithm).keyPair().build()
        val jwt = sign(keyPair.private.toBase64(), keyPair.private.algorithm)

        val jwtClaims = parse(jwt, keyPair.public.toBase64())
        println()
        println(jwtClaims.header)
        println(jwtClaims.payload)
        println(jwtClaims.payload.audience)
        assertEquals(12345, jwtClaims.payload["uuid", Number::class.java])
    }
}
