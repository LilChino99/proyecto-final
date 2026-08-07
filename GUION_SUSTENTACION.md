# 🎙️ Guion de Sustentación Individual (Video de 15 Minutos)

**Proyecto Final:** GameVault — Catálogo de Videojuegos con Reseñas Propias  
**Asignatura:** Aplicaciones Móviles  
**Estudiante:** Nicolás Constante (LilChino99)  
**Tiempo Total Estimado:** 15:00 minutos  

---

## ⏱️ Distribución del Tiempo por Secciones

| Minuto | Sección | Objetivo Principal | Rúbrica Evaluada |
|:---|:---|:---|:---|
| **0:00 - 1:30** | 1. Introducción y Demostración General | Presentación del estudiante, objetivos del proyecto y muestra rápida de la app. | Sustentación (10%) |
| **1:30 - 4:00** | 2. Demostración en Vivo de Funcionalidades | Recorrido por las 5 pantallas, búsqueda, modo oscuro y creación de reseña con foto. | UI/UX (15%) |
| **4:00 - 7:00** | 3. Arquitectura (Clean Architecture + MVVM) | Explicación del diagrama de capas (`data`, `domain`, `ui`), `StateFlow` y desacoplamiento. | Arquitectura (20%) |
| **7:00 - 9:30** | 4. Consumo de API Remota (IGDB v4 + Retrofit) | Explicación de Retrofit, OAuth de Twitch, DTOs, mappers y manejo de estados (Loading/Success/Error). | API Remota (15%) |
| **9:30 - 11:30** | 5. Persistencia Local (Room + DataStore) | Explicación de la BD Room (`ReviewEntity`, `ReviewDao` con `Flow`) y DataStore de preferencias. | Persistencia (15%) |
| **11:30 - 13:30** | 6. Hardware y Permisos (Cámara + FileProvider) | Explicación de `TakePicture()`, `FileProvider` y gestión del permiso `CAMERA` en tiempo de ejecución. | Hardware/Permisos (15%) |
| **13:30 - 15:00** | 7. Despliegue, Conclusiones y Cierre | Muestra de binarios firmados (`.aab` y `.apk`), lecciones aprendidas y despedida. | Despliegue (10%) |

---

## 📝 Guion Detallado Paso a Paso (Qué decir y qué mostrar)

### 🎬 1. Introducción (0:00 - 1:30)
* **Qué mostrar en pantalla:** La portada del proyecto o el emulador con la app abriéndose.
* **Lo que debes decir:**
  > *"Buenas tardes profesor y compañeros. Mi nombre es Nicolás Constante y esta es la sustentación de mi proyecto final individual para la asignatura de Aplicaciones Móviles. Mi proyecto se llama **GameVault**, una aplicación Android nativa completa desarrollada con Jetpack Compose. El objetivo de GameVault es permitir a los usuarios explorar un catálogo extenso de videojuegos consumiendo una API remota en tiempo real y, al mismo tiempo, gestionar sus propias reseñas locales almacenadas en el dispositivo, acompañadas de fotos tomadas directamente con la cámara física del teléfono."*

---

### 📲 2. Demostración en Vivo de Funcionalidades (1:30 - 4:00)
* **Qué mostrar en pantalla:** Interactuar con el emulador o celular en vivo.
* **Lo que debes decir:**
  > *"Comenzemos viendo la app en funcionamiento. En la pantalla principal **Home**, tenemos una `LazyColumn` que carga el catálogo de videojuegos. Voy a usar la barra de búsqueda para filtrar un juego, por ejemplo 'Zelda' o 'Witcher'. Noten cómo la búsqueda aplica un debounce de 500 milisegundos para no saturar la red.*  
  > *Hacemos clic en un juego para navegar a **GameDetailScreen**. Aquí vemos la portada en alta resolución cargada dinámicamente con la librería Coil, la calificación, fecha de lanzamiento, géneros, desarrolladores y la descripción devuelta por la API.*  
  > *Ahora presionamos 'Escribir Reseña'. Esto nos lleva a **CreateReviewScreen**. Selecciono 5 estrellas, escribo una opinión y toco 'Tomar foto con la Cámara'. El sistema me solicita el permiso de cámara en tiempo de ejecución. Si acepto, abre la cámara, tomo la foto y se adjunta la vista previa. Al hacer clic en 'Guardar Reseña', los datos se guardan en Room.*  
  > *Finalmente, en la pestaña **Mis Reseñas**, vemos la reseña guardada con su foto adjunta. Y en la pestaña **Ajustes**, puedo alternar el modo oscuro, el cual cambia el tema de la app en tiempo real mediante DataStore."*

---

