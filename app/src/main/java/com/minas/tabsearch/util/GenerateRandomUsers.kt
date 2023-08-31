package com.minas.tabsearch.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.minas.tabsearch.data.FriendStatus
import com.minas.tabsearch.data.User
import com.minas.tabsearch.ui.domain.DomainUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class GenerateRandomUsers {
    companion object {
        private const val DEFAULT_USER_COUNT = 100
        private val genericFirstNames = listOf(
            "John", "Mary", "Robert", "Linda", "William", "Patricia", "David", "Jennifer",
            "James", "Elizabeth", "Michael", "Susan", "Charles", "Jessica", "Joseph", "Sarah",
            "Thomas", "Karen", "Christopher", "Nancy", "George", "Andrew", "Maria", "Daniel", "Lisa",
            "Matthew", "Margaret", "Anthony", "Emily", "Donald", "Laura", "Mark", "Kimberly", "Paul",
            "Donna", "Steven", "Michelle", "Kevin", "Ruth", "Brian", "Carol", "Edward", "Amanda"
        )

        private val genericLastNames = listOf(
            "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson",
            "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin",
            "Clark", "Lewis", "Lee", "Walker", "Hall", "Young", "King", "Wright", "Turner",
            "Hill", "Scott", "Cooper", "Green", "Adams", "Baker", "Gonzalez", "Nelson", "Carter",
            "Mitchell", "Perez", "Roberts", "Turner", "Phillips", "Campbell", "Parker", "Evans",
            "Collins", "Stewart", "Sanchez", "Morris", "Reed"
        )

        fun asFlow(howManyUsers: Int = DEFAULT_USER_COUNT): Flow<User> = flow {
            repeat(howManyUsers) {
                val user = generateRandomUser()
                emit(user)
                delay(100) // Delay for demonstration purposes
            }
        }
        private var userIdCounter = 1
        private fun generateRandomUser(): User {
            val firstName = genericFirstNames.random()
            val lastName = genericLastNames.random()
            val friendStatus = FriendStatus.values().random()
            val id = userIdCounter++
            val userName = "user$id"
            return User(
                id = id,
                userName = userName,
                name = firstName,
                lastName = lastName,
                isFollowing = Random.nextBoolean(),
                isFollower = Random.nextBoolean(),
                friendStatus = friendStatus,
            )
        }

        fun getRandomProfilePic(user: DomainUser) = getRandomProfilePic(user.id, user.name, user.lastName)
        fun getRandomProfilePic(seed: Int, name: String, lastName: String): Bitmap {
            val nameInitials = getInitials(name, lastName)

            // Create a Bitmap and Canvas to draw the profile picture
            val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            // Set up the Paint for drawing
            val paint = Paint().apply {
                isAntiAlias = true
                color = generateColor(seed) // Generate a color based on the user's ID
                style = Paint.Style.FILL
                textSize = 40f
                textAlign = Paint.Align.CENTER
            }

            // Draw the background circle
            val radius = 50f
            val centerX = 50f
            val centerY = 50f
            canvas.drawCircle(centerX, centerY, radius, paint)

            // Draw the user's ID as text
            val textBounds = Rect()
            paint.getTextBounds(nameInitials, 0, nameInitials.length, textBounds)
            val textX = centerX
            val textY = centerY + textBounds.height() / 2
            paint.apply {
                color = getContrastColor(color)
            }
            canvas.drawText(nameInitials, textX, textY, paint)

            return bitmap
        }

        private fun getContrastColor(currentColor: Int): Int {
            // Extract the RGB components of the current color
            val red = Color.red(currentColor)
            val green = Color.green(currentColor)
            val blue = Color.blue(currentColor)

            // Calculate the inverted color components
            val invertedRed = 255 - red
            val invertedGreen = 255 - green
            val invertedBlue = 255 - blue

            // Combine the inverted color components to get the opposite color
            return Color.rgb(invertedRed, invertedGreen, invertedBlue)
        }

        private fun getInitials(firstName: String, lastName: String): String {
            val firstInitial = firstName.firstOrNull()?.uppercaseChar() ?: ""
            val lastInitial = lastName.firstOrNull()?.uppercaseChar() ?: ""
            return "$firstInitial$lastInitial"
        }

        private fun generateColor(seed: Int): Int {
            val random = Random(seed.toLong())
            return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        }
    }
}