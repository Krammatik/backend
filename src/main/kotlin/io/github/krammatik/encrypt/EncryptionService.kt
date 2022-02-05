package io.github.krammatik.encrypt

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionService(private val secret: String) {

    private fun cipher(mode: Int, secretKey: String): Cipher {
        if (secretKey.length != 32) throw RuntimeException("SecretKey length is not 32 chars")
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val sk = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
        val iv = IvParameterSpec(secretKey.substring(0, 16).toByteArray(Charsets.UTF_8))
        c.init(mode, sk, iv)
        return c
    }

    fun encrypt(value: String): String {
        val encrypted = cipher(Cipher.ENCRYPT_MODE, secret).doFinal(value.toByteArray(Charsets.UTF_8))
        return String(Base64.getEncoder().encode(encrypted))
    }

    fun decrypt(value: String): String {
        val byteStr = Base64.getDecoder().decode(value.toByteArray(Charsets.UTF_8))
        return String(cipher(Cipher.DECRYPT_MODE, secret).doFinal(byteStr))
    }

}