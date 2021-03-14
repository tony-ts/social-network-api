package com.edu.sna.dto

data class PageableResponseDto<T>(
    val total: Long,
    val result: List<T>
)