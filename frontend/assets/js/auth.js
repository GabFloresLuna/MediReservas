import { getUsers, removeSession, saveSession } from "./storage.js";

const ROLE_DESTINATIONS = {
    ADMINISTRADOR: "dashboard.html",
    RECEPCIONISTA: "dashboard.html",
    MEDICO: "dashboard.html",
    PACIENTE: "dashboard.html"
};

export function authenticate(email, password) {
    const normalizedEmail = email.trim().toLowerCase();

    return getUsers().find(
        (user) =>
            user.email.toLowerCase() === normalizedEmail &&
            user.password === password &&
            user.active
    );
}

export function createSession(user) {
    const session = {
        token: crypto.randomUUID?.() ?? `session-${Date.now()}`,
        userId: user.id,
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        role: user.role
    };

    saveSession(session);
    return session;
}

export function getRoleDestination(role) {
    return ROLE_DESTINATIONS[role] ?? "login.html";
}

export function isSessionValid(session, user) {
    return Boolean(
        typeof session?.token === "string" &&
        session.token.trim() &&
        user?.active === true &&
        session.userId === user.id &&
        session.role === user.role
    );
}

export function logout() {
    removeSession();
}
