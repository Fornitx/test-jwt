import crypto.JwtUtils
import crypto.KeyUtils
import io.fusionauth.jwt.JWTUtils
import io.fusionauth.jwt.Signer
import io.fusionauth.jwt.Verifier
import io.fusionauth.jwt.domain.JWT
import io.fusionauth.jwt.ec.ECSigner
import io.fusionauth.jwt.ec.ECVerifier
import io.fusionauth.jwt.rsa.RSASigner
import io.fusionauth.jwt.rsa.RSAVerifier
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FusionAuth {
    @Test
    fun testRsa() {
        val keyPair = JWTUtils.generate2048_RSAKeyPair()
        KeyUtils.printPrivate(keyPair.privateKey)
        val signer = RSASigner.newSHA256Signer(keyPair.privateKey)
        val encodedJWT = sign(signer)
        KeyUtils.printPublic(keyPair.publicKey)
        println()

        val verifier = RSAVerifier.newVerifier(keyPair.publicKey)
        parse(encodedJWT, verifier)
    }

    @Test
    fun testEc() {
        val keyPair = JWTUtils.generate256_ECKeyPair()
        KeyUtils.printPrivate(keyPair.privateKey)
        val signer = ECSigner.newSHA256Signer(keyPair.privateKey)
        val encodedJWT = sign(signer)
        KeyUtils.printPublic(keyPair.publicKey)
        println()

        val verifier = ECVerifier.newVerifier(keyPair.publicKey)
        parse(encodedJWT, verifier)
    }

    private fun sign(signer: Signer): String {
        val jwt = JWT().setAudience(arrayOf("A", "B"))
        jwt.otherClaims.putAll(JwtUtils.MAP)

        val encodedJWT = JWT.getEncoder().encode(jwt, signer)
        JwtUtils.printJwt(encodedJWT)

        return encodedJWT
    }

    private fun parse(encodedJWT: String, verifier: Verifier) {
        val jwt = JWT.getDecoder().decode(encodedJWT, verifier)
        println(jwt.header)
        println(jwt)
        println(jwt.audience)
        assertEquals(12345, jwt.getInteger("uuid"))
    }
}
