const USERS_KEY = "medireservas_users";

export function getUsers() {
    try {
        return JSON.parse(localStorage.getItem(USERS_KEY)) ?? [];
    } catch {
        return [];
    }
}

export function userExists(run, email) {
    const normalizedEmail = email.toLowerCase();

    return getUsers().some(
        (user) => user.run === run || user.email.toLowerCase() === normalizedEmail
    );
}

export function saveUser(user) {
    const users = getUsers();
    users.push(user);
    localStorage.setItem(USERS_KEY, JSON.stringify(users));
}
