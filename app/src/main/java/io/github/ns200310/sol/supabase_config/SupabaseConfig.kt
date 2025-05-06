package io.github.ns200310.sol.supabase_config

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient

import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.github.ns200310.sol.BuildConfig

val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.SUPABASE_URL,
    supabaseKey = BuildConfig.SUPABASE_ANON_KEY,

) {
    install(Auth)
    install(Postgrest)
    install(Storage)
}
