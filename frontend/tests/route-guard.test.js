import test from "node:test";
import assert from "node:assert/strict";
import { evaluateRouteAccess } from "../assets/js/route-guard.js";

const allRoles = ["ADMINISTRADOR", "RECEPCIONISTA", "MEDICO", "PACIENTE"];

test("permite ingresar a una ruta pública sin sesión", () => {
    assert.equal(
        evaluateRouteAccess({ authRequired: false, allowedRoles: [], session: null }),
        "allowed"
    );
});

test("envía al login cuando falta una sesión válida", () => {
    assert.equal(
        evaluateRouteAccess({ authRequired: true, allowedRoles: allRoles, session: null }),
        "login"
    );
    assert.equal(
        evaluateRouteAccess({
            authRequired: true,
            allowedRoles: allRoles,
            session: { role: "ROL_DESCONOCIDO" },
        }),
        "login"
    );
});

test("permite a cada rol entrar a las vistas compartidas", () => {
    allRoles.forEach((role) => {
        assert.equal(
            evaluateRouteAccess({ authRequired: true, allowedRoles: allRoles, session: { role } }),
            "allowed"
        );
    });
});

test("deniega una vista administrativa a roles no autorizados", () => {
    ["RECEPCIONISTA", "MEDICO", "PACIENTE"].forEach((role) => {
        assert.equal(
            evaluateRouteAccess({
                authRequired: true,
                allowedRoles: ["ADMINISTRADOR"],
                session: { role },
            }),
            "denied"
        );
    });
});
