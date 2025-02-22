import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.ECDSASigner
import com.nimbusds.jose.crypto.ECDSAVerifier
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.AsymmetricJWK
import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.ECKeyGenerator
import com.nimbusds.jose.jwk.gen.JWKGenerator
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import crypto.JwtUtils
import crypto.KeyUtils
import crypto.KeyUtils.parsePublic
import crypto.KeyUtils.toBase64
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.argumentSet
import org.junit.jupiter.params.provider.MethodSource
import java.security.KeyFactory
import java.security.PublicKey
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPublicKey
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Nimbus {
    fun test(): List<Arguments> = listOf(
        argumentSet(
            "RSA",
            RSAKeyGenerator(2048),
            { key: RSAKey -> RSASSASigner(key) },
            JWSAlgorithm.RS256,
            "RSA",
            { key: RSAPublicKey -> RSASSAVerifier(key) },
        ),
        argumentSet(
            "EC",
            ECKeyGenerator(Curve.P_256),
            { key: ECKey -> ECDSASigner(key) },
            JWSAlgorithm.ES256,
            "EC",
            { key: ECPublicKey -> ECDSAVerifier(key) },
        ),
    )

    @ParameterizedTest
    @MethodSource
    fun <T : JWK> test(
        generator: JWKGenerator<T>,
        signerCreator: (T) -> JWSSigner,
        algorithm: JWSAlgorithm,
        keyFactoryAlgorithm: String,
        verifierCreator: (PublicKey) -> JWSVerifier,
    ) {
        val jwk = generator.generate()
        val signer = signerCreator(jwk)
        val jwt = sign(signer, algorithm)
        val publicKeyBase64 = (jwk as AsymmetricJWK).toPublicKey().toBase64()
        KeyUtils.printPublic(publicKeyBase64)

        val signedJWT = SignedJWT.parse(jwt)
        println(signedJWT.header)
        println(signedJWT.payload)

        val publicKey = KeyFactory.getInstance(keyFactoryAlgorithm).parsePublic(publicKeyBase64)
        val verifier = verifierCreator(publicKey)
        assertTrue(signedJWT.verify(verifier))
    }

    private fun sign(signer: JWSSigner, algorithm: JWSAlgorithm): String {
//        assertEquals(JWTClaimsSet.parse(JwtUtils.JSON), JWTClaimsSet.parse(JwtUtils.MAP))

        val claimsSet = JWTClaimsSet.Builder(JWTClaimsSet.parse(JwtUtils.JSON)).audience(listOf("A", "B")).build()

        val signedJWT = SignedJWT(
            JWSHeader.Builder(algorithm).build(),
            claimsSet
        )
        signedJWT.sign(signer)

        val jwt = signedJWT.serialize()
        JwtUtils.printJwt(jwt)
        println()

        return jwt
    }
}
