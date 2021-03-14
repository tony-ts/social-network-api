package com.edu.sna.repository.impl

import org.springframework.r2dbc.core.DatabaseClient


/**
 * Bind a nullable value to a parameter identified by its {@code name}.
 * @param name the name of the parameter
 * @param value the value to bind
 * @param clazz the parameter type
 */
fun DatabaseClient.GenericExecuteSpec.bind(
    name: String,
    value: Any?,
    clazz: Class<*>
): DatabaseClient.GenericExecuteSpec =
    if (value != null) bind(name, value) else bindNull(name, clazz)