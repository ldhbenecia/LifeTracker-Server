package com.benecia.lifetracker.security.userdetails

import com.benecia.lifetracker.user.service.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class LoginUser(
    val id: UUID,
    val email: String,
    val displayName: String,
) : UserDetails {

    override fun getUsername(): String = id.toString()
    override fun getPassword(): String? = null
    override fun getAuthorities() = listOf<GrantedAuthority>()
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true

    companion object {
        fun from(user: User): LoginUser {
            val id = user.id ?: throw IllegalArgumentException("User id is null")
            return LoginUser(id, user.email, user.displayName)
        }
    }
}
