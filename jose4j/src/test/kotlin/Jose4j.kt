import crypto.JwtUtils
import crypto.KeyUtils
import crypto.KeyUtils.parseKey
import crypto.KeyUtils.toBase64
import org.jose4j.jwk.EcJwkGenerator
import org.jose4j.jwk.PublicJsonWebKey
import org.jose4j.jwk.RsaJwkGenerator
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jws.RsaUsingShaAlgorithm
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.jose4j.keys.EllipticCurves
import org.junit.jupiter.api.Test
import java.security.KeyFactory
import java.time.Duration
import java.time.Instant

class Jose4j {
    private fun sign(jsonWebKey: PublicJsonWebKey, algorithm: String): Pair<String, String> {
        val claims = JwtClaims.parse(JwtUtils.JSON)
        claims.setAudience("A", "B")
        claims.issuedAt = NumericDate.fromMilliseconds(Instant.now().minus(Duration.ofDays(1)).toEpochMilli())
        claims.expirationTime = NumericDate.fromMilliseconds(Instant.now().plus(Duration.ofDays(1)).toEpochMilli())

        val jws = JsonWebSignature()
        jws.payload = claims.toJson()
        jws.key = jsonWebKey.privateKey
        jws.algorithmHeaderValue = algorithm

        val jwt = jws.compactSerialization
        JwtUtils.printJwt(jwt)

        val publicKey = jsonWebKey.publicKey.toBase64()
        KeyUtils.printKey(publicKey)

        return jwt to publicKey
    }

    private fun parse(jwt: String, publicKeyBase64: String): JwtClaims {
        val jwtConsumer = JwtConsumerBuilder()
            .setVerificationKeyResolver { jws, _ ->
                KeyFactory.getInstance(jws.algorithm.keyType).parseKey(publicKeyBase64)
            }
            .setSkipDefaultAudienceValidation()
            .build()

        return jwtConsumer.processToClaims(jwt)
    }

    @Test
    fun testRsa() {
        val jsonWebKey = RsaJwkGenerator.generateJwk(2048)

        RsaUsingShaAlgorithm.RsaSha256().keyType

        val (jwt, publicKeyBase64) = sign(jsonWebKey, AlgorithmIdentifiers.RSA_USING_SHA256)

        val jwtClaims = parse(jwt, publicKeyBase64)
        println(jwtClaims)
        println(jwtClaims.audience)
    }

    @Test
    fun testEc() {
        val jsonWebKey = EcJwkGenerator.generateJwk(EllipticCurves.P256)

        val (jwt, publicKeyBase64) = sign(jsonWebKey, AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256)

        val jwtClaims = parse(jwt, publicKeyBase64)
        println(jwtClaims)
        println(jwtClaims.audience)
    }
}
