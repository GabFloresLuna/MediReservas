import test from "node:test";
import assert from "node:assert/strict";
import { readFileSync } from "node:fs";

const pagePaths = [
    "../index.html",
    "../pages/acceso-denegado.html",
    "../pages/dashboard.html",
    "../pages/login.html",
    "../pages/perfil.html",
    "../pages/registro.html",
    "../pages/roles.html",
    "../pages/usuarios.html",
];

const pages = pagePaths.map((path) => ({
    path,
    html: readFileSync(new URL(path, import.meta.url), "utf8"),
}));

test("todas las vistas declaran un nombre de página", () => {
    const names = pages.map(({ path, html }) => {
        const match = html.match(/data-page-name="([^"]+)"/);
        assert.ok(match, `${path} no declara data-page-name`);
        return match[1];
    });

    assert.equal(new Set(names).size, pages.length);
});

test("todas las vistas permiten saltar al contenido principal", () => {
    pages.forEach(({ path, html }) => {
        assert.match(html, /href="#main-content"/, `${path} no tiene enlace de salto`);
        assert.match(html, /id="main-content"/, `${path} no tiene destino principal`);
        assert.match(html, /id="main-content"[^>]*tabindex="-1"/, `${path} no permite enfocar el destino`);
    });
});

test("cada vista contiene un único destino principal", () => {
    pages.forEach(({ path, html }) => {
        const destinations = html.match(/id="main-content"/g) ?? [];
        assert.equal(destinations.length, 1, `${path} debe tener un solo main-content`);
    });
});
