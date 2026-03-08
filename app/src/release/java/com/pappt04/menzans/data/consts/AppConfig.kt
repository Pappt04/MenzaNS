package com.pappt04.menzans.data.consts

object AppConfig {
    const val BASE_SERVER_URL = "https://apollo4.duckdns.org/"
    const val BASE_API_NAME = "/appapi"
    const val APP_STORE_URL = "https://play.google.com/store/apps/details?id=com.pappt04.menzans"
    // Production API key — must match APP_API_KEY in MenzaNS-server/.env on the production server
    // IMPORTANT: Generate with: openssl rand -hex 16
    const val APP_API_KEY = "cce660ed96c39bfaa51ee831bd7c7a093f94f75a76c7bfaaa9f9c4724df834a7"
    const val SERVER_HOSTNAME = "apollo4.duckdns.org"
    // TODO: Before releasing, replace null with the server's SHA-256 certificate pin.
    // Generate with: openssl s_client -connect apollo4.duckdns.org:443 </dev/null |
    //   openssl x509 -outform DER | openssl dgst -sha256 -binary | openssl enc -base64
    // Then set: val CERT_PIN: String? = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
    val CERT_PIN: String? = null
}
