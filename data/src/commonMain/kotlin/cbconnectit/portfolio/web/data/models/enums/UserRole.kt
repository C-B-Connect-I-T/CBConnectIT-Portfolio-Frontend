package cbconnectit.portfolio.web.data.models.enums

enum class UserRole {
    User,
    Moderator,
    Admin;

    companion object {
        fun mapValue(role: String?): UserRole? = when (role) {
            "admin" -> Admin
            "moderator" -> Moderator
            "user" -> User
            else -> null
        }
    }
}
