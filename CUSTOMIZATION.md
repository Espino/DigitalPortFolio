# Guía de personalización

Toda la información profesional vive en `docs/profile.json`. Después de modificarla, ejecuta:

```bash
./gradlew syncProfileContent
```

Así la misma versión pasa a `data/src/main/assets/profile.json` y queda disponible como respaldo offline en una instalación nueva.

## Campos que debes sustituir

| Ruta JSON | Qué poner |
|---|---|
| `identity.fullName` | Nombre y apellidos profesionales |
| `identity.initials` | Dos iniciales para el fallback de la foto |
| `identity.headline` | Posicionamiento en una sola frase |
| `identity.location` | Ciudad o zona de trabajo |
| `identity.availability` | Tipo de oportunidades que buscas |
| `identity.photoUrl` | URL HTTPS completa de tu foto publicada |
| `identity.shortBio` | Presentación breve orientada a valor |
| `identity.elevatorPitch` | Pitch de 20–30 segundos |
| `contact.*` | Correo, teléfono, web y perfiles públicos |
| `highlights` | Tres datos que se entiendan de un vistazo |
| `services` | Servicios concretos que puedes ofrecer |
| `operations` | Cómo trabajas desde idea hasta mejora continua |
| `experience` | Empresas, cargos, periodos y resultados reales |
| `projects` | Casos relevantes, rol, tecnologías y enlace |
| `skills` | Selección breve y estratégica de competencias |
| `credentials` | Formación, premios y certificaciones verificables |
| `updatedAt` | Fecha ISO `AAAA-MM-DD` de la última actualización |

## Fotografía

Recomendaciones:

- Formato WebP o JPEG.
- Encuadre cuadrado, fondo limpio y aspecto profesional.
- Entre 600 × 600 y 1200 × 1200 píxeles.
- Peso inferior a 300 KB cuando sea posible.
- Nombre estable, por ejemplo `profile.webp`, para no cambiar el JSON cada vez.

Tras subir `docs/assets/profile.webp`, su URL será similar a:

```text
https://tuusuario.github.io/digital-portfolio/assets/profile.webp
```

Si `photoUrl` está vacío o no se puede descargar, la app y la web muestran las iniciales.

## Un pitch profesional que funciona

Una estructura útil es:

```text
Ayudo a [tipo de cliente] a [resultado] mediante [especialidad].
Aporto [diferenciador] para llevar [situación inicial] hasta [resultado final].
```

Ejemplo incluido:

> Ayudo a empresas y emprendedores a convertir una necesidad real en una app útil y mantenible. Aporto visión de producto, desarrollo móvil senior y dirección operativa para llevar cada iniciativa desde la idea hasta una solución que pueda crecer.

Adáptalo a tus resultados reales y a la audiencia del evento.

## URL y QR

El QR se genera en el dispositivo a partir de `contact.websiteUrl`. Mantén esa URL corta, permanente y con HTTPS. Si conectas un dominio propio, cambia:

- `contact.websiteUrl`
- `contact.cvUrl`
- `PORTFOLIO_BASE_URL` en `gradle.properties`
- Los enlaces de proyectos que apunten al dominio anterior

Después ejecuta `syncProfileContent` y recompila.

## Privacidad

Publica solo datos que entregarías en una tarjeta profesional. Si no quieres exponer tu teléfono personal, usa un número profesional o deja vacío el campo y elimina el botón correspondiente en `ContactScreen.kt`.

Nunca incluyas tokens, contraseñas, claves API, direcciones privadas, documentos de identidad ni datos de clientes sujetos a confidencialidad.

