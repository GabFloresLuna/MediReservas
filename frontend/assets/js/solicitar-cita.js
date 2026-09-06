import {
    getDoctors,
    getNextAppointmentId,
    getSession,
    getSpecialties,
    getUserById,
    initializeBaseAppointments,
    initializeBaseDoctors,
    initializeBaseSpecialties,
    saveAppointment
} from "./storage.js";
import { getLocalDateString } from "./validaciones.js";

const form = document.querySelector("#formularioCita");
const successMessage = document.querySelector("#mensajeSolicitud");
const specialtySelect = document.querySelector("#especialidad");
const doctorSelect = document.querySelector("#medico");
const dateInput = document.querySelector("#fecha");

const errorElements = {
    especialidad: document.querySelector("#errorEspecialidad"),
    medico: document.querySelector("#errorMedico"),
    fecha: document.querySelector("#errorFecha"),
    hora: document.querySelector("#errorHora"),
    motivo: document.querySelector("#errorMotivo"),
    modalidad: document.querySelector("#errorModalidad")
};

function resetSelect(select, placeholder) {
    select.replaceChildren(new Option(placeholder, ""));
}

function fillSpecialties() {
    resetSelect(specialtySelect, "Seleccione una especialidad");

    getSpecialties()
        .filter((specialty) => specialty.active)
        .forEach((specialty) => {
            specialtySelect.add(new Option(specialty.specialtyName, specialty.specialtyId));
        });
}

function fillDoctors() {
    const specialtyId = Number(specialtySelect.value);
    resetSelect(doctorSelect, "Seleccione un médico");

    if (!specialtyId) return;

    getDoctors()
        .filter(
            (doctor) =>
                doctor.active &&
                doctor.specialtyIds.includes(specialtyId)
        )
        .forEach((doctor) => {
            doctorSelect.add(new Option(`${doctor.firstName} ${doctor.lastName}`, doctor.doctorId));
        });
}

function clearErrors() {
    Object.values(errorElements).forEach((element) => {
        element.textContent = "";
    });
}

function getFormValues() {
    const data = new FormData(form);

    return {
        specialtyId: Number(data.get("especialidad")),
        doctorId: Number(data.get("medico")),
        date: String(data.get("fecha") ?? ""),
        time: String(data.get("hora") ?? ""),
        reason: String(data.get("motivo") ?? "").trim(),
        modality: String(data.get("modalidad") ?? "")
    };
}

function validateAppointment(values) {
    const errors = {};

    if (!values.specialtyId) errors.especialidad = "Seleccione una especialidad.";
    if (!values.doctorId) errors.medico = "Seleccione un médico.";
    if (!values.date) errors.fecha = "Seleccione una fecha.";
    else if (values.date < getLocalDateString()) errors.fecha = "Seleccione una fecha desde hoy en adelante.";
    if (!values.time) errors.hora = "Seleccione una hora.";
    if (!values.reason) errors.motivo = "Ingrese el motivo de la consulta.";
    if (!values.modality) errors.modalidad = "Seleccione una modalidad.";

    return errors;
}

function showErrors(errors) {
    Object.entries(errors).forEach(([field, message]) => {
        errorElements[field].textContent = message;
    });
}

specialtySelect?.addEventListener("change", fillDoctors);

form?.addEventListener("submit", (event) => {
    event.preventDefault();
    clearErrors();
    successMessage.classList.add("hidden");

    const values = getFormValues();
    const errors = validateAppointment(values);

    if (Object.keys(errors).length > 0) {
        showErrors(errors);
        return;
    }

    const session = getSession();
    const patient = getUserById(session?.userId);
    const specialty = getSpecialties().find((item) => item.specialtyId === values.specialtyId);
    const doctor = getDoctors().find((item) => item.doctorId === values.doctorId);

    if (!patient || !specialty || !doctor) {
        errorElements.medico.textContent = "No fue posible registrar la cita. Actualice la página e intente nuevamente.";
        return;
    }

    saveAppointment({
        appointmentId: getNextAppointmentId(),
        patientUserId: patient.userId,
        patientName: `${patient.firstName} ${patient.lastName}`,
        patientRun: patient.run,
        doctorId: doctor.doctorId,
        doctorName: `${doctor.firstName} ${doctor.lastName}`,
        specialtyId: specialty.specialtyId,
        scheduleSlotId: getNextAppointmentId(),
        specialtyName: specialty.specialtyName,
        date: values.date,
        time: values.time,
        reason: values.reason,
        modality: values.modality,
        appointmentStatus: "PENDING"
    });

    successMessage.classList.remove("hidden");
    form.reset();
    resetSelect(doctorSelect, "Seleccione un médico");
});

initializeBaseSpecialties();
initializeBaseDoctors();
initializeBaseAppointments();
dateInput.min = getLocalDateString();
fillSpecialties();
