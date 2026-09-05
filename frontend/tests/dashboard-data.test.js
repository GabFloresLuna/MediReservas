import test from "node:test";
import assert from "node:assert/strict";
import {getDashboardSummary} from "../assets/js/dashboard-data.js";

const appointments = [
    {id: "1", patientId: "patient-1", doctorId: "doctor-1", date: "2026-09-05", status: "PENDIENTE"},
    {id: "2", patientId: "patient-1", doctorId: "doctor-1", date: "2026-09-05", status: "CONFIRMADA"},
    {id: "3", patientId: "patient-1", doctorId: "doctor-1", date: "2026-09-04", status: "COMPLETADA"}
];

test("calcula el resumen del paciente desde sus citas", () => {
    const summary = getDashboardSummary({
        session: {role: "PACIENTE", userId: "patient-1"},
        appointments,
        today: "2026-09-05"
    });

    assert.deepEqual(summary.map((item) => item.value), ["2", "1", "1"]);
});

test("calcula el resumen del médico desde su agenda", () => {
    const summary = getDashboardSummary({
        session: {role: "MEDICO", userId: "doctor-1"},
        appointments,
        today: "2026-09-05"
    });

    assert.deepEqual(summary.map((item) => item.value), ["2", "1", "1"]);
});

test("calcula el resumen administrativo desde usuarios y especialidades", () => {
    const summary = getDashboardSummary({
        session: {role: "ADMINISTRADOR", userId: "admin-1"},
        users: [{role: "ADMINISTRADOR", active: true}, {role: "PACIENTE", active: false}],
        specialties: [{active: true}, {active: false}],
        today: "2026-09-05"
    });

    assert.deepEqual(summary.map((item) => item.value), ["2", "1", "1"]);
});
