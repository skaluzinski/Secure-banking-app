package com.example.securebankingapp.data.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class LoginUserModel(
    val email: String?,
    val password: String,
)

@Serializable
data class RegisterModel(
    val name: String,
    val surname: String,
    val email: String,
    val password: String
)

@Serializable
data class SecureUserModelWithId(
    val name: String,
    val email: String,
    val id: Int
)

@Serializable
data class PrivateUserModel(
    val name: String,
    val email: String,
    val balance: Float,
    val accountNumber: String
)

@Serializable
data class UserModel(
    val name: String,
    val email: String,
    val balance: Float,
    val transactions: List<RevisedTransaction>
)

@Serializable
data class RevisedTransaction(
    val transactionId: Long,
    @Serializable(with = LocalDateSerializer::class)
    val transactionDate: LocalDate,
    val senderEmail: String,
    val recipientEmail: String?,
    val balanceBefore: Float,
    val balanceAfter: Float,
    val type: String,
    val title: String,
    val transactionAmount: Float,
)

object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val result = value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        encoder.encodeString(result)
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }
}