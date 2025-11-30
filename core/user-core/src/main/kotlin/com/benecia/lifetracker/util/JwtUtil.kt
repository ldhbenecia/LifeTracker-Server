package com.benecia.lifetracker.util

import com.benecia.lifetracker.security.userdetails.LoginUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date
import java.util.UUID

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.expiration}") private val expiration: Long,
) {

    private val key: Key by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray())
    }

    fun generateToken(userId: UUID, email: String, displayName: String, profileImageUrl: String?): String {
        val builder = Jwts.builder()
            .setSubject(userId.toString())
            .claim("email", email)
            .claim("displayName", displayName)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(key, SignatureAlgorithm.HS512)

        if (profileImageUrl != null) {
            builder.claim("profileImageUrl", profileImageUrl)
        }

        return builder.compact()
    }

    fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        val claims = extractAllClaims(token)

        val userId = UUID.fromString(claims.subject)
        val email = claims.get("email", String::class.java) ?: ""
        val displayName = claims.get("displayName", String::class.java) ?: ""
        val profileImageUrl = claims.get("profileImageUrl", String::class.java) ?: ""

        val loginUser = LoginUser(
            id = userId,
            email = email,
            displayName = displayName,
            profileImageUrl = profileImageUrl,
        )

        return UsernamePasswordAuthenticationToken(
            loginUser,
            null,
            listOf(SimpleGrantedAuthority("ROLE_USER")), // 기본 권한 부여 (필요시 claims에서 꺼냄)
        )
    }

    fun validateToken(token: String): Boolean {
        try {
            val claims = extractAllClaims(token)
            return !claims.expiration.before(Date())
        } catch (e: Exception) {
            return false
        }
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun extractExpiration(token: String): Date {
        return extractAllClaims(token).expiration
    }

    fun isTokenExpired(token: String): Boolean {
        return try {
            extractExpiration(token).before(Date())
        } catch (e: Exception) {
            true
        }
    }
}
