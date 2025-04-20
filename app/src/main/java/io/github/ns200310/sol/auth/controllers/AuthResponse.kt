package io.github.ns200310.sol.auth.controllers

interface AuthResponse {
    data object Success : AuthResponse
    data class Error(
        val message: String
    ) : AuthResponse

}