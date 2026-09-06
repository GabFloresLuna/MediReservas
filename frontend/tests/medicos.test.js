import test from "node:test";
import assert from "node:assert/strict";
import {getDoctors, initializeBaseDoctors} from "../assets/js/storage.js";

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
    initializeBaseDoctors();
});

test("cada médico se relaciona con una cuenta diferente", () => {
    const doctors = getDoctors();

    assert.equal(new Set(doctors.map(({userId}) => userId)).size, doctors.length);
    assert.ok(doctors.every(({userId}) => Number.isInteger(userId)));
});

test("usa specialtyIds según el contrato del backend", () => {
    const doctor = getDoctors()[0];

    assert.deepEqual(doctor.specialtyIds, [1, 5]);
    assert.equal("specialtyId" in doctor, false);
    assert.equal("extraSpecialtyIds" in doctor, false);
});

test("convierte la estructura antigua de especialidades médicas", () => {
    localStorage.setItem("medireservas_doctors", JSON.stringify([
        {doctorId: 2, userId: "doctor-1", specialtyId: 2, extraSpecialtyIds: [4]}
    ]));

    assert.deepEqual(getDoctors()[0].specialtyIds, [2, 4]);
    assert.equal(getDoctors()[0].userId, 5);
});
