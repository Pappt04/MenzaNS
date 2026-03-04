package com.pappt04.menzans.data.consts

object AppConfig {
    const val BASE_SERVER_URL = "https://apollo4.duckdns.org/"
    const val BASE_API_NAME = "/menzaapi"
    const val APP_STORE_URL = "https://play.google.com/store/apps/details?id=com.pappt04.menzans"
    // Production API key — must match APP_API_KEY in MenzaNS-server/.env on the production server
    // IMPORTANT: Generate with: openssl rand -hex 16
    const val APP_API_KEY = "CHANGE_ME_must_match_server_APP_API_KEY"
}
