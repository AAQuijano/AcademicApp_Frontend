# AcademicApp (Frontend Android)

Este es el proyecto frontend móvil para la aplicación **AcademicApp**, una plataforma de gestión académica desarrollada con **Jetpack Compose + Ktor Client**, integrada con una API FastAPI desplegada en AWS EC2.

---

## 🚀 Funcionalidades principales

### ✅ Autenticación
- Login con validación y JWT
- Registro de usuarios (estudiantes y profesores)
- Persistencia de sesión con `DataStore`
- Decodificación de JWT para obtener `userId` y `role`

### 🧭 Navegación
- Navegación protegida usando `AppNavigation.kt`
- `startDestination` dinámico según sesión
- Redirección automática tras login/register

### 👨‍🏫 Profesor
- Pantalla `ProfessorHomeScreen.kt`
- Visualización de `Scores` por materia
- Botón para crear `Score` y `Calificación` (en construcción)

### 🎓 Estudiante
- Pantalla `StudentHomeScreen.kt`
- Visualización de calificaciones (`Grades`)
- Botón de logout

---

## 📦 Arquitectura del proyecto

- `ui/screens/` → Pantallas Compose
- `viewmodel/` → Lógica de presentación y estados
- `network/` → Llamadas HTTP usando Ktor
- `data/remote/local/` → Persistencia local y manejo de sesión
- `utils/` → Decodificador JWT

---

## 🔐 Manejo de sesión

Implementado en `SessionManager.kt`:

- Guarda `accessToken`, `userId` y `role` con DataStore
- Expone `Flow<String?>` para uso reactivo
- Se integra con `AuthViewModel` y `AppNavigation.kt`

---

## ⚠️ Pendiente por implementar

- [ ] Formulario para crear `Score`
- [ ] Formulario para crear `Calificación`
- [ ] Validación de campos
- [ ] Manejo de errores visual (snackbar/dialog)

---

## ✅ Requisitos

- Android Studio Flamingo+
- API nivel mínimo: 24 (Android 7.0)
- Internet (para consumir la API FastAPI)

---

## 📡 API utilizada

Se conecta a un backend FastAPI desplegado en EC2:

- Endpoint base configurado en `ApiConfig.kt`
- Autenticación JWT incluida en todos los requests protegidos

---

## 🧠 Autor

Desarrollado por **Antonio Quijano** como proyecto académico de ingeniería de software.