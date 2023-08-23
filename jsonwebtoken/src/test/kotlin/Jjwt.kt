import crypto.JwtUtils
import crypto.KeyUtils
import crypto.KeyUtils.parsePrivate
import crypto.KeyUtils.parsePublic
import crypto.KeyUtils.toBase64
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.security.Key
import java.security.KeyFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Jjwt {
    private fun sign(privateKeyBase64: String, algorithm: String): String {
        KeyUtils.printPrivate(privateKeyBase64)
        val privateKey = KeyFactory.getInstance(algorithm).parsePrivate(privateKeyBase64)

        val jwt = Jwts.builder()
            .setClaims(JwtUtils.MAP)
            .setAudience("A")
            .signWith(privateKey)
            .compact()
        JwtUtils.printJwt(jwt)
        return jwt
    }

    private fun parse(token: String, publicKeyBase64: String): Jws<Claims> {
        KeyUtils.printPublic(publicKeyBase64)
        return Jwts.parserBuilder().setSigningKeyResolver(object : SigningKeyResolver {
            override fun resolveSigningKey(header: JwsHeader<*>, claims: Claims): Key {
                val algorithm = SignatureAlgorithm.forName(header.algorithm)
                return keyFactory(algorithm).parsePublic(publicKeyBase64)
            }

            override fun resolveSigningKey(header: JwsHeader<*>, plaintext: String): Key {
                val algorithm = SignatureAlgorithm.forName(header.algorithm)
                return keyFactory(algorithm).parsePublic(publicKeyBase64)
            }

            private fun keyFactory(signatureAlgorithm: SignatureAlgorithm): KeyFactory = KeyFactory.getInstance(
                if (signatureAlgorithm.isRsa)
                    "RSA"
                else if (signatureAlgorithm.isEllipticCurve)
                    "EC"
                else throw UnsupportedOperationException()
            )
        }).build().parseClaimsJws(token)
    }

    @ParameterizedTest
    @EnumSource(SignatureAlgorithm::class, names = ["RS256", "ES256"])
    fun test(signatureAlgorithm: SignatureAlgorithm) {
        val keyPair = Keys.keyPairFor(signatureAlgorithm)
        val jwt = sign(keyPair.private.toBase64(), keyPair.private.algorithm)

        val jwtClaims = parse(jwt, keyPair.public.toBase64())
        println()
        println(jwtClaims.header)
        println(jwtClaims.body)
        println(jwtClaims.body.audience)
    }
}
