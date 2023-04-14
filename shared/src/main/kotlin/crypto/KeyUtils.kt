package crypto

import java.security.*
import java.security.spec.ECGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

object KeyUtils {
    fun rsaKeyPair(keysize: Int): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(keysize)
        return keyPairGenerator.generateKeyPair()
    }

    fun ecKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("EC")
        keyPairGenerator.initialize(ECGenParameterSpec("secp256r1"))
        return keyPairGenerator.generateKeyPair()
    }

    fun Key.toBase64(): String {
        return Base64.getEncoder().encodeToString(this.encoded)
    }

    fun KeyFactory.parseKey(key: String): PublicKey {
        val keyBytes = Base64.getDecoder().decode(key)
        val keySpec = X509EncodedKeySpec(keyBytes)
        return this.generatePublic(keySpec)
    }

    fun printKey(key: String) {
        println("PublicKey: ${key.length}")
        println(key)
    }
}
