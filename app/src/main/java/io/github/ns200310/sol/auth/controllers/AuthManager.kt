package io.github.ns200310.sol.auth.controllers
import io.github.jan.supabase.auth.SignOutScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.ns200310.sol.supabase_config.supabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthManager {
    // sign in with email and password
     fun SignInWithEmailAndPassword(email: String, password: String):Flow<AuthResponse> = flow{
        try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            emit(AuthResponse.Success)
        }
        catch (e: Exception) {
            emit(
                AuthResponse.Error(
                message = e.message?:"Unknown error occurred"
                )
            )
        }
    }

    // sign up with email and password
     fun SignUpWithEmailAndPassword(email: String, password: String):Flow<AuthResponse> = flow{
        try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
        }
        catch (e: Exception) {
            emit(
                AuthResponse.Error(
                message = e.message?:"Unknown error occurred"
                )
            )
        }
    }

    // state of user
    fun OnAuthStateChanged(): Flow<Boolean> = flow {
        try {
            supabase.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> emit(true)
                    is SessionStatus.NotAuthenticated -> emit(false)
                    SessionStatus.Initializing -> println("Initializing")
                    is SessionStatus.RefreshFailure -> println("Refresh failure: ${status.cause}")
                }
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            emit(false)
        }
    }
    fun SignOutWithEmailAndPassword(): Flow<AuthResponse> = flow {
        println("Logging out")
        try {
            supabase.auth.signOut()
            emit(AuthResponse.Success)
            println("Logged out successfully")
        } catch (e: Exception) {

            emit(
                AuthResponse.Error(
                    message = e.localizedMessage ?: "Unknown error occurred"
                )
            )
            println("Error logging out: ${e.localizedMessage}")
        }
    }


    fun ResetPassword(email: String) :Flow<AuthResponse> = flow {
        try {
            supabase.auth.resetPasswordForEmail(email)
        } catch (e: Exception) {
            emit(
                AuthResponse.Error(
                    message = e.message ?: "Unknown error occurred"
                )
            )
        }
    }

}