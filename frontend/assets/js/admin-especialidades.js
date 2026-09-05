import {
    getSpecialties,
    getSpecialtyById,
    initializeBaseSpecialties,
    isSpecialtyNameTaken,
    saveSpecialty,
    updateSpecialty,
    getNextSpecialtyId
} from "./storage.js";
import {createTableCell} from "./ui-utils.js";

const dialog = document.querySelector("#specialty-dialog");
const form = document.querySelector("#specialty-form");
const tableBody = document.querySelector("#specialties-table-body");
const emptyMessage = document.querySelector("#specialties-empty-message");
const resultCount = document.querySelector("#specialties-result-count");
const formMessage = document.querySelector("#specialty-form-message");
const statusDialog = document.querySelector("#specialty-status-dialog");
const statusSpecialtyId = document.querySelector("#specialty-status-id");
const statusDialogTitle = document.querySelector("#specialty-status-dialog-title");
const statusDialogDescription = document.querySelector("#specialty-status-dialog-description");
const confirmStatusButton = document.querySelector("#confirm-specialty-status-button");

initializeBaseSpecialties();

function getInput(fieldName) {
    return form?.elements.namedItem(fieldName);
}

function getFormValues() {
    const formData = new FormData(form);

    return {
        id: Number(formData.get("specialtyId") ?? 0),
        specialtyName: String(formData.get("specialtyName") ?? "").trim(),
        description: String(formData.get("description") ?? "").trim(),
        active: getInput("active").checked
    };
}

function showFieldError(fieldName, error = "") {
    const input = getInput(fieldName);
    const errorElement = document.querySelector(`#specialty-${fieldName === "specialtyName" ? "name" : "description"}-error`);

    if (!input || !errorElement) return;
    input.setAttribute("aria-invalid", String(Boolean(error)));
    input.classList.toggle("border-red-500", Boolean(error));
    errorElement.textContent = error;
}

function validateSpecialty(values) {
    const errors = {};

    if (!values.specialtyName) errors.specialtyName = "El nombre de la especialidad es obligatorio.";
    else if (values.specialtyName.length > 60) errors.specialtyName = "El nombre no puede superar 60 caracteres.";

    if (values.description.length > 150) errors.description = "La descripción no puede superar 150 caracteres.";

    return errors;
}

function createStatusCell(isActive) {
    const cell = document.createElement("td");
    cell.className = "px-5 py-4";
    const badge = document.createElement("span");
    badge.className = isActive
        ? "inline-flex rounded-full bg-emerald-50 px-3 py-1 text-xs font-bold text-primary-dark"
        : "inline-flex rounded-full bg-red-50 px-3 py-1 text-xs font-bold text-red-700";
    badge.textContent = isActive ? "Activa" : "Inactiva";
    cell.append(badge);
    return cell;
}

function renderSpecialties() {
    const specialties = getSpecialties();
    const rows = specialties.map((specialty) => {
        const row = document.createElement("tr");
        row.className = "border-b border-line last:border-0";

        row.append(
            createTableCell(specialty.specialtyName, "px-5 py-4 font-semibold"),
            createTableCell(specialty.description || "Sin descripción"),
            createStatusCell(specialty.active)
        );

        const actionsCell = document.createElement("td");
        actionsCell.className = "px-5 py-4 text-right";
        const actions = document.createElement("div");
        actions.className = "flex flex-wrap justify-end gap-2";
        const editButton = document.createElement("button");
        editButton.className = "rounded-lg border border-line px-3 py-2 text-sm font-semibold text-primary-dark transition hover:bg-primary-light";
        editButton.type = "button";
        editButton.dataset.editSpecialty = specialty.id;
        editButton.textContent = "Editar";
        const statusButton = document.createElement("button");
        statusButton.className = specialty.active
            ? "rounded-lg border border-red-200 px-3 py-2 text-sm font-semibold text-red-700 transition hover:bg-red-50"
            : "rounded-lg border border-emerald-200 px-3 py-2 text-sm font-semibold text-primary-dark transition hover:bg-emerald-50";
        statusButton.type = "button";
        statusButton.dataset.changeStatus = specialty.id;
        statusButton.textContent = specialty.active ? "Desactivar" : "Activar";
        actions.append(editButton, statusButton);
        actionsCell.append(actions);
        row.append(actionsCell);
        return row;
    });

    tableBody.replaceChildren(...rows);
    resultCount.textContent = `${specialties.length} ${specialties.length === 1 ? "especialidad registrada" : "especialidades registradas"}`;
    emptyMessage.hidden = specialties.length > 0;
}

