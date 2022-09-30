package pl.mjurek.util

import com.sun.org.slf4j.internal.Logger
import com.sun.org.slf4j.internal.LoggerFactory

fun <T> loggerFor(clazz: Class<T>): Logger = LoggerFactory.getLogger(clazz)
