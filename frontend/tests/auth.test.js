import test from "node:test";
import assert from "node:assert/strict";
import { authenticate, createSession, getRoleDestination } from "../assets/js/auth.js";
import {
    getSession,
    getUsers,
    initializeBaseUsers,
    saveUser
} from "../assets/js/storage.js";
import { validateLogin } from "../assets/js/validaciones.js";

class LocalStorageMock {
    #data = new Map();

    getItem(key) {
        return this.#data.has(key) ? this.#data.get(key) : null;
    }

    setItem(key, value) {
        this.#data.set(key, String(value));
    }

    clear() {
        this.#data.clear();
    }
}

globalThis.localStorage = new LocalStorageMock();

test.beforeEach(() => {
    localStorage.clear();
    initializeBaseUsers();
});

test("inicializa una sola cuenta por cada rol", () => {
    initializeBaseUsers();

    assert.equal(getUsers().length, 4);
    assert.deepEqual(
        new Set(getUsers().map((user) => user.role)),
        new Set(["ADMINISTRADOR", "RECEPCIONISTA", "MEDICO", "PACIENTE"])
    );
});

test("autentica credenciales válidas sin distinguir mayúsculas del correo", () => {
    const user = authenticate("ADMINISTRADOR@MEDIRESERVAS.CL", "Admin123");

    assert.equal(user?.role, "ADMINISTRADOR");
});

test("rechaza una contraseña incorrecta y una cuenta inactiva", () => {
    assert.equal(authenticate("medico@medireservas.cl", "incorrecta"), undefined);

    saveUser({
        id: "inactive-1",
        email: "inactivo@medireservas.cl",
        password: "Inactivo123",
        role: "PACIENTE",
        active: false
    });

    assert.equal(authenticate("inactivo@medireservas.cl", "Inactivo123"), undefined);
});

test("crea una sesión sin incluir la contraseña", () => {
    const user = authenticate("paciente@medireservas.cl", "Paciente123");
    const session = createSession(user);

    assert.equal(session.role, "PACIENTE");
    assert.equal(getSession().email, "paciente@medireservas.cl");
    assert.equal("password" in getSession(), false);
});

test("dirige los roles reconocidos al dashboard compartido", () => {
    assert.equal(getRoleDestination("ADMINISTRADOR"), "dashboard.html");
    assert.equal(getRoleDestination("RECEPCIONISTA"), "dashboard.html");
    assert.equal(getRoleDestination("MEDICO"), "dashboard.html");
    assert.equal(getRoleDestination("PACIENTE"), "dashboard.html");
    assert.equal(getRoleDestination("ROL_DESCONOCIDO"), "login.html");
});

test("valida los campos del formulario antes de autenticar", () => {
    assert.deepEqual(validateLogin({ email: "paciente@medireservas.cl", password: "Paciente123" }), {});

    const errors = validateLogin({ email: "correo-invalido", password: "123" });
    assert.ok(errors.email);
    assert.ok(errors.password);
});
