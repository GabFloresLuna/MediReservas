import test from "node:test";
import assert from "node:assert/strict";
import {createTableCell} from "../assets/js/ui-utils.js";

test("crea una celda segura con texto y clases", () => {
    globalThis.document = {
        createElement(tagName) {
            return {tagName: tagName.toUpperCase(), className: "", textContent: ""};
        }
    };

    const cell = createTableCell("Paciente <script>", "cell-class");

    assert.equal(cell.tagName, "TD");
    assert.equal(cell.className, "cell-class");
    assert.equal(cell.textContent, "Paciente <script>");
});
