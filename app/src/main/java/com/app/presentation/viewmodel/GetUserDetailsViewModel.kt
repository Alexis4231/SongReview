import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.app.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GetUserDetailsViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var user = mutableStateOf<User?>(null)
        private set

    fun loadUserData() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            val email = firebaseUser.email
            if (email != null) {
                db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val document = querySnapshot.documents[0]
                            user.value = document.toObject(User::class.java)
                        } else {
                            Log.e("UserViewModel", "No se encontró un usuario con el correo: $email")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("UserViewModel", "Error al obtener datos del usuario con correo: $email", e)
                    }
            } else {
                Log.e("UserViewModel", "Correo inválido o vacío")
            }
        } else {
            Log.e("UserViewModel", "Usuario no autenticado")
        }
    }
}
