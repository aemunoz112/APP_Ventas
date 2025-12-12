package co.edu.anders.proyectoventas.feature.dashboard

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import co.edu.anders.proyectoventas.ui.components.appbar.CustomAppBar
import co.edu.anders.proyectoventas.ui.components.bottomnav.BottomNavBar
import co.edu.anders.proyectoventas.ui.theme.DashboardBackground
import co.edu.anders.proyectoventas.ui.theme.ProyectoVentasTheme

/**
 * Dashboard Empresarial con Power BI
 * Pantalla principal que muestra el dashboard de Power BI embebido
 * 
 * IMPORTANTE: Reemplaza la URL con tu URL de Power BI embed
 */
@Composable
fun DashboardScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToAssistant: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
    powerBiUrl: String = "https://app.powerbi.com/view?r=eyJrIjoiMTdmOWE5MGUtNmMwYS00Y2ZlLTgyNTYtNmI4MWY1YTA4YjU1IiwidCI6IjFlOWFhYmU4LTY3ZjgtNGYxYy1hMzI5LWE3NTRlOTI0OTlhZSIsImMiOjR9"  // Cambia esto por tu URL real
) {
    var selectedBottomItem by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var webView: WebView? by remember { mutableStateOf(null) }
    
    ProyectoVentasTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            // Fondo gradiente
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0F172A),
                                Color(0xFF111827),
                                DashboardBackground
                            )
                        )
                    )
            )
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    CustomAppBar(
                        title = "Dashboard Power BI",
                        actions = {
                            IconButton(onClick = { webView?.reload() }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Recargar Dashboard",
                                    tint = Color.White
                                )
                            }
                        }
                    )
                },
                bottomBar = {
                    BottomNavBar(
                        selectedItem = selectedBottomItem,
                        onItemSelected = { index ->
                            selectedBottomItem = index
                            when (index) {
                                0 -> {} // Ya estamos en Dashboard
                                1 -> onNavigateToSearch()
                                2 -> onNavigateToAssistant()
                                3 -> onNavigateToOrders()
                                4 -> onNavigateToProfile()
                            }
                        }
                    )
                }
            ) { padding ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Encabezado visual
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF2563EB).copy(alpha = 0.18f),
                                        Color(0xFF22D3EE).copy(alpha = 0.14f)
                                    )
                                ),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Resumen Ejecutivo",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Visualiza tus KPIs clave y métricas en tiempo real.",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Pill(text = "Power BI", color = Color(0xFF2563EB))
                            Pill(text = "Tiempo real", color = Color(0xFF22D3EE))
                            Pill(text = "KPIs", color = Color(0xFF10B981))
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(18.dp)
                            )
                            .padding(4.dp)
                    ) {
                        AndroidView(
                            factory = { context ->
                                WebView(context).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                    
                                    settings.apply {
                                        javaScriptEnabled = true
                                        domStorageEnabled = true
                                        loadWithOverviewMode = true
                                        useWideViewPort = true
                                        builtInZoomControls = true
                                        displayZoomControls = false
                                        setSupportZoom(true)
                                        allowFileAccess = true
                                        allowContentAccess = true
                                        databaseEnabled = true
                                        cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
                                        userAgentString = settings.userAgentString + " PowerBIEmbedded"
                                    }
                                    
                                    webViewClient = object : WebViewClient() {
                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)
                                            isLoading = false
                                        }
                                        
                                        override fun onReceivedError(
                                            view: WebView?,
                                            errorCode: Int,
                                            description: String?,
                                            failingUrl: String?
                                        ) {
                                            super.onReceivedError(view, errorCode, description, failingUrl)
                                            isLoading = false
                                            Toast.makeText(
                                                context,
                                                "Error al cargar: $description",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                    
                                    webChromeClient = WebChromeClient()
                                    
                                    if (powerBiUrl != "TU_URL_DE_POWER_BI_AQUI") {
                                        loadUrl(powerBiUrl)
                                    } else {
                                        loadData(
                                            """
                                            <html>
                                            <body style='font-family: Arial; padding: 20px; text-align: center;'>
                                                <h2 style='color: #1565C0;'>Configuración Requerida</h2>
                                                <p>Para ver tu dashboard de Power BI:</p>
                                                <ol style='text-align: left; max-width: 600px; margin: 20px auto;'>
                                                    <li>Ve a Power BI y abre tu dashboard</li>
                                                    <li>Haz clic en 'Archivo' → 'Insertar informe' → 'Sitio web o portal'</li>
                                                    <li>Copia la URL de inserción</li>
                                                    <li>En el código, reemplaza "TU_URL_DE_POWER_BI_AQUI" con tu URL</li>
                                                </ol>
                                                <p style='color: #666; font-size: 14px; margin-top: 30px;'>
                                                    Ubicación: DashboardScreen.kt, parámetro powerBiUrl
                                                </p>
                                            </body>
                                            </html>
                                            """.trimIndent(),
                                            "text/html",
                                            "UTF-8"
                                        )
                                        isLoading = false
                                    }
                                    
                                    webView = this
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Pill(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.15f),
        contentColor = color,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

