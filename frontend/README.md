# MediReservas Frontend

Frontend web desarrollado con HTML, Tailwind CSS y JavaScript modular.

## Autenticación actual

Durante la etapa sin conexión al backend, las cuentas y la sesión se almacenan en `localStorage`. Al iniciar sesión se genera un identificador local en la propiedad `token`; este valor permite representar el flujo de autenticación, pero no es un JWT ni debe considerarse seguro para producción.

Las vistas protegidas utilizan `route-guard.js` para comprobar:

- que exista una sesión con token;
- que la cuenta todavía exista y esté activa;
- que el rol de la sesión coincida con el rol actual del usuario;
- que el rol tenga permiso para acceder a la ruta solicitada.

Los permisos de rutas se mantienen centralizados en `roles.js`. Si la sesión no es válida, el usuario vuelve al login. Si está autenticado pero no tiene permiso, se muestra la vista de acceso restringido.

## Integración futura con JWT

Al conectar Auth Service, el token local será reemplazado por el JWT entregado por el backend. El frontend deberá enviar ese token mediante el encabezado `Authorization: Bearer <token>` y tratar las respuestas `401` como sesión inválida y las respuestas `403` como acceso no autorizado.

La autorización definitiva siempre debe realizarse en el backend. Ocultar enlaces y proteger rutas en el frontend mejora la navegación, pero no sustituye la validación del servidor.
