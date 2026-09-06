import test from "node:test";
import assert from "node:assert/strict";
import {
    canCancelAppointment,
    formatAppointmentDate,
    getAppointmentStatusBadgeClass,
    getAppointmentStatusLabel,
    matchesAppointmentFilters
} from "../assets/js/citas-utils.js";

test("presenta todos los estados de cita conocidos", () => {
    assert.equal(getAppointmentStatusLabel("PENDING"), "Pendiente");
    assert.equal(getAppointmentStatusLabel("COMPLETED"), "Completada");
    assert.equal(getAppointmentStatusLabel("NO_SHOW"), "Inasistencia");
    assert.match(getAppointmentStatusBadgeClass("CANCELLED"), /red/);
});

test("formatea las fechas de citas para Chile sin cambiar el día", () => {
    assert.equal(formatAppointmentDate("2026-09-07"), "07-09-2026");
});

test("solo permite cancelar citas que todavía admiten cambios", () => {
    assert.equal(canCancelAppointment("PENDING"), true);
    assert.equal(canCancelAppointment("CONFIRMED"), true);
    assert.equal(canCancelAppointment("CANCELLED"), false);
    assert.equal(canCancelAppointment("COMPLETED"), false);
    assert.equal(canCancelAppointment("NO_SHOW"), false);
});

test("filtra citas por texto, estado y fecha", () => {
    const appointment = {
        patientName: "Camila Soto",
        patientRun: "19.234.567-8",
        doctorName: "Daniela Rojas",
        appointmentStatus: "PENDING",
        date: "2026-09-08"
    };

    assert.equal(matchesAppointmentFilters(appointment, {query: "camila", status: "PENDING", date: "2026-09-08"}), true);
    assert.equal(matchesAppointmentFilters(appointment, {date: "2026-09-09"}), false);
    assert.equal(matchesAppointmentFilters(appointment, {status: "CONFIRMED"}), false);
});
