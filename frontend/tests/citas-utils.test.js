import test from "node:test";
import assert from "node:assert/strict";
import {
    canCancelAppointment,
    getAppointmentStatusBadgeClass,
    getAppointmentStatusLabel
} from "../assets/js/citas-utils.js";

test("presenta todos los estados de cita conocidos", () => {
    assert.equal(getAppointmentStatusLabel("PENDIENTE"), "Pendiente");
    assert.equal(getAppointmentStatusLabel("COMPLETADA"), "Completada");
    assert.match(getAppointmentStatusBadgeClass("CANCELADA"), /red/);
});

test("solo permite cancelar citas que todavía admiten cambios", () => {
    assert.equal(canCancelAppointment("PENDIENTE"), true);
    assert.equal(canCancelAppointment("CONFIRMADA"), true);
    assert.equal(canCancelAppointment("REAGENDADA"), true);
    assert.equal(canCancelAppointment("CANCELADA"), false);
    assert.equal(canCancelAppointment("COMPLETADA"), false);
});
