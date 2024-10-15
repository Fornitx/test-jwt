import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.crypto.ECDSASigner
import com.nimbusds.jose.crypto.ECDSAVerifier
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.gen.ECKeyGenerator
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import crypto.JwtUtils
import crypto.KeyUtils
import crypto.KeyUtils.parsePublic
import crypto.KeyUtils.toBase64
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.security.KeyFactory
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPublicKey
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Nimbus {
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

    @Test
    fun testRsa() {
        val jwk = RSAKeyGenerator(2048).generate()
        val signer = RSASSASigner(jwk)
        val jwt = sign(signer, JWSAlgorithm.RS256)
        val publicKeyBase64 = jwk.toPublicKey().toBase64()
        KeyUtils.printPublic(publicKeyBase64)

        val signedJWT = SignedJWT.parse(jwt)
        println(signedJWT.header)
        println(signedJWT.payload)

        val verifier = RSASSAVerifier(KeyFactory.getInstance("RSA").parsePublic(publicKeyBase64) as RSAPublicKey)
        assertTrue(signedJWT.verify(verifier))
    }

    @Test
    fun testEc() {
        val jwk = ECKeyGenerator(Curve.P_256).generate()
        val signer = ECDSASigner(jwk)
        val jwt = sign(signer, JWSAlgorithm.ES256)
        val publicKeyBase64 = jwk.toPublicKey().toBase64()
        KeyUtils.printPublic(publicKeyBase64)

        val signedJWT = SignedJWT.parse(jwt)
        println(signedJWT.header)
        println(signedJWT.payload)

        val verifier = ECDSAVerifier(KeyFactory.getInstance("EC").parsePublic(publicKeyBase64) as ECPublicKey)
        assertTrue(signedJWT.verify(verifier))
    }
}
