import { getDashboardConfig } from "./roles.js";
import {
    getUsers,
    initializeBaseUsers,
    isUserDataTaken,
    saveUser,
    updateUser
} from "./storage.js";
import { normalizeRun, validateManagedUser } from "./validaciones.js";

const dialog = document.querySelector("#user-dialog");
const form = document.querySelector("#user-form");
const tableBody = document.querySelector("#users-table-body");
const emptyMessage = document.querySelector("#users-empty-message");
const resultCount = document.querySelector("#users-result-count");
const searchInput = document.querySelector("#user-search");
const statusFilter = document.querySelector("#status-filter");
const formMessage = document.querySelector("#user-form-message");
const fieldNames = ["run", "firstName", "lastName", "email", "phone", "address", "password"];

initializeBaseUsers();

function getInput(fieldName) {
    return form?.elements.namedItem(fieldName);
}

function getFormValues() {
    const formData = new FormData(form);

    return {
        id: String(formData.get("userId") ?? ""),
        run: normalizeRun(String(formData.get("run") ?? "").trim()),
        firstName: String(formData.get("firstName") ?? "").trim(),
        lastName: String(formData.get("lastName") ?? "").trim(),
        email: String(formData.get("email") ?? "").trim().toLowerCase(),
        phone: String(formData.get("phone") ?? "").trim(),
        address: String(formData.get("address") ?? "").trim(),
        password: String(formData.get("password") ?? "")
    };
}

function showFieldError(fieldName, error = "") {
    const input = getInput(fieldName);
    const errorElement = document.querySelector(`#user-${fieldName.replace(/[A-Z]/g, (letter) => `-${letter.toLowerCase()}`)}-error`);

    if (!input || !errorElement) return;
    input.setAttribute("aria-invalid", String(Boolean(error)));
    input.classList.toggle("border-red-500", Boolean(error));
    errorElement.textContent = error;
}

function getFilteredUsers() {
    const query = searchInput.value.trim().toLowerCase();
    const status = statusFilter.value;

    return getUsers().filter((user) => {
        const searchableText = `${user.firstName ?? ""} ${user.lastName ?? ""} ${user.run ?? ""} ${user.email ?? ""}`.toLowerCase();
        const matchesQuery = searchableText.includes(query);
        const matchesStatus = status === "all" || (status === "active" ? user.active : !user.active);
        return matchesQuery && matchesStatus;
    });
}

function createCell(text, className = "px-5 py-4") {
    const cell = document.createElement("td");
    cell.className = className;
    cell.textContent = text;
    return cell;
}

function renderUsers() {
    const users = getFilteredUsers();
    const rows = users.map((user) => {
        const row = document.createElement("tr");
        row.className = "border-b border-line last:border-0";
        const fullName = `${user.firstName ?? ""} ${user.lastName ?? ""}`.trim() || "Sin nombre";
        const role = getDashboardConfig(user.role)?.label ?? "Usuario";

        row.append(
            createCell(fullName, "px-5 py-4 font-semibold"),
            createCell(user.run ?? "Sin información"),
            createCell(user.email),
            createCell(role),
            createCell(user.active ? "Activo" : "Inactivo")
        );

        const actionsCell = document.createElement("td");
        actionsCell.className = "px-5 py-4 text-right";
        const editButton = document.createElement("button");
        editButton.className = "rounded-lg border border-line px-3 py-2 text-sm font-semibold text-primary-dark hover:bg-primary-light";
        editButton.type = "button";
        editButton.dataset.editUser = user.id;
        editButton.textContent = "Editar";
        actionsCell.append(editButton);
        row.append(actionsCell);
        return row;
    });

    tableBody.replaceChildren(...rows);
    resultCount.textContent = `${users.length} ${users.length === 1 ? "usuario encontrado" : "usuarios encontrados"}`;
    emptyMessage.hidden = users.length > 0;
}

function openCreateDialog() {
    form.reset();
    getInput("userId").value = "";
    document.querySelector("#user-dialog-title").textContent = "Crear usuario";
    fieldNames.forEach((fieldName) => showFieldError(fieldName));
    formMessage.textContent = "";
    dialog.showModal();
}

function openEditDialog(userId) {
    const user = getUsers().find((item) => item.id === userId);
    if (!user) return;

    form.reset();
    getInput("userId").value = user.id;
    fieldNames.filter((field) => field !== "password").forEach((field) => {
        getInput(field).value = user[field] ?? "";
        showFieldError(field);
    });
    showFieldError("password");
    document.querySelector("#user-dialog-title").textContent = "Editar usuario";
    formMessage.textContent = "";
    dialog.showModal();
}

function validateField(fieldName) {
    const values = getFormValues();
    const errors = validateManagedUser(values, Boolean(values.id));
    showFieldError(fieldName, errors[fieldName]);
}

fieldNames.forEach((fieldName) => {
    const input = getInput(fieldName);
    input?.addEventListener("blur", () => validateField(fieldName));
    input?.addEventListener("input", () => {
        if (input.getAttribute("aria-invalid") === "true") validateField(fieldName);
    });
});

document.querySelector("#new-user-button")?.addEventListener("click", openCreateDialog);
document.querySelector("#close-user-dialog")?.addEventListener("click", () => dialog.close());
document.querySelector("#cancel-user-button")?.addEventListener("click", () => dialog.close());
searchInput?.addEventListener("input", renderUsers);
statusFilter?.addEventListener("change", renderUsers);

tableBody?.addEventListener("click", (event) => {
    const editButton = event.target.closest("[data-edit-user]");
    if (editButton) openEditDialog(editButton.dataset.editUser);
});

getInput("run")?.addEventListener("blur", (event) => {
    event.target.value = normalizeRun(event.target.value);
});

form?.addEventListener("submit", (event) => {
    event.preventDefault();
    const values = getFormValues();
    const isEditing = Boolean(values.id);
    const errors = validateManagedUser(values, isEditing);

    fieldNames.forEach((fieldName) => showFieldError(fieldName, errors[fieldName]));
    formMessage.textContent = "";

    if (Object.keys(errors).length > 0) {
        getInput(Object.keys(errors)[0])?.focus();
        return;
    }

    if (isUserDataTaken(values.run, values.email, values.id || null)) {
        formMessage.className = "mt-4 text-center text-sm font-medium text-red-600";
        formMessage.textContent = "El RUN o correo ya está asociado a otra cuenta.";
        return;
    }

    if (isEditing) {
        const changes = { ...values };
        delete changes.id;
        if (!changes.password) delete changes.password;
        updateUser(values.id, changes);
    } else {
        saveUser({
            ...values,
            id: crypto.randomUUID?.() ?? `user-${Date.now()}`,
            role: "PACIENTE",
            active: true
        });
    }

    dialog.close();
    renderUsers();
});

renderUsers();
