package com.example.kotlinsupabase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlinsupabase.ui.theme.KotlinSupabaseTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable


val supabase = createSupabaseClient(
    supabaseUrl = "your_url",
    supabaseKey = "your_key"
) {
    install(Postgrest)
    //install other modules
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinSupabaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val notes = runBlocking(Dispatchers.IO) {
                        val results = supabase.from("your_table").select().decodeList<Note>()
                        results.toMutableList()
                    }
                    NotesList(notes)
                }
            }

            // Verificar si Supabase se ha inicializado correctamente
            if (supabase != null) {
                Toast.makeText(this, "Supabase inicializado correctamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al inicializar Supabase", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Serializable
data class  Note(
    val id: Int,
    val body: String,
)

@Composable
fun NotesList(notes: List<Note>){
    LazyColumn{
        items(notes){
                note -> ListItem(headlineContent = { Text(text = note.body) })
        }
    }
}