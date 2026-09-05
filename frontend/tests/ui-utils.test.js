import test from "node:test";
import assert from "node:assert/strict";
import {appendLabeledText, createTableCell} from "../assets/js/ui-utils.js";

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

test("agrega texto etiquetado sin interpretar HTML", () => {
    const children = [];
    globalThis.document = {
        createElement(tagName) {
            return {
                tagName: tagName.toUpperCase(),
                className: "",
                textContent: "",
                append(...items) {
                    this.children = items;
                }
            };
        },
        createTextNode(textContent) {
            return {textContent};
        }
    };
    const container = {append(item) { children.push(item); }};

    appendLabeledText(container, "Médico", "Ana <script>", "mt-2");

    assert.equal(children[0].className, "mt-2");
    assert.equal(children[0].children[0].textContent, "Médico: ");
    assert.equal(children[0].children[1].textContent, "Ana <script>");
});
