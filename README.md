# Digital Portfolio

Tarjeta profesional Android y portfolio web para eventos, elevator pitch y networking. La app muestra el mismo contenido público que la web, genera un QR escaneable y conserva una copia local con Room para seguir funcionando sin conexión.

El proyecto está preparado como ejemplo editable para **Jordi Developer**, empresario, Senior Android/iOS Developer y Director de Operaciones. Los datos no verificados (teléfono, correo, empresa, fechas, métricas y enlaces) están marcados como plantillas para evitar presentar información inventada.

## Qué incluye

- App Android nativa, moderna y adaptable con Jetpack Compose y Material 3.
- Vista de tarjeta con foto/iniciales y QR próximo al perfil.
- Diálogo para ampliar el QR y facilitar el escaneo desde otro móvil.
- Experiencia, proyectos, servicios, operaciones, habilidades y contacto.
- Acciones para abrir web, correo, teléfono, WhatsApp, LinkedIn, GitHub y CV.
- Compartir perfil y guardar un contacto mediante intents del sistema.
- Web responsive, pública y sin cuenta, lista para GitHub Pages.
- CV imprimible desde `docs/cv.html`.
- Contenido único en `docs/profile.json`, servido también como endpoint HTTPS.
- Sincronización remota con Retrofit/Moshi y caché offline-first con Room.
- Perfil inicial incluido en assets para que la app arranque incluso sin red.
- Clean Architecture modular, MVVI, BaseViewModel y Koin.
- Tema claro/oscuro y navegación adaptativa: barra inferior en móvil y rail en pantallas amplias.

## Solución técnica elegida

GitHub Pages publica el directorio `docs/`. La web y el endpoint de datos quedan disponibles así:

```text
https://TU_USUARIO.github.io/digital-portfolio/
https://TU_USUARIO.github.io/digital-portfolio/profile.json
https://TU_USUARIO.github.io/digital-portfolio/cv.html
```

La app consulta directamente `profile.json`; no usa la API REST de GitHub, tokens ni un backend. Para este caso es más simple, evita exponer secretos y permite que cualquier persona vea el perfil sin iniciar sesión.

## Requisitos

- Android Studio compatible con AGP 9.1.1.
- JDK 17.
- Android SDK 37.
- Gradle 9.3.1 (incluido mediante Wrapper).

## Primer arranque

1. Descomprime y abre la carpeta `DigitalPortfolio` en Android Studio.
2. Selecciona JDK 17 para Gradle.
3. Espera a que finalice la sincronización.
4. Ejecuta la configuración `app` en un dispositivo o emulador con Android 8.0 o superior.

También puedes compilar desde terminal:

```bash
./gradlew :app:assembleDebug
```

El APK se genera en `app/build/outputs/apk/debug/app-debug.apk`.

## Personalizar tus datos

1. Edita `docs/profile.json`.
2. Añade una fotografía optimizada, por ejemplo `docs/assets/profile.webp`.
3. Publica primero la web o calcula su URL definitiva.
4. Escribe en `photoUrl` la URL HTTPS completa de la foto.
5. Sincroniza la copia inicial Android:

```bash
./gradlew syncProfileContent
```

6. Sustituye `TU_USUARIO` en `gradle.properties`:

```properties
PORTFOLIO_BASE_URL=https://tuusuario.github.io/digital-portfolio/
```

La URL debe terminar en `/`, porque Retrofit la utiliza como URL base.

Consulta [CUSTOMIZATION.md](CUSTOMIZATION.md) para la lista completa de campos que debes revisar.

## Publicar gratis con GitHub Pages

1. Crea un repositorio público llamado `digital-portfolio` en GitHub.
2. Sube el contenido de esta carpeta a la rama `main`.
3. Ve a **Settings → Pages**.
4. En **Build and deployment**, elige **Deploy from a branch**.
5. Selecciona `main` y la carpeta `/docs`.
6. Guarda y espera a que GitHub muestre la URL publicada.
7. Abre `/profile.json` y `/cv.html` para comprobar los tres recursos.
8. Actualiza `PORTFOLIO_BASE_URL`, ejecuta `syncProfileContent` y recompila la app.

No publiques información privada en `profile.json`: todo el directorio web debe considerarse público. Puedes conectar después un dominio propio sin cambiar la arquitectura.

## Flujo offline-first

1. En el primer arranque, si Room está vacío, se carga el `profile.json` incluido en assets.
2. La interfaz observa Room mediante `Flow`.
3. La app solicita `GET profile.json` a GitHub Pages.
4. Si la respuesta es válida, se guarda el JSON completo en Room y la UI se actualiza.
5. Si no hay red o la web falla, se mantiene la última copia almacenada.

Room es aquí una caché robusta y una fuente única observable. GitHub Pages continúa siendo la fuente editable y pública.

## Módulos

| Módulo | Responsabilidad |
|---|---|
| `app` | Entrada Android, BuildConfig y grafo principal de Koin |
| `core:common` | `BaseViewModel` MVVI reutilizable |
| `core:model` | Modelos de dominio independientes |
| `core:database` | Room, entidad, DAO y esquema exportado |
| `core:network` | Retrofit, Moshi, OkHttp y DTOs |
| `core:designsystem` | Tema, colores y tipografía Compose |
| `domain` | Contrato del repositorio y casos de uso |
| `data` | Repositorio offline-first, mapeo y seed local |
| `feature:profile` | Estado, intents, efectos, ViewModel, navegación y pantallas |
| `docs` | Web estática, CV y endpoint `profile.json` |

Más detalle en [ARCHITECTURE.md](ARCHITECTURE.md) y [API_CONTRACT.md](API_CONTRACT.md).

## Versiones respetadas

El catálogo `gradle/libs.versions.toml` conserva las versiones solicitadas: AGP 9.1.1, Kotlin 2.3.20, KSP 2.3.7, Compose BOM 2026.04.01, Room 2.8.4, Koin 4.2.1, Retrofit 3.0.0, Moshi 1.15.2, OkHttp 4.12.0 y Coroutines 1.10.2, entre otras.

Se han añadido únicamente Coil 3 para la foto remota y ZXing Core para generar el QR localmente, sin depender de un servicio externo de códigos QR.

## Antes del evento

- Comprueba la web y el QR con un segundo teléfono usando datos móviles.
- Sustituye todos los textos entre corchetes y los enlaces de plantilla.
- Usa logros concretos y medibles, sin sobrecargar el perfil.
- Exporta `cv.html` a PDF desde el navegador si quieres ofrecer ambas opciones.
- Lleva la app ya abierta y el brillo suficiente para escanear el QR.
- Conserva también la URL corta en una nota o fondo de pantalla como respaldo.

