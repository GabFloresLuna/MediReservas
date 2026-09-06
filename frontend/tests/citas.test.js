import test from "node:test";
import assert from "node:assert/strict";
import {
    getAppointmentById,
    getAppointments,
    getNextAppointmentId,
    initializeBaseAppointments,
    saveAppointment,
    updateAppointment
} from "../assets/js/storage.js";

class LocalStorageMock {
    #data = new Map();

    getItem(key) {
        return this.#data.has(key) ? this.#data.get(key) : null;
    }

    setItem(key, value) {
        this.#data.set(key, String(value));
    }

    removeItem(key) {
        this.#data.delete(key);
    }

    clear() {
        this.#data.clear();
    }
}

globalThis.localStorage = new LocalStorageMock();

test.beforeEach(() => {
    localStorage.clear();
    initializeBaseAppointments();
});

test("guarda una cita sin reemplazar las solicitudes existentes", () => {
    const appointment = {
        id: getNextAppointmentId(),
        patientId: "patient-1",
        patientName: "Paula Contreras",
        patientRun: "44444444-4",
        doctorId: "doctor-1",
        doctorRecordId: 1,
        doctorName: "Ana Rojas",
        specialtyId: 1,
        specialtyName: "Cardiología",
        date: "2026-09-20",
        time: "10:00",
        reason: "Control médico",
        modality: "Presencial",
        status: "PENDING"
    };

    saveAppointment(appointment);

    assert.equal(getAppointments().length, 7);
    assert.deepEqual(getAppointmentById("cita-7"), appointment);
});

test("genera un identificador correlativo para la siguiente cita", () => {
    assert.equal(getNextAppointmentId(), "cita-7");
});

test("adapta estados antiguos guardados al contrato del backend", () => {
    localStorage.setItem("medireservas_appointments", JSON.stringify([
        {id: "legacy-1", status: "CONFIRMADA"},
        {id: "legacy-2", status: "REAGENDADA"}
    ]));

    assert.deepEqual(
        getAppointments().map(({status}) => status),
        ["CONFIRMED", "PENDING"]
    );
});

test("guarda la cancelación de una cita", () => {
    const cancelled = updateAppointment("cita-1", {status: "CANCELLED"});

    assert.equal(cancelled.status, "CANCELLED");
    assert.equal(getAppointmentById("cita-1").status, "CANCELLED");
});

test("guarda la observación clínica y completa la cita", () => {
    updateAppointment("cita-3", {
        diagnosis: "Dolor lumbar",
        clinicalNotes: "Se indica reposo y control médico.",
        completedAt: "2026-09-05T15:00:00.000Z",
        status: "COMPLETED"
    });

    const completed = getAppointmentById("cita-3");
    assert.equal(completed.status, "COMPLETED");
    assert.equal(completed.diagnosis, "Dolor lumbar");
    assert.equal(completed.clinicalNotes, "Se indica reposo y control médico.");
});
