export function createTableCell(text, className = "px-5 py-4") {
    const cell = document.createElement("td");
    cell.className = className;
    cell.textContent = text;
    return cell;
}

export function createActiveStatusCell(isActive, labels = {}) {
    const cell = document.createElement("td");
    cell.className = "px-5 py-4";

    const badge = document.createElement("span");
    badge.className = isActive
        ? "inline-flex rounded-full bg-emerald-50 px-3 py-1 text-xs font-bold text-primary-dark"
        : "inline-flex rounded-full bg-red-50 px-3 py-1 text-xs font-bold text-red-700";
    badge.textContent = isActive
        ? labels.active ?? "Activo"
        : labels.inactive ?? "Inactivo";
    cell.append(badge);

    return cell;
}

export function appendLabeledText(container, label, value, className = "") {
    const paragraph = document.createElement("p");
    paragraph.className = className;

    const strong = document.createElement("strong");
    strong.textContent = `${label}: `;
    paragraph.append(strong, document.createTextNode(value));
    container.append(paragraph);

    return paragraph;
}

export function setFieldError(input, errorElement, error = "") {
    if (!input || !errorElement) return;

    input.setAttribute("aria-invalid", String(Boolean(error)));
    input.classList.toggle("border-red-500", Boolean(error));
    errorElement.textContent = error;
}
