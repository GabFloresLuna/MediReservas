import test from "node:test";
import assert from "node:assert/strict";
import {
    getAppointmentById,
    getAppointments,
    getNextAppointmentId,
    initializeBaseAppointments,
    saveAppointment
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
        doctorId: 1,
        doctorName: "Ana Rojas",
        specialtyId: 1,
        specialtyName: "Cardiología",
        date: "2026-09-20",
        time: "10:00",
        reason: "Control médico",
        modality: "Presencial",
        status: "PENDIENTE"
    };

    saveAppointment(appointment);

    assert.equal(getAppointments().length, 7);
    assert.deepEqual(getAppointmentById("cita-7"), appointment);
});

test("genera un identificador correlativo para la siguiente cita", () => {
    assert.equal(getNextAppointmentId(), "cita-7");
});
