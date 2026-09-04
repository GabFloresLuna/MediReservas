import test from "node:test";
import assert from "node:assert/strict";
import { getNavigationItems } from "../assets/js/navigation.js";

test("cada rol recibe accesos al panel y al perfil", () => {
    ["ADMINISTRADOR", "RECEPCIONISTA", "MEDICO", "PACIENTE"].forEach((role) => {
        const items = getNavigationItems(role);

        assert.equal(items[0].href, "dashboard.html");
        assert.equal(items[1].href, "perfil.html");
        assert.ok(items.length >= 5);
    });
});

test("la navegación no expone enlaces para roles desconocidos", () => {
    assert.deepEqual(getNavigationItems("ROL_DESCONOCIDO"), []);
    assert.deepEqual(getNavigationItems(), []);
});

test("los enlaces internos usan rutas HTML válidas", () => {
    getNavigationItems("ADMINISTRADOR").forEach((item) => {
        assert.match(item.href, /^[a-z0-9-]+\.html(?:\?[a-z0-9=&-]+)?$/);
    });
});
