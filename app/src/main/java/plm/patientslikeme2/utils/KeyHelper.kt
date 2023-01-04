package plm.patientslikeme2.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.*
import java.security.cert.CertificateException
import java.util.*
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class KeyHelper(ctx: Context) {
    private var keyStore: KeyStore? = null

    @Throws(
        NoSuchProviderException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        KeyStoreException::class,
        CertificateException::class,
        IOException::class
    )
    private fun generateEncryptKey(ctx: Context) {
        keyStore = KeyStore.getInstance(AndroidKeyStore)
        keyStore?.load(null)
        if (!keyStore!!.containsAlias(KEY_ALIAS)) {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(false)
                    .build()
            )
            keyGenerator.generateKey()
        }
    }

    @Throws(Exception::class)
    private fun rsaEncrypt(secret: ByteArray): ByteArray {
        val privateKeyEntry = keyStore?.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry
        // Encrypt the text
        val inputCipher = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL")
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.certificate.publicKey)
        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
        cipherOutputStream.write(secret)
        cipherOutputStream.close()
        return outputStream.toByteArray()
    }

    @Throws(Exception::class)
    private fun rsaDecrypt(encrypted: ByteArray): ByteArray {
        val privateKeyEntry = keyStore?.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry
        val output = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL")
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)
        val cipherInputStream = CipherInputStream(
            ByteArrayInputStream(encrypted), output
        )
        val values = ArrayList<Byte>()
        var nextByte: Int
        while (cipherInputStream.read().also { nextByte = it } != -1) {
            values.add(nextByte.toByte())
        }
        val bytes = ByteArray(values.size)
        for (i in bytes.indices) {
            bytes[i] = values[i]
        }
        return bytes
    }

    @Throws(Exception::class)
    private fun generateAESKey(context: Context) {
        val pref = context.getSharedPreferences(SHARED_PREFENCE_NAME, Context.MODE_PRIVATE)
        var enryptedKeyB64 = pref.getString(ENCRYPTED_KEY, null)
        if (enryptedKeyB64 == null) {
            val key = ByteArray(16)
            val secureRandom = SecureRandom()
            secureRandom.nextBytes(key)
            val encryptedKey = rsaEncrypt(key)
            enryptedKeyB64 = Base64.encodeToString(encryptedKey, Base64.DEFAULT)
            val edit = pref.edit()
            edit.putString(ENCRYPTED_KEY, enryptedKeyB64)
            edit.apply()
        }
    }

    @get:Throws(
        NoSuchProviderException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        KeyStoreException::class,
        CertificateException::class,
        IOException::class,
        UnrecoverableKeyException::class
    )
    private val aESKeyFromKS: Key
        get() {
            keyStore = KeyStore.getInstance(AndroidKeyStore)
            keyStore?.load(null)
            return keyStore?.getKey(KEY_ALIAS, null) as SecretKey
        }

    @Throws(Exception::class)
    private fun getSecretKey(context: Context): Key {
        val pref = context.getSharedPreferences(SHARED_PREFENCE_NAME, Context.MODE_PRIVATE)
        val enryptedKeyB64 = pref.getString(ENCRYPTED_KEY, null)
        val encryptedKey = Base64.decode(enryptedKeyB64, Base64.DEFAULT)
        val key = rsaDecrypt(encryptedKey)
        return SecretKeySpec(key, "AES")
    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        NoSuchProviderException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        UnsupportedEncodingException::class
    )
    fun encrypt(context: Context, input: String): String {
        val pref = context.getSharedPreferences(SHARED_PREFENCE_NAME, Context.MODE_PRIVATE)
        val publicIV = pref.getString(PUBLIC_IV, null)
        val c: Cipher = Cipher.getInstance(AES_MODE_M)
        try {
            c.init(
                Cipher.ENCRYPT_MODE,
                aESKeyFromKS,
                GCMParameterSpec(128, Base64.decode(publicIV, Base64.DEFAULT))
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val encodedBytes = c.doFinal(input.toByteArray(charset("UTF-8")))
        return Base64.encodeToString(encodedBytes, Base64.DEFAULT)
    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        NoSuchProviderException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        UnsupportedEncodingException::class
    )
    fun decrypt(context: Context, encrypted: String): String {
        val c: Cipher = Cipher.getInstance(AES_MODE_M)
        val pref = context.getSharedPreferences(SHARED_PREFENCE_NAME, Context.MODE_PRIVATE)
        val publicIV = pref.getString(PUBLIC_IV, null)
        try {
            c.init(
                Cipher.DECRYPT_MODE,
                aESKeyFromKS,
                GCMParameterSpec(128, Base64.decode(publicIV, Base64.DEFAULT))
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val decodedValue = Base64.decode(encrypted.toByteArray(charset("UTF-8")), Base64.DEFAULT)
        val decryptedVal = c.doFinal(decodedValue)
        return String(decryptedVal)
    }

    private fun generateRandomIV(ctx: Context) {
        val pref = ctx.getSharedPreferences(SHARED_PREFENCE_NAME, Context.MODE_PRIVATE)
        val publicIV = pref.getString(PUBLIC_IV, null)
        if (publicIV == null) {
            val random = SecureRandom()
            val generated = random.generateSeed(12)
            val generatedIVstr = Base64.encodeToString(generated, Base64.DEFAULT)
            val edit = pref.edit()
            edit.putString(Constants.PUBLIC_IV_PERSONAL, generatedIVstr)
            edit.putString(PUBLIC_IV, generatedIVstr)
            edit.apply()
        }
    }

    private fun getStringFromSharedPrefs(key: String, ctx: Context): String? {
        val prefs = ctx.getSharedPreferences(Constants.Preference, 0)
        return prefs.getString(key, null)
    }

    companion object {
        private const val RSA_MODE = "RSA/ECB/PKCS1Padding"
        private const val AES_MODE_M = "AES/GCM/NoPadding"
        private const val KEY_ALIAS = "KEY"
        private const val AndroidKeyStore = "AndroidKeyStore"
        const val SHARED_PREFENCE_NAME = "SAVED_TO_SHARED"
        const val ENCRYPTED_KEY = "ENCRYPTED_KEY"
        const val PUBLIC_IV = "PUBLIC_IV"
        private var keyHelper: KeyHelper? = null
        fun getInstance(ctx: Context): KeyHelper? {
            if (keyHelper == null) {
                try {
                    keyHelper = KeyHelper(ctx)
                } catch (e: NoSuchPaddingException) {
                    e.printStackTrace()
                } catch (e: NoSuchProviderException) {
                    e.printStackTrace()
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                } catch (e: InvalidAlgorithmParameterException) {
                    e.printStackTrace()
                } catch (e: KeyStoreException) {
                    e.printStackTrace()
                } catch (e: CertificateException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return keyHelper
        }
    }

    init {
        generateEncryptKey(ctx)
        generateRandomIV(ctx)
    }
}