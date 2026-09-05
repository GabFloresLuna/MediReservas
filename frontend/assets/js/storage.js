import {BASE_APPOINTMENTS, BASE_DOCTORS, BASE_SPECIALTIES, BASE_USERS} from "./mock-data.js";

const USERS_KEY = "medireservas_users";
const SESSION_KEY = "medireservas_session";

export function getUsers() {
    try {
        return JSON.parse(localStorage.getItem(USERS_KEY)) ?? [];
    } catch {
        return [];
    }
}

export function userExists(run, email) {
    return isUserDataTaken(run, email);
}

export function getUserById(userId) {
    return getUsers().find((user) => user.id === userId) ?? null;
}

export function saveUser(user) {
    const users = getUsers();
    users.push(user);
    localStorage.setItem(USERS_KEY, JSON.stringify(users));
}

export function isUserDataTaken(run, email, excludedUserId = null) {
    const normalizedEmail = email.toLowerCase();

    return getUsers().some(
        (user) =>
            user.id !== excludedUserId &&
            (user.run === run || user.email.toLowerCase() === normalizedEmail)
    );
}

export function updateUser(userId, changes) {
    const users = getUsers();
    const userIndex = users.findIndex((user) => user.id === userId);

    if (userIndex < 0) return null;

    users[userIndex] = {...users[userIndex], ...changes, id: userId};
    localStorage.setItem(USERS_KEY, JSON.stringify(users));
    return users[userIndex];
}

export function updateUserStatus(userId, active) {
    if (typeof active !== "boolean") return null;
    return updateUser(userId, {active});
}

export function initializeBaseUsers() {
    const users = getUsers();
    const existingEmails = new Set(users.map((user) => user.email.toLowerCase()));
    const missingUsers = BASE_USERS.filter(
        (user) => !existingEmails.has(user.email.toLowerCase())
    );

    if (missingUsers.length > 0) {
        localStorage.setItem(USERS_KEY, JSON.stringify([...users, ...missingUsers]));
    }
}

export function saveSession(session) {
    localStorage.setItem(SESSION_KEY, JSON.stringify(session));
}

export function getSession() {
    try {
        return JSON.parse(localStorage.getItem(SESSION_KEY));
    } catch {
        return null;
    }
}

export function removeSession() {
    localStorage.removeItem(SESSION_KEY);
}

const DOCTORS_KEY = "medireservas_doctors";
const SPECIALTIES_KEY = "medireservas_specialties";
const APPOINTMENTS_KEY = "medireservas_appointments";

export function getSpecialties() {
    try {
        return JSON.parse(localStorage.getItem(SPECIALTIES_KEY)) ?? [];
    } catch {
        return [];
    }
}

export function getSpecialtyById(specialtyId) {
    return getSpecialties().find((specialty) => specialty.id === Number(specialtyId)) ?? null;
}

export function saveSpecialty(specialty) {
    const specialties = getSpecialties();
    specialties.push(specialty);
    localStorage.setItem(SPECIALTIES_KEY, JSON.stringify(specialties));
}

export function updateSpecialty(specialtyId, changes) {
    const specialties = getSpecialties();
    const specialtyIndex = specialties.findIndex((specialty) => specialty.id === Number(specialtyId));

    if (specialtyIndex < 0) return null;

    specialties[specialtyIndex] = {...specialties[specialtyIndex], ...changes, id: Number(specialtyId)};
    localStorage.setItem(SPECIALTIES_KEY, JSON.stringify(specialties));
    return specialties[specialtyIndex];
}

export function isSpecialtyNameTaken(specialtyName, excludedSpecialtyId = null) {
    const normalizedName = specialtyName.trim().toLowerCase();

    return getSpecialties().some(
        (specialty) =>
            specialty.id !== excludedSpecialtyId &&
            specialty.specialtyName.trim().toLowerCase() === normalizedName
    );
}

export function getNextSpecialtyId() {
    return getSpecialties().reduce((maxId, specialty) => Math.max(maxId, specialty.id), 0) + 1;
}

export function initializeBaseSpecialties() {
    if (localStorage.getItem(SPECIALTIES_KEY)) return;

    localStorage.setItem(SPECIALTIES_KEY, JSON.stringify(BASE_SPECIALTIES));
}

export function getDoctors() {
    try {
        return JSON.parse(localStorage.getItem(DOCTORS_KEY)) ?? [];
    } catch {
        return [];
    }
}

export function getDoctorById(doctorId) {
    return getDoctors().find((doctor) => doctor.doctorId === Number(doctorId)) ?? null;
}

export function saveDoctor(doctor) {
    const doctors = getDoctors();
    doctors.push(doctor);
    localStorage.setItem(DOCTORS_KEY, JSON.stringify(doctors));
}

export function updateDoctor(doctorId, changes) {
    const doctors = getDoctors();
    const doctorIndex = doctors.findIndex((doctor) => doctor.doctorId === Number(doctorId));

    if (doctorIndex < 0) return null;

    doctors[doctorIndex] = {...doctors[doctorIndex], ...changes, doctorId: Number(doctorId)};
    localStorage.setItem(DOCTORS_KEY, JSON.stringify(doctors));
    return doctors[doctorIndex];
}

export function isDoctorDataTaken(run, medicalLicenseNumber, excludedDoctorId = null) {
    const normalizedRun = run.trim().toLowerCase();
    const normalizedLicense = medicalLicenseNumber.trim().toLowerCase();

    return getDoctors().some(
        (doctor) =>
            doctor.doctorId !== excludedDoctorId &&
            (doctor.run.trim().toLowerCase() === normalizedRun ||
                doctor.medicalLicenseNumber.trim().toLowerCase() === normalizedLicense)
    );
}

export function getNextDoctorId() {
    return getDoctors().reduce((maxId, doctor) => Math.max(maxId, doctor.doctorId), 0) + 1;
}

export function initializeBaseDoctors() {
    if (localStorage.getItem(DOCTORS_KEY)) return;

    localStorage.setItem(DOCTORS_KEY, JSON.stringify(BASE_DOCTORS));
}

export function getAppointments() {
    try {
        return JSON.parse(localStorage.getItem(APPOINTMENTS_KEY)) ?? [];
    } catch {
        return [];
    }
}

export function getAppointmentById(appointmentId) {
    return getAppointments().find((appointment) => appointment.id === appointmentId) ?? null;
}

export function saveAppointment(appointment) {
    const appointments = getAppointments();
    appointments.push(appointment);
    localStorage.setItem(APPOINTMENTS_KEY, JSON.stringify(appointments));
}

export function getNextAppointmentId() {
    const highestId = getAppointments().reduce((maxId, appointment) => {
        const numericId = Number.parseInt(String(appointment.id).replace("cita-", ""), 10);
        return Number.isNaN(numericId) ? maxId : Math.max(maxId, numericId);
    }, 0);

    return `cita-${highestId + 1}`;
}

export function updateAppointment(appointmentId, changes) {
    const appointments = getAppointments();
    const appointmentIndex = appointments.findIndex((appointment) => appointment.id === appointmentId);

    if (appointmentIndex < 0) return null;

    appointments[appointmentIndex] = {...appointments[appointmentIndex], ...changes, id: appointmentId};
    localStorage.setItem(APPOINTMENTS_KEY, JSON.stringify(appointments));
    return appointments[appointmentIndex];
}

export function initializeBaseAppointments() {
    if (localStorage.getItem(APPOINTMENTS_KEY)) return;

    localStorage.setItem(APPOINTMENTS_KEY, JSON.stringify(BASE_APPOINTMENTS));
}
