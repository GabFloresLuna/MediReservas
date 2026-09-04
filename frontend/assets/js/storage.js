const USERS_KEY = "medireservas_users";
const SESSION_KEY = "medireservas_session";

const BASE_USERS = [
    {
        id: "admin-1",
        run: "11111111-1",
        firstName: "Andrea",
        lastName: "Muñoz",
        email: "administrador@medireservas.cl",
        password: "Admin123",
        role: "ADMINISTRADOR",
        active: true
    },
    {
        id: "receptionist-1",
        run: "22222222-2",
        firstName: "Ricardo",
        lastName: "Silva",
        email: "recepcion@medireservas.cl",
        password: "Recepcion123",
        role: "RECEPCIONISTA",
        active: true
    },
    {
        id: "doctor-1",
        run: "33333333-3",
        firstName: "Daniela",
        lastName: "Rojas",
        email: "medico@medireservas.cl",
        password: "Medico123",
        role: "MEDICO",
        active: true
    },
    {
        id: "patient-1",
        run: "44444444-4",
        firstName: "Paula",
        lastName: "Contreras",
        email: "paciente@medireservas.cl",
        password: "Paciente123",
        role: "PACIENTE",
        active: true
    }
];

export function getUsers() {
    try {
        return JSON.parse(localStorage.getItem(USERS_KEY)) ?? [];
    } catch {
        return [];
    }
}

export function userExists(run, email) {
    return isUserDataTaken(run, email);
}

export function getUserById(userId) {
    return getUsers().find((user) => user.id === userId) ?? null;
}

export function saveUser(user) {
    const users = getUsers();
    users.push(user);
    localStorage.setItem(USERS_KEY, JSON.stringify(users));
}

export function isUserDataTaken(run, email, excludedUserId = null) {
    const normalizedEmail = email.toLowerCase();

    return getUsers().some(
        (user) =>
            user.id !== excludedUserId &&
            (user.run === run || user.email.toLowerCase() === normalizedEmail)
    );
}

export function updateUser(userId, changes) {
    const users = getUsers();
    const userIndex = users.findIndex((user) => user.id === userId);

    if (userIndex < 0) return null;

    users[userIndex] = { ...users[userIndex], ...changes, id: userId };
    localStorage.setItem(USERS_KEY, JSON.stringify(users));
    return users[userIndex];
}

export function updateUserStatus(userId, active) {
    if (typeof active !== "boolean") return null;
    return updateUser(userId, { active });
}

export function initializeBaseUsers() {
    const users = getUsers();
    const existingEmails = new Set(users.map((user) => user.email.toLowerCase()));
    const missingUsers = BASE_USERS.filter(
        (user) => !existingEmails.has(user.email.toLowerCase())
    );

    if (missingUsers.length > 0) {
        localStorage.setItem(USERS_KEY, JSON.stringify([...users, ...missingUsers]));
    }
}

export function saveSession(session) {
    localStorage.setItem(SESSION_KEY, JSON.stringify(session));
}

export function getSession() {
    try {
        return JSON.parse(localStorage.getItem(SESSION_KEY));
    } catch {
        return null;
    }
}

export function removeSession() {
    localStorage.removeItem(SESSION_KEY);
}
