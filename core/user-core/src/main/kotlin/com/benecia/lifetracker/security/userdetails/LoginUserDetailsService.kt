package com.benecia.lifetracker.security.userdetails

import com.benecia.lifetracker.user.service.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LoginUserDetailsService(
    private val userService: UserService,
) : UserDetailsService {

    override fun loadUserByUsername(userId: String): UserDetails {
        val uuid = UUID.fromString(userId)
        val userInfo = userService.findById(uuid)
        val user = userInfo.toUser()
        return LoginUser.from(user)
    }
}
