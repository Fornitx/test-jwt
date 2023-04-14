import crypto.JwtUtils
import crypto.KeyUtils
import crypto.KeyUtils.parseKey
import crypto.KeyUtils.toBase64
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Test
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair

class Jjwt {
    private fun sign(keyPair: KeyPair): Pair<String, String> {
        val jwt = Jwts.builder()
            .setClaims(JwtUtils.MAP)
            .setAudience("A")
            .signWith(keyPair.private)
            .compact()
        JwtUtils.printJwt(jwt)

        val publicKey = keyPair.public.toBase64()
        KeyUtils.printKey(publicKey)
        println()

        return jwt to publicKey
    }

    private fun parse(publicKeyBase64: String, jwt: String): Jws<Claims> {
        return Jwts.parserBuilder().setSigningKeyResolver(object : SigningKeyResolver {
            override fun resolveSigningKey(header: JwsHeader<*>, claims: Claims): Key {
                val algorithm = SignatureAlgorithm.forName(header.algorithm)
                return KeyFactory.getInstance(algorithm.familyName).parseKey(publicKeyBase64)
            }

            override fun resolveSigningKey(header: JwsHeader<*>, plaintext: String): Key {
                val algorithm = SignatureAlgorithm.forName(header.algorithm)
                return KeyFactory.getInstance(algorithm.familyName).parseKey(publicKeyBase64)
            }
        }).build().parseClaimsJws(jwt)
    }

    @Test
    fun testRsa() {
        val keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256)
        val (jwt, publicKeyBase64) = sign(keyPair)

        val jwtClaims = parse(publicKeyBase64, jwt)
        println(jwtClaims)
        println(jwtClaims.header)
        println(jwtClaims.body)
        println(jwtClaims.body.audience)
    }

    @Test
    fun testEc() {
        val keyPair = Keys.keyPairFor(SignatureAlgorithm.ES256)
        val (jwt, publicKeyBase64) = sign(keyPair)

        val jwtClaims = parse(publicKeyBase64, jwt)
        println(jwtClaims)
        println(jwtClaims.header)
        println(jwtClaims.body)
        println(jwtClaims.body.audience)
    }
}
