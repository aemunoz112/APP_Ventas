package co.edu.anders.proyectoventas.feature.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.anders.proyectoventas.core.util.getInitials
import co.edu.anders.proyectoventas.data.local.UserPreferences
import co.edu.anders.proyectoventas.data.repository.AuthRepository
import co.edu.anders.proyectoventas.data.repository.LocationRepository
import co.edu.anders.proyectoventas.ui.components.appbar.CustomAppBar
import co.edu.anders.proyectoventas.ui.components.bottomnav.BottomNavBar
import co.edu.anders.proyectoventas.ui.components.buttons.PrimaryButton
import co.edu.anders.proyectoventas.ui.components.cards.InfoCard
import co.edu.anders.proyectoventas.ui.components.spacer.VerticalSpacer
import co.edu.anders.proyectoventas.ui.theme.PrimaryBlue
import co.edu.anders.proyectoventas.ui.theme.ProyectoVentasTheme
import co.edu.anders.proyectoventas.ui.theme.TextPrimaryLight
import co.edu.anders.proyectoventas.ui.theme.TextSecondaryLight

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToAssistant: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val authRepository = remember { AuthRepository(userPreferences) }
    val locationRepository = remember { LocationRepository() }
    val coroutineScope = rememberCoroutineScope()
    
    var selectedBottomItem by remember { mutableIntStateOf(4) }
    
    // Obtener información del usuario logueado
    val userName = userPreferences.getUserName()
    val userEmail = userPreferences.getUserEmail()
    val rolName = userPreferences.getRolName()
    val userId = userPreferences.getUserId()
    
    // Estados para departamento y ciudad (pueden actualizarse desde la API)
    var departamentoNombre by remember { mutableStateOf(userPreferences.getUserDepartamento()) }
    var ciudadNombre by remember { mutableStateOf(userPreferences.getUserCiudad()) }
    var isLoadingUbicacion by remember { mutableStateOf(false) }
    
    // Obtener nombres desde la API de Node.js si hay IDs
    LaunchedEffect(Unit) {
        val departamentoId = userPreferences.getUserDepartamentoId()
        val ciudadId = userPreferences.getUserCiudadId()
        
        android.util.Log.d("ProfileScreen", "=== INICIO CARGA PERFIL ===")
        android.util.Log.d("ProfileScreen", "Departamento ID guardado: $departamentoId")
        android.util.Log.d("ProfileScreen", "Ciudad ID guardada: $ciudadId")
        android.util.Log.d("ProfileScreen", "Departamento nombre guardado: '$departamentoNombre'")
        android.util.Log.d("ProfileScreen", "Ciudad nombre guardada: '$ciudadNombre'")
        
        // SIEMPRE intentar obtener los nombres si hay IDs y los nombres están vacíos, null o "N/A"
        if (departamentoId != null || ciudadId != null) {
            val necesitaActualizarDept = departamentoId != null && 
                                        (departamentoNombre.isEmpty() || 
                                         departamentoNombre == "N/A")
            
            val necesitaActualizarCiudad = ciudadId != null && 
                                           (ciudadNombre.isEmpty() || 
                                            ciudadNombre == "N/A")
            
            android.util.Log.d("ProfileScreen", "Necesita actualizar Dept: $necesitaActualizarDept (ID: $departamentoId, Nombre actual: '$departamentoNombre')")
            android.util.Log.d("ProfileScreen", "Necesita actualizar Ciudad: $necesitaActualizarCiudad (ID: $ciudadId, Nombre actual: '$ciudadNombre')")
            
            if (necesitaActualizarDept || necesitaActualizarCiudad) {
                isLoadingUbicacion = true
                android.util.Log.d("ProfileScreen", "Iniciando obtención de nombres desde API...")
                
                coroutineScope.launch {
                    try {
                        var nuevoDeptNombre = departamentoNombre
                        var nuevoCiuNombre = ciudadNombre
                        
                        // Obtener departamento si es necesario
                        if (necesitaActualizarDept && departamentoId != null) {
                            android.util.Log.d("ProfileScreen", "Obteniendo departamento ID: $departamentoId")
                            val result = locationRepository.getDepartamentoNombre(departamentoId)
                            if (result.isSuccess) {
                                val nombre = result.getOrNull()
                                if (!nombre.isNullOrEmpty() && nombre != "N/A") {
                                    nuevoDeptNombre = nombre
                                    android.util.Log.d("ProfileScreen", "Departamento obtenido: $nuevoDeptNombre")
                                } else {
                                    android.util.Log.w("ProfileScreen", "Departamento nombre es null o vacío: '$nombre'")
                                }
                            } else {
                                android.util.Log.w("ProfileScreen", "Error al obtener departamento: ${result.exceptionOrNull()?.message}")
                            }
                        }
                        
                        // Obtener ciudad si es necesario
                        if (necesitaActualizarCiudad && ciudadId != null) {
                            android.util.Log.d("ProfileScreen", "Obteniendo ciudad ID: $ciudadId")
                            val result = locationRepository.getCiudadNombre(ciudadId)
                            if (result.isSuccess) {
                                val nombre = result.getOrNull()
                                if (!nombre.isNullOrEmpty() && nombre != "N/A") {
                                    nuevoCiuNombre = nombre
                                    android.util.Log.d("ProfileScreen", "Ciudad obtenida: $nuevoCiuNombre")
                                } else {
                                    android.util.Log.w("ProfileScreen", "Ciudad nombre es null o vacío: '$nombre'")
                                }
                            } else {
                                android.util.Log.w("ProfileScreen", "Error al obtener ciudad: ${result.exceptionOrNull()?.message}")
                            }
                        }
                        
                        // Actualizar estados locales y preferencias
                        if (nuevoDeptNombre != departamentoNombre || nuevoCiuNombre != ciudadNombre) {
                            departamentoNombre = nuevoDeptNombre
                            ciudadNombre = nuevoCiuNombre
                            userPreferences.updateUbicacionNombres(nuevoDeptNombre, nuevoCiuNombre)
                            android.util.Log.d("ProfileScreen", "Ubicación actualizada - Dept: '$nuevoDeptNombre', Ciudad: '$nuevoCiuNombre'")
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("ProfileScreen", "Error al obtener ubicación: ${e.message}", e)
                        android.util.Log.e("ProfileScreen", "Stack trace: ${e.stackTraceToString()}")
                    } finally {
                        isLoadingUbicacion = false
                        android.util.Log.d("ProfileScreen", "=== FIN CARGA PERFIL ===")
                    }
                }
            } else {
                android.util.Log.d("ProfileScreen", "No se necesita actualizar ubicación - ya tiene nombres válidos")
            }
        } else {
            android.util.Log.w("ProfileScreen", "No hay IDs de departamento o ciudad guardados")
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
                        title = "Mi Perfil"
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
                                2 -> onNavigateToAssistant()
                                3 -> onNavigateToOrders()
                                4 -> {} // Ya estamos en Profile
                            }
                        }
                    )
                }
            ) { padding ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                        .padding(padding)
                        .padding(horizontal = 20.dp)
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Avatar placeholder
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(PrimaryBlue)
                            .size(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (userName.isNotEmpty()) userName.getInitials() else "U",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = co.edu.anders.proyectoventas.ui.theme.TextPrimaryDark
                        )
                    }
                    
                    VerticalSpacer(8.dp)
                    
                    Text(
                        text = userName.ifEmpty { "Usuario" },
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight
                    )
                    
                    if (rolName.isNotEmpty()) {
                        androidx.compose.material3.Card(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                            colors = androidx.compose.material3.CardDefaults.cardColors(
                                containerColor = PrimaryBlue.copy(alpha = 0.1f)
                            )
                        ) {
                            Text(
                                text = rolName,
                                fontSize = 14.sp,
                                color = PrimaryBlue,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                    
                    VerticalSpacer(16.dp)
                    Divider(
                        color = Color.Gray.copy(alpha = 0.2f),
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    VerticalSpacer(8.dp)
                    
                    Text(
                        text = "Información Personal",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryLight,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    InfoCard(
                        title = "Rol",
                        value = rolName.ifEmpty { "N/A" },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    InfoCard(
                        title = "Email",
                        value = userEmail.ifEmpty { "N/A" },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    InfoCard(
                        title = "Teléfono",
                        value = userPreferences.getUserTelefono().ifEmpty { "N/A" },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    InfoCard(
                        title = "Cédula",
                        value = userPreferences.getUserCedula().ifEmpty { "N/A" },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    InfoCard(
                        title = "Estado",
                        value = userPreferences.getUserEstado().ifEmpty { "N/A" },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    InfoCard(
                        title = "Departamento",
                        value = if (isLoadingUbicacion && departamentoNombre.isEmpty()) {
                            "Cargando..."
                        } else {
                            departamentoNombre.ifEmpty { "N/A" }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    InfoCard(
                        title = "Ciudad",
                        value = if (isLoadingUbicacion && ciudadNombre.isEmpty()) {
                            "Cargando..."
                        } else {
                            ciudadNombre.ifEmpty { "N/A" }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    VerticalSpacer(32.dp)
                    
                    Divider(
                        color = Color.Gray.copy(alpha = 0.2f),
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    VerticalSpacer(24.dp)
                    
                    // Botón de cerrar sesión
                    PrimaryButton(
                        text = "Cerrar Sesión",
                        onClick = {
                            authRepository.logout()
                            Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                            onLogout()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    VerticalSpacer(24.dp)
                }
            }
        }
    }
}

