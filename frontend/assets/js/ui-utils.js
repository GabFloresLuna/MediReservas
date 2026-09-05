export function createTableCell(text, className = "px-5 py-4") {
    const cell = document.createElement("td");
    cell.className = className;
    cell.textContent = text;
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
