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
import java.time.ZonedDateTime

class FusionAuth {
    private fun sign(signer: Signer): String {
        val jwt = JWT()
            .setIssuedAt(ZonedDateTime.now().minusDays(1))
            .setExpiration(ZonedDateTime.now().plusDays(1))
            .setAudience(arrayOf("A", "B"))

        val encodedJWT = JWT.getEncoder().encode(jwt, signer)
        JwtUtils.printJwt(encodedJWT)

        return encodedJWT
    }

    private fun parse(encodedJWT: String, verifier: Verifier) {
        val jwt = JWT.getDecoder().decode(encodedJWT, verifier)
        println(jwt.header)
        println(jwt)
        println(jwt.audience)
    }

    @Test
    fun testRsa() {
        val keyPair = JWTUtils.generate2048_RSAKeyPair()
        val signer = RSASigner.newSHA256Signer(keyPair.privateKey)
        val encodedJWT = sign(signer)
        KeyUtils.printKey(keyPair.publicKey)
        println()

        val verifier = RSAVerifier.newVerifier(keyPair.publicKey)
        parse(encodedJWT, verifier)
    }

    @Test
    fun testEc() {
        val keyPair = JWTUtils.generate256_ECKeyPair()
        val signer = ECSigner.newSHA256Signer(keyPair.privateKey)
        val encodedJWT = sign(signer)
        KeyUtils.printKey(keyPair.publicKey)
        println()

        val verifier = ECVerifier.newVerifier(keyPair.publicKey)
        parse(encodedJWT, verifier)
    }
}