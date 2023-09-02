package com.walker.fakeecommerce.data.profile

import com.walker.fakeecommerce.model.Profile

data class ProfileUIState(
    var profile: Profile? = null,
    var profileIsLoading: Boolean = false,
    var profileError: Boolean = false,
    var nameField: String = "",
    var nameFieldError: Boolean = false
)