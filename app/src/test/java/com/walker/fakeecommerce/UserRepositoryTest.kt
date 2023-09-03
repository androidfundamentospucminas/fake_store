package com.walker.fakeecommerce

import com.walker.fakeecommerce.datasources.UserDataSource
import com.walker.fakeecommerce.model.AccessToken
import com.walker.fakeecommerce.model.Profile
import com.walker.fakeecommerce.model.User
import com.walker.fakeecommerce.repositories.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
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
        val avatar = "http://fake.url/image.jpg"

        val expectedUser = User(
            name, email, password, avatar
        )

        coEvery {
            userDataSource.postUser(any(), any(), any(), any())
        } returns Response.success(expectedUser)

        val result = userRepository.postUser(name, email, password, avatar)

        assertEquals(expectedUser, result.body())
    }

    @Test
    fun testPostLogin() = runBlocking {
        // Mock the behavior of userDataSource.postLogin

        val accessToken = "xxxx"
        val refreshToken = "yyyy"

        val accessTokenMock = AccessToken(accessToken, refreshToken)

        coEvery {
            userDataSource.postLogin(any(), any())
        } returns Response.success(accessTokenMock)

        val result = userRepository.postLogin("john@example.com", "password")

        // Assert the result
        assertEquals(accessTokenMock, result.body())
    }

    @Test
    fun testGetProfile() = runBlocking {

        val name = "John Doe"
        val email = "john@example.com"
        val id = "123"
        val avatar = "http://fake.url/image.jpg"

        val expectedProfile = Profile(
            id,
            email,
            name,
            avatar
        )

        coEvery {
            userDataSource.getProfile()
        } returns Response.success(expectedProfile)

        val result = userRepository.getProfile()

        // Assert the result
        assertEquals(expectedProfile, result.body())
    }

    @Test
    fun testEditProfile() = runBlocking {

        val name = "John Doe"
        val email = "john@example.com"
        val id = "123"
        val avatar = "http://fake.url/image.jpg"

        val expectedProfile = Profile(
            id,
            email,
            name,
            avatar
        )

        coEvery {
            userDataSource.editProfile(any())
        } returns Response.success(expectedProfile) // You can customize the return value as needed

        val profile = Profile(
            id,
            email,
            name,
            avatar
        )
        val result = userRepository.editProfile(profile)

        // Assert the result
        assertEquals(expectedProfile, result.body())
    }

    @Test
    fun testDeleteProfile() = runBlocking {
        coEvery {
            userDataSource.deleteProfile(any())
        } returns Response.success(true)

        val result = userRepository.deleteProfile("userId")

        // Assert the result
        assertEquals(true, result.body())
    }
}