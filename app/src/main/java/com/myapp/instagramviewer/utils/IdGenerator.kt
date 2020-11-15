/**
 * Created by Tsymbalyuk Konstantin from  on 14.11.2020.
 */
package com.myapp.instagramviewer.utils

import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


class IdGenerator(private val key: String) {

    fun generateIdFromStringValue(): String {
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val spec: KeySpec = PBEKeySpec(key.toCharArray(), "salt".toByteArray(), 65536, 256)
        val tmp: SecretKey = factory.generateSecret(spec)
        val secret: SecretKey = SecretKeySpec(tmp.getEncoded(), "AES")
        return secret.toString()
    }

}