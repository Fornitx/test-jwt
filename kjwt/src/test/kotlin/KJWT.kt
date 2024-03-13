import io.github.nefilim.kjwt.JWT
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant

class KJWT {
    @Test
    fun createJWT() {
        val jwt = JWT.es256 {
            audience("MODERATION") // no multiple audience
            subject("1234567890")
            issuer("nefilim")
            claim("name", "John Doe")
            claim("admin", true)
            issuedAt(Instant.now())
            expiresAt(Instant.now().plus(Duration.ofDays(1)))
        }
    }
}
