package el.ka.rockdog.service.repository

import el.ka.rockdog.service.model.Song
import el.ka.rockdog.service.model.User

object UsersRepository {
  suspend fun getUser(id: String): User? {
    val ref = FirebaseService.usersCollection.document(id)
    val doc = FirebaseService.fetchDocument(ref) ?: return null

    val user = doc.toObject(User::class.java)!!
    user.uid = doc.id
    return user
  }
}