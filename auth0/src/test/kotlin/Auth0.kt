import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.ECDSAKeyProvider
import com.auth0.jwt.interfaces.RSAKeyProvider
import crypto.JwtUtils
import crypto.KeyUtils
import crypto.KeyUtils.parsePrivate
import crypto.KeyUtils.parsePublic
import crypto.KeyUtils.toBase64
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.security.KeyFactory
import java.security.KeyPair
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Auth0 {
    private fun sign(algorithm: Algorithm): String {
        val jwt = JWT.create()
            .withPayload(JwtUtils.MAP)
            .withAudience("A", "B")
            .sign(algorithm)

        JwtUtils.printJwt(jwt)
        return jwt
    }

    private fun parse(jwt: String, algorithm: Algorithm): DecodedJWT {
        return JWT.require(algorithm).build().verify(jwt)
    }

    fun test(): List<Algorithm> = listOf(
        Algorithm.RSA256(MyRSAKeyProvider(KeyUtils.rsaKeyPair(1024))),
        Algorithm.ECDSA256(MyECDSAKeyProvider(KeyUtils.ecKeyPair())),
    )

    @ParameterizedTest
    @MethodSource
    fun test(algorithm: Algorithm) {
        val jwt = sign(algorithm)
        val token = parse(jwt, algorithm)
        println()
        println(token.algorithm)
        println(token.claims)
        println(token.audience)
    }

    class MyRSAKeyProvider(
        private val publicKeyBase64: String,
        private val privateKeyBase64: String
    ) : RSAKeyProvider {
        constructor(keyPair: KeyPair) : this(keyPair.public.toBase64(), keyPair.private.toBase64())

        override fun getPublicKeyById(keyId: String?): RSAPublicKey {
            KeyUtils.printPublic(publicKeyBase64)
            return KeyFactory.getInstance("RSA").parsePublic(publicKeyBase64) as RSAPublicKey
        }

        override fun getPrivateKey(): RSAPrivateKey {
            KeyUtils.printPrivate(privateKeyBase64)
            return KeyFactory.getInstance("RSA").parsePrivate(privateKeyBase64) as RSAPrivateKey
        }

        override fun getPrivateKeyId(): String? {
            return null
        }
    }

    class MyECDSAKeyProvider(
        private val publicKeyBase64: String,
        private val privateKeyBase64: String
    ) : ECDSAKeyProvider {
        constructor(keyPair: KeyPair) : this(keyPair.public.toBase64(), keyPair.private.toBase64())

        override fun getPublicKeyById(keyId: String?): ECPublicKey {
            KeyUtils.printPublic(publicKeyBase64)
            return KeyFactory.getInstance("EC").parsePublic(publicKeyBase64) as ECPublicKey
        }

        override fun getPrivateKey(): ECPrivateKey {
            KeyUtils.printPrivate(privateKeyBase64)
            return KeyFactory.getInstance("EC").parsePrivate(privateKeyBase64) as ECPrivateKey
        }

        override fun getPrivateKeyId(): String? {
            return null
        }
    }
}