function openCreateDialog() {
    form.reset();
    getInput("specialtyId").value = "";
    getInput("active").checked = true;
    document.querySelector("#specialty-dialog-title").textContent = "Crear especialidad";
    showFieldError("specialtyName");
    showFieldError("description");
    formMessage.textContent = "";
    dialog.showModal();
}

function openEditDialog(specialtyId) {
    const specialty = getSpecialtyById(specialtyId);
    if (!specialty) return;

    form.reset();
    getInput("specialtyId").value = specialty.id;
    getInput("specialtyName").value = specialty.specialtyName ?? "";
    getInput("description").value = specialty.description ?? "";
    getInput("active").checked = Boolean(specialty.active);
    document.querySelector("#specialty-dialog-title").textContent = "Editar especialidad";
    showFieldError("specialtyName");
    showFieldError("description");
    formMessage.textContent = "";
    dialog.showModal();
}

function openStatusDialog(specialtyId) {
    const specialty = getSpecialtyById(specialtyId);
    if (!specialty) return;

    const nextActiveState = !specialty.active;
    const action = nextActiveState ? "activar" : "desactivar";

    statusSpecialtyId.value = specialty.id;
    confirmStatusButton.dataset.nextActive = String(nextActiveState);
    statusDialogTitle.textContent = `${nextActiveState ? "Activar" : "Desactivar"} especialidad`;
    statusDialogDescription.textContent = `¿Confirmas que deseas ${action} la especialidad ${specialty.specialtyName}?`;
    confirmStatusButton.textContent = nextActiveState ? "Activar especialidad" : "Desactivar especialidad";
    confirmStatusButton.className = nextActiveState
        ? "rounded-xl bg-primary px-5 py-3 font-semibold text-white transition hover:bg-primary-dark"
        : "rounded-xl bg-red-600 px-5 py-3 font-semibold text-white transition hover:bg-red-700";
    statusDialog.showModal();
}

document.querySelector("#new-specialty-button")?.addEventListener("click", openCreateDialog);
document.querySelector("#close-specialty-dialog")?.addEventListener("click", () => dialog.close());
document.querySelector("#cancel-specialty-button")?.addEventListener("click", () => dialog.close());
document.querySelector("#cancel-specialty-status-button")?.addEventListener("click", () => statusDialog.close());

tableBody?.addEventListener("click", (event) => {
    const editButton = event.target.closest("[data-edit-specialty]");
    if (editButton) openEditDialog(editButton.dataset.editSpecialty);

    const statusButton = event.target.closest("[data-change-status]");
    if (statusButton) openStatusDialog(statusButton.dataset.changeStatus);
});

confirmStatusButton?.addEventListener("click", () => {
    const specialtyId = statusSpecialtyId.value;
    const nextActiveState = confirmStatusButton.dataset.nextActive === "true";
    const updatedSpecialty = updateSpecialty(specialtyId, { active: nextActiveState });

    if (!updatedSpecialty) {
        statusDialogDescription.textContent = "No fue posible encontrar la especialidad seleccionada.";
        return;
    }

    statusDialog.close();
    renderSpecialties();
});

form?.addEventListener("submit", (event) => {
    event.preventDefault();
    const values = getFormValues();
    const isEditing = Boolean(values.id);
    const errors = validateSpecialty(values);

    showFieldError("specialtyName", errors.specialtyName);
    showFieldError("description", errors.description);
    formMessage.textContent = "";

    if (Object.keys(errors).length > 0) {
        getInput(errors.specialtyName ? "specialtyName" : "description")?.focus();
        return;
    }

    if (isSpecialtyNameTaken(values.specialtyName, values.id || null)) {
        formMessage.className = "mt-4 text-center text-sm font-medium text-red-600";
        formMessage.textContent = "Ya existe una especialidad con ese nombre.";
        return;
    }

    if (isEditing) {
        updateSpecialty(values.id, {
            specialtyName: values.specialtyName,
            description: values.description,
            active: values.active
        });
    } else {
        saveSpecialty({
            id: getNextSpecialtyId(),
            specialtyName: values.specialtyName,
            description: values.description,
            active: values.active
        });
    }

    dialog.close();
    renderSpecialties();
});

renderSpecialties();
