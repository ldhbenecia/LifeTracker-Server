package com.benecia.lifetracker.security.userdetails

import java.security.Principal

class LoginUserPrincipal(
    val loginUser: LoginUser,
) : Principal {

    override fun getName(): String {
        return loginUser.username
    }
}
