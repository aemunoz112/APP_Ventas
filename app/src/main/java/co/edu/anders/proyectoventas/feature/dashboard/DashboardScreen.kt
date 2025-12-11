package co.edu.anders.proyectoventas.feature.dashboard

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = DashboardBackground
        ) {
            Scaffold(
                containerColor = DashboardBackground,
                topBar = {
                    CustomAppBar(
                        title = "Dashboard Power BI",
                        actions = {
                            // Botón para recargar el dashboard
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
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(padding)
                ) {
                    // WebView para mostrar Power BI
                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                
                                // Configuración del WebView
                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                    loadWithOverviewMode = true
                                    useWideViewPort = true
                                    builtInZoomControls = true
                                    displayZoomControls = false
                                    setSupportZoom(true)
                                    
                                    // Configuraciones adicionales para Power BI
                                    allowFileAccess = true
                                    allowContentAccess = true
                                    databaseEnabled = true
                                    
                                    // Cache para mejor rendimiento
                                    cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
                                    
                                    // User Agent (Power BI puede requerirlo)
                                    userAgentString = settings.userAgentString + " PowerBIEmbedded"
                                }
                                
                                // WebViewClient para manejar la carga y errores
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
                                
                                // WebChromeClient para mejor compatibilidad
                                webChromeClient = WebChromeClient()
                                
                                // Cargar la URL de Power BI
                                if (powerBiUrl != "TU_URL_DE_POWER_BI_AQUI") {
                                    loadUrl(powerBiUrl)
                                } else {
                                    // Mensaje de instrucciones si no hay URL configurada
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
                    
                    // Indicador de carga
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

