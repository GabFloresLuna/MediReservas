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
        appointmentId: getNextAppointmentId(),
        patientUserId: 4,
        patientName: "Paula Contreras",
        patientRun: "44444444-4",
        doctorId: 1,
        doctorName: "Ana Rojas",
        specialtyId: 1,
        scheduleSlotId: 7,
        specialtyName: "Cardiología",
        date: "2026-09-20",
        time: "10:00",
        reason: "Control médico",
        modality: "Presencial",
        appointmentStatus: "PENDING"
    };

    saveAppointment(appointment);

    assert.equal(getAppointments().length, 7);
    assert.deepEqual(getAppointmentById(7), appointment);
});

test("genera un identificador correlativo para la siguiente cita", () => {
    assert.equal(getNextAppointmentId(), 7);
});

test("adapta estados antiguos guardados al contrato del backend", () => {
    localStorage.setItem("medireservas_appointments", JSON.stringify([
        {id: "cita-1", doctorId: "doctor-1", specialtyId: 1, status: "CONFIRMADA"},
        {id: "cita-2", doctorId: "doctor-1", specialtyId: 2, status: "REAGENDADA"}
    ]));

    assert.deepEqual(
        getAppointments().map(({appointmentStatus}) => appointmentStatus),
        ["CONFIRMED", "PENDING"]
    );
});

test("guarda la cancelación de una cita", () => {
    const cancelled = updateAppointment(1, {appointmentStatus: "CANCELLED"});

    assert.equal(cancelled.appointmentStatus, "CANCELLED");
    assert.equal(getAppointmentById(1).appointmentStatus, "CANCELLED");
});

test("marca una cita como completada sin mezclar datos clínicos", () => {
    updateAppointment(3, {
        appointmentStatus: "COMPLETED"
    });

    const completed = getAppointmentById(3);
    assert.equal(completed.appointmentStatus, "COMPLETED");
    assert.equal("diagnosis" in completed, false);
    assert.equal("clinicalNotes" in completed, false);
});
