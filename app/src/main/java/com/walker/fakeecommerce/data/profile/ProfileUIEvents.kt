package com.walker.fakeecommerce.data.profile

sealed class ProfileUIEvents {

    object GetProfile: ProfileUIEvents()

    object DeleteAccount: ProfileUIEvents()

    object Logout: ProfileUIEvents()

    object CleanProfile: ProfileUIEvents()

    object EditProfile: ProfileUIEvents()

    data class HasNameChanged(val name: String): ProfileUIEvents()

}