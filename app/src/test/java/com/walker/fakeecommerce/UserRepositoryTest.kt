package com.walker.fakeecommerce

import com.walker.fakeecommerce.datasources.UserDataSource
import com.walker.fakeecommerce.model.AccessToken
import com.walker.fakeecommerce.model.Profile
import com.walker.fakeecommerce.model.User
import com.walker.fakeecommerce.repositories.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import retrofit2.Response

class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userDataSource: UserDataSource

    @Before
    fun setup() {
        userDataSource = mockk()
        userRepository = UserRepository(userDataSource)
    }

    @Test
    fun testPostUser() = runBlocking {

        val name = "John Doe"
        val email = "john@example.com"
        val password = "password"
        val avatar = "https://fake.url/image.jpg"

        val expectedUser = User(
            name, email, password, avatar
        )

        coEvery {
            userDataSource.postUser(any(), any(), any(), any())
        } returns Response.success(expectedUser)

        val result = userRepository.postUser(name, email, password, avatar)

        assertEquals(expectedUser, result.body())
        coVerify { userDataSource.postUser(name, email, password, avatar) }
    }

    @Test
    fun testPostLogin() = runBlocking {
        val accessToken = "xxxx"
        val refreshToken = "yyyy"

        val expectedAccessToken = AccessToken(accessToken, refreshToken)

        coEvery {
            userDataSource.postLogin(any(), any())
        } returns Response.success(expectedAccessToken)

        val result = userRepository.postLogin("john@example.com", "password")

        assertEquals(expectedAccessToken, result.body())
        coVerify { userDataSource.postLogin("john@example.com", "password") }
    }

    @Test
    fun testGetProfile() = runBlocking {
        val id = "123"
        val name = "John Doe"
        val email = "john@example.com"
        val avatar = "https://fake.url/image.jpg"

        val expectedProfile = Profile(
            id, email, name, avatar
        )

        coEvery {
            userDataSource.getProfile()
        } returns Response.success(expectedProfile)

        val result = userRepository.getProfile()

        assertEquals(expectedProfile, result.body())
        coVerify { userDataSource.getProfile() }
    }

    @Test
    fun testUpdateProfile() = runBlocking {
        val id = "123"
        val name = "John Doe"
        val email = "john@example.com"
        val avatar = "https://fake.url/image.jpg"

        val expectedProfile = Profile(
            id, email, name, avatar
        )

        coEvery {
            userDataSource.updateProfile(any())
        } returns Response.success(expectedProfile)

        val profile = Profile(id, email, name, avatar)

        val result = userRepository.updateProfile(
            profile
        )

        assertEquals(expectedProfile, result.body())
        coVerify { userDataSource.updateProfile(profile) }
    }

    @Test
    fun testDeleteProfile() = runBlocking {

        coEvery {
            userDataSource.deleteProfile(any())
        } returns Response.success(true)

        val id = "123"
        val result = userRepository.deleteProfile(id)

        assertTrue(result.body() ?: false)
        coVerify { userDataSource.deleteProfile(id) }
    }

}