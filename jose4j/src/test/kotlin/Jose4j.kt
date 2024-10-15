import crypto.JwtUtils
import crypto.KeyUtils
import crypto.KeyUtils.parsePrivate
import crypto.KeyUtils.parsePublic
import crypto.KeyUtils.toBase64
import org.jose4j.jwk.EcJwkGenerator
import org.jose4j.jwk.PublicJsonWebKey
import org.jose4j.jwk.RsaJwkGenerator
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.jose4j.keys.EllipticCurves
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.security.KeyFactory
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Jose4j {
    private fun sign(privateKeyBase64: String, keyStoreAlgorithm: String, jwtAlgorithm: String): String {
        KeyUtils.printPrivate(privateKeyBase64)

        val claims = JwtClaims.parse(JwtUtils.JSON)
        claims.setAudience("A", "B")

        val jws = JsonWebSignature()
        jws.payload = claims.toJson()
        jws.key = KeyFactory.getInstance(keyStoreAlgorithm).parsePrivate(privateKeyBase64)
        jws.algorithmHeaderValue = jwtAlgorithm

        val jwt = jws.compactSerialization
        JwtUtils.printJwt(jwt)

        return jwt
    }

    private fun parse(jwt: String, publicKeyBase64: String): JwtClaims {
        KeyUtils.printPublic(publicKeyBase64)
        val jwtConsumer = JwtConsumerBuilder()
            .setVerificationKeyResolver { jws, _ ->
                KeyFactory.getInstance(jws.algorithm.keyType).parsePublic(publicKeyBase64)
            }
            .setSkipDefaultAudienceValidation()
            .build()

        return jwtConsumer.processToClaims(jwt)
    }

    fun test(): List<Arguments> {
        return listOf(
            arguments(
                RsaJwkGenerator.generateJwk(2048),
                "RSA",
                AlgorithmIdentifiers.RSA_USING_SHA256,
            ),
            arguments(
                EcJwkGenerator.generateJwk(EllipticCurves.P256),
                "EC",
                AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256,
            ),
        )
    }

    @ParameterizedTest
    @MethodSource
    fun test(jsonWebKey: PublicJsonWebKey, keyStoreAlgorithm: String, jwtAlgorithm: String) {
        val jwt = sign(jsonWebKey.privateKey.toBase64(), keyStoreAlgorithm, jwtAlgorithm)

        val jwtClaims = parse(jwt, jsonWebKey.publicKey.toBase64())
        println(jwtClaims)
        println(jwtClaims.audience)
        assertEquals(12345, jwtClaims.getClaimValue("uuid", Number::class.java).toInt())
    }
}
