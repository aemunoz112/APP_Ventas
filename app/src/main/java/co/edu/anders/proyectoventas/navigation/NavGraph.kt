package co.edu.anders.proyectoventas.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import co.edu.anders.proyectoventas.feature.Splash.SplashScreen
import co.edu.anders.proyectoventas.feature.auth.LoginScreen
import co.edu.anders.proyectoventas.feature.auth.SignUpScreen
import co.edu.anders.proyectoventas.feature.dashboard.DashboardScreen
import co.edu.anders.proyectoventas.feature.home.HomeScreen
import co.edu.anders.proyectoventas.feature.search.SearchScreen
import co.edu.anders.proyectoventas.feature.product.ProductDetailScreen
import co.edu.anders.proyectoventas.feature.orders.OrdersScreen
import co.edu.anders.proyectoventas.feature.orders.OrderDetailScreen
import co.edu.anders.proyectoventas.feature.assistant.AssistantScreen
import co.edu.anders.proyectoventas.feature.profile.ProfileScreen
import co.edu.anders.proyectoventas.ui.components.appbar.CustomAppBar

/**
 * Grafo de Navegación Principal
 * Define cómo se conectan todas las pantallas de la app de ventas de acero
 *
 * @param navController: Controlador de navegación
 * @param startDestination: Pantalla inicial (por defecto: Splash)
 */
@Composable
fun ProyectoVentasNavGraph(
    navController: NavHostController,
    startDestination: String = Routes.SPLASH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Screen
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToLogin = {
                    // Navega a Login y elimina Splash del stack
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    // Si ya está autenticado, navega directamente al Dashboard
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // Login Screen
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToSignUp = {
                    // Navega a Sign Up
                    navController.navigate(Routes.SIGN_UP)
                },
                onLoginSuccess = {
                    // Navega al Dashboard después de login exitoso
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Sign Up Screen
        composable(Routes.SIGN_UP) {
            SignUpScreen(
                onNavigateToLogin = {
                    // Vuelve a Login
                    navController.popBackStack()
                },
                onSignUpSuccess = {
                    // Navega al Home después de registrarse exitosamente
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SIGN_UP) { inclusive = true }
                    }
                }
            )
        }

        // Home Screen (Redirect to Dashboard)
        composable(Routes.HOME) {
            // Redirigir automáticamente al Dashboard
            navController.navigate(Routes.DASHBOARD) {
                popUpTo(Routes.HOME) { inclusive = true }
            }
        }

        // Dashboard Screen (Pantalla Principal Empresarial)
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNavigateToSearch = {
                    navController.navigate(Routes.SEARCH)
                },
                onNavigateToOrders = {
                    navController.navigate(Routes.ORDERS)
                },
                onNavigateToProducts = {
                    navController.navigate(Routes.SEARCH)
                },
                onNavigateToAssistant = {
                    navController.navigate(Routes.ASSISTANT)
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }

        // Assistant Screen (Asistente Virtual)
        composable(Routes.ASSISTANT) {
            AssistantScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onNavigateToSearch = {
                    navController.navigate(Routes.SEARCH)
                },
                onNavigateToOrders = {
                    navController.navigate(Routes.ORDERS)
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }

        // Product Detail Screen
        composable(
            route = Routes.PRODUCT_DETAIL,
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            
            ProductDetailScreen(
                productId = productId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Search Screen
        composable(Routes.SEARCH) {
            SearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onProductClick = { productId ->
                    navController.navigate(Routes.productDetail(productId))
                },
                onNavigateToHome = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onNavigateToAssistant = {
                    navController.navigate(Routes.ASSISTANT)
                },
                onNavigateToOrders = {
                    navController.navigate(Routes.ORDERS)
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }

        // Orders Screen
        composable(Routes.ORDERS) {
            OrdersScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onOrderClick = { orderId ->
                    navController.navigate(Routes.orderDetail(orderId))
                },
                onNavigateToHome = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onNavigateToSearch = {
                    navController.navigate(Routes.SEARCH)
                },
                onNavigateToAssistant = {
                    navController.navigate(Routes.ASSISTANT)
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }

        // Order Detail Screen
        composable(
            route = Routes.ORDER_DETAIL,
            arguments = listOf(
                navArgument("orderId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            
            OrderDetailScreen(
                orderId = orderId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Profile Screen
        composable(Routes.PROFILE) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onNavigateToSearch = {
                    navController.navigate(Routes.SEARCH)
                },
                onNavigateToAssistant = {
                    navController.navigate(Routes.ASSISTANT)
                },
                onNavigateToOrders = {
                    navController.navigate(Routes.ORDERS)
                },
                onLogout = {
                    // Cerrar sesión y navegar al Login
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }
    }
}

