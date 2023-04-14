import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import crypto.JwtUtils
import crypto.KeyUtils
import crypto.KeyUtils.parseKey
import crypto.KeyUtils.toBase64
import org.junit.jupiter.api.Test
import java.security.KeyFactory
import java.security.KeyPair
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Duration
import java.time.Instant

class Auth0 {
    private fun sign(keyPair: KeyPair, algorithm: Algorithm): Pair<String, String> {
        val jwt = JWT.create()
            .withPayload(JwtUtils.JSON)
            .withAudience("A", "B")
            .withIssuedAt(Instant.now().minus(Duration.ofDays(1)))
            .withExpiresAt(Instant.now().plus(Duration.ofDays(1)))
            .sign(algorithm)
        JwtUtils.printJwt(jwt)

        val publicKey = keyPair.public.toBase64()
        KeyUtils.printKey(publicKey)
        println()

        return jwt to publicKey
    }

    @Test
    fun testRsa() {
        val keyPair = KeyUtils.rsaKeyPair(1024)
        val algorithm = Algorithm.RSA256(keyPair.private as RSAPrivateKey)
        val (jwt, publicKeyBase64) = sign(keyPair, algorithm)

        val publicKey = KeyFactory.getInstance("RSA").parseKey(publicKeyBase64)
        val token = JWT.require(Algorithm.RSA256(publicKey as RSAPublicKey)).build().verify(jwt)
        println(token.algorithm)
        println(token.claims)
        println(token.audience)
    }

    @Test
    fun testEc() {
        val keyPair = KeyUtils.ecKeyPair()
        val algorithm = Algorithm.ECDSA256(keyPair.private as ECPrivateKey)
        val (jwt, publicKeyBase64) = sign(keyPair, algorithm)

        val publicKey = KeyFactory.getInstance("EC").parseKey(publicKeyBase64)
        val token = JWT.require(Algorithm.ECDSA256(publicKey as ECPublicKey)).build().verify(jwt)
        println(token.algorithm)
        println(token.claims)
        println(token.audience)
    }
}
