package com.edu.sna.repository.impl

import com.edu.sna.model.TotalResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.support.PageableExecutionUtils


/**
 * Wraps the list into a Page object containing the total number of entries from TotalResponse.
 * @param pageRequest pagination information
 * @return sublist of a list of objects
 * */
fun <T> List<TotalResponse<T>>.wrapPage(pageRequest: PageRequest): Page<T> =
    PageableExecutionUtils.getPage(map { tr -> tr.entity }, pageRequest) {
        first().total
    }