### 🏛️ 3. Arquitectura del Proyecto: Clean Architecture + MVVM (4:00 - 7:00)
* **Qué mostrar en pantalla:** El código fuente en Android Studio, mostrando la estructura de carpetas `data`, `domain` y `ui`.
* **Lo que debes decir:**
  > *"Para la arquitectura de GameVault elegí **Clean Architecture** estructurada en tres capas estrictas: `domain`, `data` y `ui`.*  
  > *1. **Capa de Dominio (`domain/`):** Es Kotlin puro, libre de dependencias de Android. Contiene los modelos de dominio puros como `Game`, `GameDetail` y `Review`, e interfaces de repositorio como `GameRepository` y `ReviewRepository`.*  
  > *2. **Capa de Datos (`data/`):** Implementa estas interfaces. Aquí se encuentra Retrofit (`IgdbApiService`), Room (`GameVaultDatabase`) y DataStore.*  
  > *3. **Capa de Presentación (`ui/`):** Utiliza el patrón **MVVM** con Unidirectional Data Flow (UDF). Los ViewModels exponen un `StateFlow<UiState>` que los Composables observan. Lo más importante es que los ViewModels **únicamente** conocen las interfaces del repositorio de la capa de dominio; no tienen acceso directo a Retrofit ni a los DAOs de Room, lo que garantiza un desacoplamiento completo y facilita las pruebas unitarias."*

---

### 🌐 4. Consumo de API Remota con Retrofit (7:00 - 9:30)
* **Qué mostrar en pantalla:** El archivo `RetrofitClient.kt`, `IgdbApiService.kt` y `GameMapper.kt`.
* **Lo que debes decir:**
  > *"Para el consumo de datos remotos se cambió la API a **IGDB v4 por Twitch**, una de las bases de datos de videojuegos más completas del mundo. Para comunicarnos con ella utilizamos **Retrofit** y **Gson**.*  
  > *La autenticación se realiza mediante el flujo OAuth2 Client Credentials con Twitch, obteniendo un Bearer Token.*  
  > *Manejamos visiblemente los tres estados fundamentales en la UI:*  
  > *- **Loading:** Muestra un indicador circular mientras se descargan los datos.*  
  > *- **Success:** Renderiza la lista con las tarjetas de juegos.*  
  > *- **Error:** En caso de no tener conexión a internet o haber un timeout, la app captura la excepción y muestra un mensaje amigable con un botón de 'Reintentar'."*

---

### 💾 5. Persistencia Local con Room y DataStore (9:30 - 11:30)
* **Qué mostrar en pantalla:** `GameVaultDatabase.kt`, `ReviewDao.kt` y `UserPreferencesDataStore.kt`.
* **Lo que debes decir:**
  > *"Para la persistencia local utilizamos dos tecnologías de Jetpack:*  
  > *1. **Room Database:** Para almacenar datos estructurados del usuario. Definimos la entidad `ReviewEntity` y el DAO `ReviewDao`. Una decisión clave de diseño fue usar `Flow<List<ReviewEntity>>` en los métodos de consulta. Gracias a Flow, la pantalla 'Mis Reseñas' es reactiva: cuando el usuario guarda o elimina una reseña, Room emite el nuevo listado y la UI se actualiza automáticamente sin recargar la pantalla.*  
  > *2. **DataStore Preferences:** Para los ajustes ligeros del usuario, específicamente el toggle de Modo Oscuro (`isDarkMode`) y el criterio de orden predeterminado. A diferencia de SharedPreferences, DataStore es asíncrono y reactivo."*

---

### 📸 6. Hardware y Permisos en Tiempo de Ejecución (11:30 - 13:30)
* **Qué mostrar en pantalla:** `AndroidManifest.xml`, `PhotoFileProvider.kt` y `CreateReviewScreen.kt`.
* **Lo que debes decir:**
  > *"Una de las características requeridas es la integración con el hardware de la **Cámara**.*  
  > *Para lograrlo de forma segura en Android:*  
  > *1. Configuré un `FileProvider` en el AndroidManifest y `file_paths.xml` para compartir URIs de archivos de imagen locales con la cámara del sistema.*  
  > *2. En `CreateReviewScreen`, solicito el permiso `android.permission.CAMERA` en tiempo de ejecución utilizando `rememberLauncherForActivityResult(RequestPermission())`.*  
  > *3. **Manejo de rechazo de permiso:** Si el usuario niega el permiso de cámara, la aplicación maneja el caso elegantemente mostrando un `AlertDialog` que explica la situación y le permite continuar y **guardar la reseña normalmente sin foto**, o volver a intentar solicitar el permiso."*

---

### 📦 7. Despliegue, Conclusiones y Cierre (13:30 - 15:00)
* **Qué mostrar en pantalla:** Las carpetas `app/build/outputs/apk/release/` y `app/build/outputs/bundle/release/` con los archivos `.apk` y `.aab`.
* **Lo que debes decir:**
  > *"Para el despliegue de la aplicación, generé una clave de firma keystore RSA de 2048 bits (`gamevault_release_key.jks`) y configuré los `signingConfigs` en Gradle.*  
  > *Se compilaron exitosamente dos binarios firmados:*  
  > *- El archivo **`.aab` firmado** (`app-release.aab`), listo para subir a la Google Play Store.*  
  > *- El archivo **`.apk` firmado** (`app-release.apk`), para instalación directa en dispositivos.*  
  > *En conclusión, este proyecto me permitió aplicar todos los conceptos vistos en clase: arquitectura limpia, persistencia híbrida con Room y DataStore, consumo de APIs REST modernas y manejo responsable de permisos de hardware. Muchas gracias por su atención."*
