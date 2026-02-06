package me.bfapplicant.infra.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@ConfigurationProperties(prefix = "jwt")
class JwtProperties(
    val privateKey: Resource,
    val publicKey: Resource,
    val accessExpirationMs: Long = 1_800_000,
    val refreshExpirationMs: Long = 604_800_000
) {
    val rsaPrivateKey: RSAPrivateKey by lazy { loadPrivateKey() }
    val rsaPublicKey: RSAPublicKey by lazy { loadPublicKey() }

    private fun loadPrivateKey(): RSAPrivateKey {
        val pem = privateKey.inputStream.bufferedReader().readText()
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\\s".toRegex(), "")
        val decoded = Base64.getDecoder().decode(pem)
        return KeyFactory.getInstance("RSA")
            .generatePrivate(PKCS8EncodedKeySpec(decoded)) as RSAPrivateKey
    }

    private fun loadPublicKey(): RSAPublicKey {
        val pem = publicKey.inputStream.bufferedReader().readText()
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s".toRegex(), "")
        val decoded = Base64.getDecoder().decode(pem)
        return KeyFactory.getInstance("RSA")
            .generatePublic(X509EncodedKeySpec(decoded)) as RSAPublicKey
    }
}
