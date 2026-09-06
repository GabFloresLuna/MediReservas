import test from "node:test";
import assert from "node:assert/strict";
import {
    getAvailableScheduleSlots,
    getScheduleSlots,
    initializeBaseScheduleSlots,
    reserveScheduleSlot
} from "../assets/js/schedule-storage.js";

class LocalStorageMock {
    #data = new Map();

    getItem(key) {
        return this.#data.has(key) ? this.#data.get(key) : null;
    }

    setItem(key, value) {
        this.#data.set(key, String(value));
    }

    clear() {
        this.#data.clear();
    }
}

globalThis.localStorage = new LocalStorageMock();

test.beforeEach(() => {
    localStorage.clear();
    initializeBaseScheduleSlots();
});

test("crea bloques disponibles con el contrato del backend", () => {
    const slot = getScheduleSlots()[0];

    assert.deepEqual(
        Object.keys(slot).sort(),
        ["appointmentId", "doctorId", "endTime", "scheduleSlotId", "slotDate", "slotStatus", "startTime"].sort()
    );
    assert.equal(slot.slotStatus, "DISPONIBLE");
});

test("filtra los bloques disponibles por médico", () => {
    const slots = getAvailableScheduleSlots(1);

    assert.ok(slots.length > 0);
    assert.ok(slots.every(({doctorId, slotStatus}) => doctorId === 1 && slotStatus === "DISPONIBLE"));
});

test("reserva un bloque una sola vez y lo relaciona con la cita", () => {
    const reservedSlot = reserveScheduleSlot(7, 15);

    assert.equal(reservedSlot.slotStatus, "RESERVADO");
    assert.equal(reservedSlot.appointmentId, 15);
    assert.equal(reserveScheduleSlot(7, 16), null);
    assert.equal(getAvailableScheduleSlots(1).some(({scheduleSlotId}) => scheduleSlotId === 7), false);
});
