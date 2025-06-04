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
            val uid = firebaseUser.uid
            db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            user.value = documentSnapshot.toObject(User::class.java)
                        } else {
                            Log.e("UserViewModel", "No se encontró un usuario con uid: $uid")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("UserViewModel", "Error al obtener datos del usuario con uid: $uid", e)
                    }
            } else {
                Log.e("UserViewModel", "Correo inválido o vacío")
            }
    }
}
