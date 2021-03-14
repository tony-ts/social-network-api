package com.edu.sna.configuration

import org.springframework.security.core.annotation.AuthenticationPrincipal

/**
 * Helps to receive id of the current user.
 * Use as parameter of method with Mono<Long> as a type.
 * */
@AuthenticationPrincipal(expression = "id")
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class CurrentUserId
