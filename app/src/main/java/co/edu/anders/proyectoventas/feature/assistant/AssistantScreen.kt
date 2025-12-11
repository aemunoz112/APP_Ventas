package co.edu.anders.proyectoventas.feature.assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import co.edu.anders.proyectoventas.ui.components.appbar.CustomAppBar
import co.edu.anders.proyectoventas.ui.components.bottomnav.BottomNavBar
import co.edu.anders.proyectoventas.ui.components.chat.ChatBubble
import co.edu.anders.proyectoventas.ui.components.chat.ChatInput
import co.edu.anders.proyectoventas.ui.theme.ProyectoVentasTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage(
    val id: Int,
    val message: String,
    val isUser: Boolean,
    val timestamp: String
)

@Composable
fun AssistantScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    id = 1,
                    message = "¡Hola! Soy tu asistente virtual de ventas. ¿En qué puedo ayudarte hoy? Puedo ayudarte a buscar productos de acero, consultar especificaciones técnicas, dimensiones y disponibilidad.",
                    isUser = false,
                    timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                )
            )
        )
    }
    
    var currentMessage by remember { mutableStateOf("") }
    var selectedBottomItem by remember { mutableIntStateOf(2) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }
    
    ProyectoVentasTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Scaffold(
                containerColor = Color.White,
                topBar = {
                    CustomAppBar(
                        title = "Asistente Virtual"
                    )
                },
                bottomBar = {
                    BottomNavBar(
                        selectedItem = selectedBottomItem,
                        onItemSelected = { index ->
                            selectedBottomItem = index
                            when (index) {
                                0 -> onNavigateToHome()
                                1 -> onNavigateToSearch()
                                2 -> {} // Ya estamos en Assistant
                                3 -> onNavigateToOrders()
                                4 -> onNavigateToProfile()
                            }
                        }
                    )
                }
            ) { padding ->
                Column(modifier = modifier.fillMaxSize().background(Color.White)) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(padding)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                items(messages) { message ->
                    ChatBubble(
                        message = message.message,
                        isUser = message.isUser,
                        timestamp = message.timestamp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                    }
                    
                    ChatInput(
                        message = currentMessage,
                        onMessageChange = { currentMessage = it },
                        onSendClick = {
                            if (currentMessage.isNotBlank()) {
                                val userMessageText = currentMessage
                                val userMessage = ChatMessage(
                                    id = messages.size + 1,
                                    message = userMessageText,
                                    isUser = true,
                                    timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                )
                                
                                messages = messages + userMessage
                                currentMessage = ""
                                
                                // Simular respuesta del asistente después de un delay
                                coroutineScope.launch {
                                    delay(1000)
                                    val assistantResponse = ChatMessage(
                                        id = messages.size + 2,
                                        message = "Entiendo que buscas información sobre: \"$userMessageText\". Te puedo ayudar a encontrar productos de acero, consultar dimensiones, pesos teóricos y disponibilidad. ¿Qué producto específico te interesa?",
                                        isUser = false,
                                        timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                    )
                                    messages = messages + assistantResponse
                                }
                            }
                        },
                        modifier = Modifier.padding(bottom = 80.dp) // Padding para evitar que se oculte detrás del bottomBar
                    )
                }
            }
        }
    }
}

