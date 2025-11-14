package com.alexmls.lazypizza.authorization.data

import com.alexmls.lazypizza.core.common.ActivityProvider
import com.alexmls.lazypizza.core.domain.auth.AuthRepository
import com.alexmls.lazypizza.core.domain.auth.AuthState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val activityProvider: ActivityProvider,
): AuthRepository {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Anonymous)
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var verificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    override suspend fun startPhoneVerification(phone: String) {
        require(phone.isNotBlank()) { "phone must not be blank" }

        val activity = activityProvider.currentActivity
            ?: throw IllegalStateException("No active Activity for phone auth")

        return suspendCancellableCoroutine { cont ->
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithCredential(credential, cont)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    if (cont.isActive) cont.resumeWithException(e)
                }

                override fun onCodeSent(
                    verId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    verificationId = verId
                    resendToken = token
                    if (cont.isActive) cont.resume(Unit)
                }
            }

            val builder = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)

            resendToken?.let { builder.setForceResendingToken(it) }

            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }
    }

    override suspend fun verifyCode(code: String) {
        val verId = verificationId
            ?: throw IllegalStateException("No verificationId, call startPhoneVerification first")

        val credential = PhoneAuthProvider.getCredential(verId, code)

        return suspendCancellableCoroutine { cont ->
            signInWithCredential(credential, cont)
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
        verificationId = null
        resendToken = null
        _authState.value = AuthState.Anonymous
    }

    private fun signInWithCredential(
        credential: PhoneAuthCredential,
        cont: CancellableContinuation<Unit>?
    ) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    val ex = task.exception ?: Exception("Sign in failed")
                    cont?.takeIf { it.isActive }?.resumeWithException(ex)
                    return@addOnCompleteListener
                }

                val user = task.result?.user
                _authState.value = AuthState.Authenticated(
                    userId = user?.uid.orEmpty(),
                    phone = user?.phoneNumber
                )

                cont?.takeIf { it.isActive }?.resume(Unit)
            }
    }
}