# Contrato del contenido público

Aunque no hay un backend dinámico, `profile.json` funciona como un endpoint REST de solo lectura servido por GitHub Pages.

## Petición

```http
GET /digital-portfolio/profile.json HTTP/1.1
Host: TU_USUARIO.github.io
Accept: application/json
```

No requiere autenticación ni cabeceras privadas.

## Respuesta

```http
HTTP/1.1 200 OK
Content-Type: application/json
```

Estructura resumida:

```json
{
  "schemaVersion": 1,
  "updatedAt": "2026-09-07",
  "identity": {
    "fullName": "Jordi Developer",
    "initials": "JD",
    "headline": "Empresario · Senior Android & iOS Developer · Director de Operaciones",
    "location": "Rivas-Vaciamadrid · Madrid · España",
    "availability": "Disponible para colaboraciones",
    "photoUrl": "https://.../assets/profile.webp",
    "shortBio": "...",
    "elevatorPitch": "..."
  },
  "contact": {
    "email": "...",
    "phone": "...",
    "websiteUrl": "https://.../",
    "linkedInUrl": "https://...",
    "githubUrl": "https://...",
    "whatsAppUrl": "https://wa.me/...",
    "cvUrl": "https://.../cv.html"
  },
  "highlights": [],
  "services": [],
  "operations": [],
  "experience": [],
  "projects": [],
  "skills": [],
  "credentials": []
}
```

El fichero completo de ejemplo es `docs/profile.json`.

## Evolución

Mantén `schemaVersion` en `1` mientras los campos existentes conserven su significado. Para un cambio incompatible:

1. Incrementa `schemaVersion`.
2. Añade DTOs y mapeo compatibles en Android.
3. Publica la app capaz de leer ambas versiones.
4. Actualiza después el JSON web.

Así evitas que instalaciones antiguas reemplacen su caché válida por un documento que no entienden.

## Caché HTTP y Room

OkHttp puede reutilizar la respuesta durante una sesión según las cabeceras del hosting. Room aporta persistencia duradera entre cierres y reinicios. La interfaz observa Room y no depende directamente del tiempo de la llamada de red.

