import { getDashboardConfig } from "./roles.js";
import { getSession, getUserById } from "./storage.js";

const session = getSession();
const user = session ? getUserById(session.userId) : null;

function setText(selector, value, fallback = "Sin información") {
    const element = document.querySelector(selector);
    if (element) element.textContent = value || fallback;
}

function getInitials(firstName, lastName) {
    return `${firstName?.[0] ?? ""}${lastName?.[0] ?? ""}`.toUpperCase() || "MR";
}

function formatDate(dateValue) {
    if (!dateValue) return "Sin información";

    return new Intl.DateTimeFormat("es-CL", {
        day: "2-digit",
        month: "long",
        year: "numeric",
        timeZone: "UTC"
    }).format(new Date(`${dateValue}T00:00:00Z`));
}

function renderProfile() {
    if (!session || !user) return;

    const role = getDashboardConfig(session.role)?.label ?? "Usuario";
    const fullName = `${user.firstName ?? ""} ${user.lastName ?? ""}`.trim();

    document.title = `${fullName || "Mi perfil"} | MediReservas`;
    setText("#profile-initials", getInitials(user.firstName, user.lastName));
    setText("#profile-name", fullName);
    setText("#profile-role", role);
    setText("#profile-run", user.run);
    setText("#profile-email", user.email);
    setText("#profile-phone", user.phone);
    setText("#profile-birth-date", formatDate(user.birthDate));
    setText("#profile-address", user.address);
    setText("#profile-status", user.active ? "Activa" : "Inactiva");
}

renderProfile();
