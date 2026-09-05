export function createTableCell(text, className = "px-5 py-4") {
    const cell = document.createElement("td");
    cell.className = className;
    cell.textContent = text;
    return cell;
}
