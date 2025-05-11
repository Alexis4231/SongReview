import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.R

@Composable
fun HomeScreen(navController: NavController) {
    var selectedProfileItem by remember { mutableStateOf(0) }
    val profileItems = listOf("Canciones", "Artistas","Usuarios")
    val profileIcons = listOf<@Composable () -> Unit>(
        {Icon(Icons.Filled.MusicNote, contentDescription = "MusicNote")},
        {Icon(
            painter = painterResource(id = R.drawable.artist_24dp_1f1f1f_fill0_wght400_grad0_opsz24),
            contentDescription = "Artist",
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )},
        {Icon(Icons.Filled.AccountCircle, contentDescription = "Profiles")}
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF585D5F)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF585D5F)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeHeader()
        NavigationBar(containerColor = Color(0xFF39D0B9)) {
            profileItems.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = profileIcons[index],
                    label = { Text(item) },
                    selected = selectedProfileItem == index,
                    onClick = { selectedProfileItem = index },
                    modifier = Modifier.background(Color(0xFF39D0B9))
                )
            }
        }

        when (selectedProfileItem) {
            0 -> listSongsContent()
            1 -> listArtistsContent()
            2 -> listProfilesContent()
        }
    }
}


@Composable
fun HomeHeader(){
    var search by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val dropdownItems = listOf("Todos","Rock","Pop","Reggae","Blues")
    var selectedItem by remember { mutableStateOf(dropdownItems.first()) }


    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color(0xFF39D0B9),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Buscar", color = Color.White) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color.White
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF39D0B9),
                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.height(56.dp),
                    border = BorderStroke(0.dp, Color.Transparent),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote, // Usa el icono que desees
                        contentDescription = "Género"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = selectedItem)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    dropdownItems.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                selectedItem = item
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

fun listSongsContent(){

}

fun listArtistsContent(){

}

fun listProfilesContent(){

}
