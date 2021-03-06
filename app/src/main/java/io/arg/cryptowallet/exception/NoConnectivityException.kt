package io.arg.cryptowallet.exception

import java.io.IOException

/**
 * Custom exception for no internet connectivity
 */
class NoConnectivityException(message: String = "No internet connectivity!") : IOException(message)