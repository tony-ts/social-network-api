package com.edu.sna.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class AuthUser(
    private var username: String,
    private var password: String,
    private var authorities: MutableCollection<GrantedAuthority> = mutableListOf(SimpleGrantedAuthority(ROLE_USER)),
    private var enabled: Boolean = true
) : UserDetails {

    companion object {
        const val ROLE_USER: String = "ROLE_USER"
    }

    override fun getUsername() = this.username

    override fun getPassword() = this.password

    override fun getAuthorities() = this.authorities

    override fun isEnabled() = this.enabled

    override fun isCredentialsNonExpired() = true

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true
}