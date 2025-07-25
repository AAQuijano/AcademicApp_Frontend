README - PANTALLAS ACTUALIZADAS

Este archivo contiene dos pantallas de Jetpack Compose completamente integradas con AuthViewModel y SessionManager:

1. LoginScreen.kt
2. RegisterScreen.kt

Ambas utilizan ViewModelFactory para inyectar correctamente las dependencias necesarias como ApiService y el contexto de la app.

🔐 LoginScreen.kt:
- Realiza login llamando a AuthViewModel.login()
- Observa el estado de LoginState (Loading, Success, Error)
- Llama a onLoginSuccess(token) si el login fue exitoso

📝 RegisterScreen.kt:
- Realiza registro llamando a AuthViewModel.register()
- Observa el estado de RegisterState
- Llama a onRegisterSuccess() si el registro fue exitoso

📦 Para usar estas pantallas:
1. Asegúrate de tener ViewModelFactory.kt implementado.
2. Reemplaza tus pantallas actuales por estas.
3. Verifica que tu navegación (`AppNavigation.kt`) maneje correctamente los callbacks onLoginSuccess y onRegisterSuccess.

Estas pantallas están listas para producción y totalmente conectadas con tu backend FastAPI.